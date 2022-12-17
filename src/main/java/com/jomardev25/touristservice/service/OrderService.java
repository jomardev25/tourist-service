package com.jomardev25.touristservice.service;

import com.jomardev25.touristservice.DTO.OrderRequestDTO;
import com.jomardev25.touristservice.DTO.OrderResponseDTO;
import com.jomardev25.touristservice.DTO.OrderSingleResponseDTO;
import com.jomardev25.touristservice.DTO.OrderUpdateRequestDTO;
import com.jomardev25.touristservice.DTO.OrderUpdateStatusRequestDTO;

public interface OrderService {

	public OrderResponseDTO getAllOrders(int page, int size, String sortBy, String sortDir);

	public OrderSingleResponseDTO getOrderById(int id);

	public OrderSingleResponseDTO createOrder(OrderRequestDTO request);

	public OrderSingleResponseDTO updateOrder(int id, OrderUpdateRequestDTO request);

	public OrderSingleResponseDTO updateOrderStatus(int id, OrderUpdateStatusRequestDTO request);

	public void deleteOrder(int id);

}
