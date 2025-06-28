package com.ordermgmt.service;

import java.util.List;
import java.util.concurrent.ExecutionException;

import com.ordermgmt.models.dto.OrderDTO;

public interface OrderService {

	void createOrder(List<OrderDTO> orders) throws ExecutionException, InterruptedException;

}
