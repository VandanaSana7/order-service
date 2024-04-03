package com.order.in.service;

import java.util.List;

import com.order.in.entity.Order;

public interface OrderService {

	public  Order placeOrder(Order order);
	public List<Order> getAllOrders();
	public Order findOrder(Long orderId);
	
}
