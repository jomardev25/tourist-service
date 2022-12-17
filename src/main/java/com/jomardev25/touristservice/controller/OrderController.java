package com.jomardev25.touristservice.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jomardev25.touristservice.DTO.OrderRequestDTO;
import com.jomardev25.touristservice.DTO.OrderResponseDTO;
import com.jomardev25.touristservice.DTO.OrderSingleResponseDTO;
import com.jomardev25.touristservice.DTO.OrderUpdateRequestDTO;
import com.jomardev25.touristservice.DTO.OrderUpdateStatusRequestDTO;
import com.jomardev25.touristservice.service.OrderService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

	private final OrderService orderService;

	@GetMapping
	public ResponseEntity<OrderResponseDTO> getAllOrders(
			@RequestParam(name = "page", defaultValue = "1", required = false) int page,
			@RequestParam(name = "size", defaultValue = "10", required = false) int size,
			@RequestParam(name = "sort_by", defaultValue = "id", required = false) String sortBy,
			@RequestParam(name = "sort_dir", defaultValue = "asc", required = false) String sortDir
	) {

		return ResponseEntity.ok().body(orderService.getAllOrders(page, size, sortBy, sortDir));
	}

	@GetMapping("/{id}")
	public ResponseEntity<OrderSingleResponseDTO> getOrderById(@PathVariable(name = "id") int id) {
		return ResponseEntity.ok().body(orderService.getOrderById(id));
	}

	@PostMapping
	public ResponseEntity<OrderSingleResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO request) {
		return new ResponseEntity<OrderSingleResponseDTO>(orderService.createOrder(request), HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<OrderSingleResponseDTO> updateOrder(@PathVariable(name = "id") int id, @Valid @RequestBody OrderUpdateRequestDTO request) {
		return ResponseEntity.ok().body(orderService.updateOrder(id, request));
	}

	@PutMapping("/status/{id}")
	public ResponseEntity<OrderSingleResponseDTO> updateOrderStatus(@PathVariable(name = "id") int id, @Valid @RequestBody OrderUpdateStatusRequestDTO request) {
		return ResponseEntity.ok().body(orderService.updateOrderStatus(id, request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteOrder(@PathVariable(name = "id") int id) {
		orderService.deleteOrder(id);
		return ResponseEntity.ok().body("Order deleted successfully.");
	}

}


