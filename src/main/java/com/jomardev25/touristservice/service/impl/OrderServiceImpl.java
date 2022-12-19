package com.jomardev25.touristservice.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.jomardev25.touristservice.DTO.OrderRequestDTO;
import com.jomardev25.touristservice.DTO.OrderResponseDTO;
import com.jomardev25.touristservice.DTO.OrderSingleResponseDTO;
import com.jomardev25.touristservice.DTO.OrderUpdateRequestDTO;
import com.jomardev25.touristservice.DTO.OrderUpdateStatusRequestDTO;
import com.jomardev25.touristservice.DTO.TouristResponseDTO;
import com.jomardev25.touristservice.entity.Order;
import com.jomardev25.touristservice.entity.OrderStatus;
import com.jomardev25.touristservice.entity.Product;
import com.jomardev25.touristservice.exception.GlobalApiException;
import com.jomardev25.touristservice.exception.ResourceNotFoundException;
import com.jomardev25.touristservice.repository.OrderRepository;
import com.jomardev25.touristservice.repository.ProductRepository;
import com.jomardev25.touristservice.service.OrderService;
import com.jomardev25.touristservice.service.TouristService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;
	private final TouristService touristService;
	private final RestTemplate restTemplate;
	private final ModelMapper modelMapper;

	@Override
	public OrderResponseDTO getAllOrders(int page, int size, String sortBy, String sortDir) {
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(page - 1, size, sort);
		Page<Order> orders = orderRepository.findAll(pageable);

		List<OrderSingleResponseDTO> data = getTourists(orders.getContent());

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

		TouristResponseDTO tourist = touristService.getTouristById(order.getTouristId());

		OrderSingleResponseDTO response = OrderSingleResponseDTO
											.builder()
											.id(order.getId())
											.dateOrdered(order.getDateOrdered())
											.tourist(tourist)
											.product(order.getProduct())
											.status(order.getStatus())
											.build();



		return response;
	}

	@Override
	public OrderSingleResponseDTO createOrder(OrderRequestDTO request) {
		Product product = productRepository.findById(request.getProductId())
							.orElseThrow(() -> new ResourceNotFoundException("Product", "id", request.getProductId()));

		Order order = Order.builder()
						.dateOrdered(request.getDateOrdered())
						.touristId(request.getTouristId())
						.product(product)
						.build();

		TouristResponseDTO tourist = touristService.getTouristById(request.getTouristId());
		OrderSingleResponseDTO orderSingleResponseDTO = modelMapper.map(orderRepository.save(order), OrderSingleResponseDTO.class);
		orderSingleResponseDTO.setTourist(tourist);
		return orderSingleResponseDTO;
	}

	@Override
	public OrderSingleResponseDTO updateOrder(int id, OrderUpdateRequestDTO request) {
		Product product = productRepository.findById(request.getProductId())
				.orElseThrow(() -> new ResourceNotFoundException("Product", "id", request.getProductId()));
		Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
		order.setProduct(product);
		OrderSingleResponseDTO orderSingleResponseDTO = modelMapper.map(orderRepository.save(order), OrderSingleResponseDTO.class);

		TouristResponseDTO tourist = touristService.getTouristById(order.getTouristId());
		orderSingleResponseDTO.setTourist(tourist);

		return orderSingleResponseDTO;
	}

	@Override
	public OrderSingleResponseDTO updateOrderStatus(int id, OrderUpdateStatusRequestDTO request) {
		Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
		OrderStatus status = OrderStatus.valueOf(request.getStatus());
		order.setStatus(status);
		OrderSingleResponseDTO orderSingleResponseDTO = modelMapper.map(orderRepository.save(order), OrderSingleResponseDTO.class);

		TouristResponseDTO tourist = touristService.getTouristById(order.getTouristId());
		orderSingleResponseDTO.setTourist(tourist);

		return orderSingleResponseDTO;
	}

	@Override
	public void deleteOrder(int id) {
		Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
		orderRepository.delete(order);
	}

	private List<OrderSingleResponseDTO> getTourists(List<Order> Orders) {

		List<CompletableFuture<Void>> completableFutures = new ArrayList<>(Orders.size());
		ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		List<OrderSingleResponseDTO> response = new ArrayList<>();

		for (Order order : Orders) {
			CompletableFuture<Void> requestCompletableFuture = CompletableFuture
	                .supplyAsync(
	                		() -> restTemplate.exchange("http://restapi.adequateshop.com/api/Tourist/" + order.getTouristId(), HttpMethod.GET, null, TouristResponseDTO.class),
	                        executor
	                )
	                .thenApply((responseEntity) -> {
	                    return responseEntity.getBody();
	                })
	                .thenAccept((responseEntity) -> {
	                	OrderSingleResponseDTO orderSingleResponseDTO =  modelMapper.map(order, OrderSingleResponseDTO.class);
	                	orderSingleResponseDTO.setTourist(responseEntity);
	                	response.add(orderSingleResponseDTO);
	                })
	                .exceptionally(ex -> {
	                	ex.printStackTrace();
	                    return null;
	                });

	        completableFutures.add(requestCompletableFuture);
		}

		try {
	        CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[completableFutures.size()])).get();
	    } catch (InterruptedException ex) {
	        ex.printStackTrace();
	        throw new GlobalApiException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
	    } catch (ExecutionException ex) {
	    	throw new GlobalApiException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
	    }

		return response;
	}

}
