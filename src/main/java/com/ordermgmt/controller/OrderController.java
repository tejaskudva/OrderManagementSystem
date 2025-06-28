package com.ordermgmt.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ordermgmt.models.dto.GenericErrorResponse;
import com.ordermgmt.models.dto.GenericSuccessResponse;
import com.ordermgmt.models.dto.OrderDTO;
import com.ordermgmt.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {
	
	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

	private final OrderService orderService;

	OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@PostMapping("/create")
	public ResponseEntity<Object> createOrder(@RequestBody List<OrderDTO> orders) {
		
		logger.info("Started-createOrder controller");

		try {
			orderService.createOrder(orders);
			logger.info("Finished-createOrder controller");
			return ResponseEntity.ok(new GenericSuccessResponse());

		} catch (Exception e) {
			logger.error("Error-createOrder could not be finished properly", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new GenericErrorResponse());
		}

	}

}
