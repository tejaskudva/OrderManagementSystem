package com.ordermgmt.service.impl;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import com.ordermgmt.helpers.RestTemplateWrapper;
import com.ordermgmt.mapstructs.OrderMapper;
import com.ordermgmt.models.dto.ExternalServiceWrapperResponse;
import com.ordermgmt.models.dto.OrderDTO;
import com.ordermgmt.models.entity.Order;
import com.ordermgmt.rabbitmq.producers.OrderEventProducer;
import com.ordermgmt.repository.OrderRepository;
import com.ordermgmt.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

	private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

	private final OrderRepository orderRepo;
	private final OrderMapper orderMapper;
	@Qualifier("orderTaskExecutor")
	private final TaskExecutor taskExecutor;

	private final OrderEventProducer orderEventProducer;
	private final RestTemplateWrapper apiWrapper;

	@Value("${order.batchsize}")
	private int batchSize;

	OrderServiceImpl(OrderRepository orderRepo, OrderMapper orderMapper, TaskExecutor taskExecutor,
			OrderEventProducer orderEventProducer, RestTemplateWrapper apiWrapper) {
		this.orderRepo = orderRepo;
		this.orderMapper = orderMapper;
		this.taskExecutor = taskExecutor;
		this.orderEventProducer = orderEventProducer;
		this.apiWrapper = apiWrapper;
	}

	@Override
	public void createOrder(List<OrderDTO> orders) {

		for (int i = 0; i < orders.size(); i += batchSize) {

			System.out.println("Batch number: " + i + 1);

			List<OrderDTO> batchOrder = orders.subList(i, Math.min(i + batchSize, orders.size()));

			List<CompletableFuture<Void>> futures = batchOrder.stream().map(order -> CompletableFuture.runAsync(() -> {
				logOrderInDbAndPublish(orderMapper.toEntity(order));
			}, taskExecutor)).collect(Collectors.toList());

			// Wait for current batch to finish before processing the next
			CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
		}
	}

	private void logOrderInDbAndPublish(Order order) {
		orderEventProducer.sendOrderCreatedEvent(orderRepo.save(order).getId());
	}

	public void completeOrder(Long orderId) throws Exception {

		ExternalServiceWrapperResponse response = apiWrapper.externalServiceWrapper("userinfo", null,
				String.valueOf(orderId), null);

		logger.info("API response: " + response);

		if (!response.getHttpCode().is2xxSuccessful()) {
			throw new Exception("API Failure");
		}

		orderRepo.updateOrderStatus(orderId, "COMPLETED");
	}

	@Override
	public OrderDTO getOrderById(Long orderId) {

		return orderMapper.toDto(orderRepo.findById(orderId).orElseThrow());

	}

	@Override
	public List<OrderDTO> getOrderByCustomerName(String customerName) {

		List<Order> orders = orderRepo.findByCustomerName(customerName);

		return orders.stream().map(orderMapper::toDto).collect(Collectors.toList());
	}

}
