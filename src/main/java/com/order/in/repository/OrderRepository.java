package com.order.in.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.order.in.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
	
}
