package com.ordermgmt.rabbitmq.consumers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.ordermgmt.service.impl.OrderServiceImpl;

@Component
public class OrderEventConsumer {

	private static final Logger logger = LoggerFactory.getLogger(OrderEventConsumer.class);

	@Autowired
	private OrderServiceImpl orderImpl;

	@RabbitListener(queues = "order.queue", containerFactory = "rabbitListenerContainerFactory")
	public void handleOrderCreated(@Payload Long orderId) throws Exception {

		logger.info("Received employee event: " + orderId);
		// Add your business logic here (email, notifications, etc)
		orderImpl.completeOrder(orderId);
	}

}
