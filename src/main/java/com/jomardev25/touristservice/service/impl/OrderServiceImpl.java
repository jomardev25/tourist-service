package com.jomardev25.touristservice.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.jomardev25.touristservice.DTO.OrderRequestDTO;
import com.jomardev25.touristservice.DTO.OrderResponseDTO;
import com.jomardev25.touristservice.DTO.OrderSingleResponseDTO;
import com.jomardev25.touristservice.DTO.OrderUpdateRequestDTO;
import com.jomardev25.touristservice.DTO.OrderUpdateStatusRequestDTO;
import com.jomardev25.touristservice.entity.Order;
import com.jomardev25.touristservice.entity.OrderStatus;
import com.jomardev25.touristservice.entity.Product;
import com.jomardev25.touristservice.exception.ResourceNotFoundException;
import com.jomardev25.touristservice.repository.OrderRepository;
import com.jomardev25.touristservice.repository.ProductRepository;
import com.jomardev25.touristservice.service.OrderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;
	private final ModelMapper modelMapper;

	@Override
	public OrderResponseDTO getAllOrders(int page, int size, String sortBy, String sortDir) {
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(page - 1, size, sort);
		Page<Order> orders = orderRepository.findAll(pageable);
		List<OrderSingleResponseDTO> data =  orders.getContent().stream()
												.map(order -> modelMapper.map(order, OrderSingleResponseDTO.class))
												.collect(Collectors.toList());
		OrderResponseDTO response = OrderResponseDTO.builder()
										.page(page)
										.perPage(orders.getSize())
										.totalPages(orders.getTotalPages())
										.totalRecord(orders.getTotalElements())
										.first(orders.isFirst())
										.last(orders.isLast())
										.data(data)
										.build();
		return response;
	}

	@Override
	public OrderSingleResponseDTO getOrderById(int id) {
		Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
		log.info("{}", order);
		return modelMapper.map(order, OrderSingleResponseDTO.class);
	}

	@Override
	public OrderSingleResponseDTO createOrder(OrderRequestDTO request) {
		Product product = productRepository.findByName(request.getProduct())
							.orElseThrow(() -> new ResourceNotFoundException("Product", "name", request.getProduct()));

		Order order = Order.builder()
						.id(request.getId())
						.orderedBy(request.getOrderedBy())
						.dateOrdered(request.getDateOrdered())
						.product(product)
						.build();

		return modelMapper.map(orderRepository.save(order), OrderSingleResponseDTO.class);
	}

	@Override
	public OrderSingleResponseDTO updateOrder(int id, OrderUpdateRequestDTO request) {
		Product product = productRepository.findByName(request.getProduct())
				.orElseThrow(() -> new ResourceNotFoundException("Product", "name", request.getProduct()));
		Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
		order.setProduct(product);
		return modelMapper.map(orderRepository.save(order), OrderSingleResponseDTO.class);
	}

	@Override
	public OrderSingleResponseDTO updateOrderStatus(int id, OrderUpdateStatusRequestDTO request) {
		Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
		OrderStatus status = OrderStatus.valueOf(request.getStatus());
		order.setStatus(status);
		order = orderRepository.save(order);
		return modelMapper.map(order, OrderSingleResponseDTO.class);
	}

	@Override
	public void deleteOrder(int id) {
		Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
		orderRepository.delete(order);
	}
}
