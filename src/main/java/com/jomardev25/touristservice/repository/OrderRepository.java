package com.jomardev25.touristservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jomardev25.touristservice.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

}
