package com.ordermgmt.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ordermgmt.models.dto.GenericApiResponse;
import com.ordermgmt.models.dto.OrderDTO;
import com.ordermgmt.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {

	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

	private final OrderService orderService;
	private final JdbcTemplate jdbc;

	OrderController(OrderService orderService, JdbcTemplate jdbc) {
		this.orderService = orderService;
		this.jdbc = jdbc;
	}

	@PostMapping("/create")
	public ResponseEntity<Object> createOrder(@RequestBody List<OrderDTO> orders) {

		logger.info("Started-createOrder controller");

		try {
			orderService.createOrder(orders);
			logger.info("Finished-createOrder controller");
			return ResponseEntity.ok(new GenericApiResponse("Orders created Successful"));

		} catch (Exception e) {
			logger.error("Error-createOrder could not be finished properly", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new GenericApiResponse("Error in creating orders"));
		}

	}

	@GetMapping("/get/{orderId}")
	public ResponseEntity<Object> getOrderById(@PathVariable Long orderId) {
		try {

			return ResponseEntity.ok(orderService.getOrderById(orderId));

		} catch (Exception e) {
			logger.error("Error-getOrderById could not be finished properly", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new GenericApiResponse("Error in fetching order"));
		}
	}

	@GetMapping("/getByName")
	public ResponseEntity<Object> getOrderById(@RequestParam String customerName) {
		try {

			return ResponseEntity.ok(orderService.getOrderByCustomerName(customerName));

		} catch (Exception e) {
			logger.error("Error-getOrderById could not be finished properly", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new GenericApiResponse("Error in fetching order"));
		}
	}

	@GetMapping("/test")
	public String test(){
		
		List<Map<String, Object>> output = jdbc.queryForList("select api_request from api_logs");
		System.out.println(output);
		return "Hello";
	}

}
