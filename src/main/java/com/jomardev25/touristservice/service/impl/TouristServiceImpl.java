package com.jomardev25.touristservice.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.jomardev25.touristservice.DTO.TouristDTO;
import com.jomardev25.touristservice.DTO.TouristRequestDTO;
import com.jomardev25.touristservice.DTO.TouristResponseDTO;
import com.jomardev25.touristservice.entity.Tourist;
import com.jomardev25.touristservice.exception.GlobalApiException;
import com.jomardev25.touristservice.service.TouristService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class TouristServiceImpl implements TouristService {

	private final RestTemplate restTemplate;
	private final ModelMapper modelMapper;

	@Value("${root-service-url}")
	private String ROOT_SERVICE_URL;

	@Override
	@CircuitBreaker(name="getTouristServiceBreaker", fallbackMethod = "getAllTouristsFallback") // Circuit will handle the failed called
	@Retry(name="getTouristServiceRetry")
	public List<TouristResponseDTO> getAllTourists() {
		String serviceUrl = String.format("%s?page=3", ROOT_SERVICE_URL);
		log.info("GET: {}", serviceUrl);
		ResponseEntity<TouristDTO> restTemplateResponse = restTemplate.exchange(serviceUrl, HttpMethod.GET, null, new ParameterizedTypeReference<TouristDTO>(){});
		log.info("Response: {}", restTemplateResponse.getBody());


		List<Tourist> tourists = restTemplateResponse.getBody().getData();
		List<TouristResponseDTO> response = tourists.stream()
												.map(tourist -> modelMapper.map(tourist, TouristResponseDTO.class))
												.sorted((o1, o2) -> o1.getTouristEmail().compareTo(o2.getTouristEmail()))
												.collect(Collectors.toList());

		return response;
	}

	@Override
	@CircuitBreaker(name="getTouristServiceBreaker", fallbackMethod = "getTouristFallback") // Circuit will handle the failed called
	@Retry(name="getTouristServiceRetry")
	public TouristResponseDTO getTouristById(long id) {
		String serviceUrl = String.format("%s/%d", ROOT_SERVICE_URL, id);
		log.info("GET: {}", serviceUrl);
		ResponseEntity<Tourist> restTemplateResponse = null;
		restTemplateResponse = restTemplate.exchange(serviceUrl, HttpMethod.GET, null, Tourist.class);
		log.info("Response: {}", restTemplateResponse.getBody());
		TouristResponseDTO response = modelMapper.map(restTemplateResponse.getBody(), TouristResponseDTO.class);
		return response;
	}

	@Override
	@CircuitBreaker(name="getTouristServiceBreaker", fallbackMethod = "getTouristFallback") // Circuit will handle the failed called
	@Retry(name="getTouristServiceRetry")
	public TouristResponseDTO createTourist(TouristRequestDTO request) {
		HttpHeaders header = new HttpHeaders();
		header.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		header.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<TouristRequestDTO> touristRequestDTO = new HttpEntity<>(request, header);
		log.info("POST: {}, Payload: {}", ROOT_SERVICE_URL, request);
		ResponseEntity<Tourist> restTemplateResponse = restTemplate.exchange(ROOT_SERVICE_URL, HttpMethod.POST, touristRequestDTO, Tourist.class);
		log.info("Response: {}", restTemplateResponse);
		TouristResponseDTO response = modelMapper.map(restTemplateResponse.getBody(), TouristResponseDTO.class);
		return response;
	}

	@Override
	@CircuitBreaker(name="getTouristServiceBreaker", fallbackMethod = "getTouristFallback") // Circuit will handle the failed called
	@Retry(name="getTouristServiceRetry")
	public TouristResponseDTO updateTourist(long id, TouristRequestDTO request) {
		/*
		 	http://restapi.adequateshop.com/api/Tourist/200089 - An error occurred
		 	405 - HTTP verb used to access this page is not allowed.
		 	HTTP Verb: PUT
		*/
		String serviceUrl = String.format("%s/%d", ROOT_SERVICE_URL, id);
		HttpHeaders header = new HttpHeaders();
		header.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		header.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<TouristRequestDTO> touristRequestDTO = new HttpEntity<>(request, header);
		log.info("PUT: {}, Payload: {}", serviceUrl, request);
		ResponseEntity<TouristResponseDTO> restTemplateResponse = restTemplate.exchange(ROOT_SERVICE_URL, HttpMethod.PUT, touristRequestDTO, TouristResponseDTO.class);
		log.info("Response: {}", restTemplateResponse);
		TouristResponseDTO response = modelMapper.map(restTemplateResponse.getBody(), TouristResponseDTO.class);
		return response;
	}

	@Override
	@CircuitBreaker(name="getTouristServiceBreaker", fallbackMethod = "deleteTouristFalback") // Circuit will handle the failed called
	@Retry(name="getTouristServiceRetry")
	public void deleteTourist(long id) {
		/*
		 	http://restapi.adequateshop.com/api/Tourist/200089 - An error occurred
		 	405 - HTTP verb used to access this page is not allowed.
		 	HTTP Verb: DELETE
		*/
		String serviceUrl = String.format("%s/%d", ROOT_SERVICE_URL, id);
		log.info("DELETE: {}", serviceUrl);
		ResponseEntity<String> response = restTemplate.exchange(serviceUrl, HttpMethod.DELETE, null, String.class);
		log.info("Response: {}", response);
	}

	public List<TouristResponseDTO> getAllTouristsFallback(Exception ex) {
		log.error("AllTouristsFallback: {}", ex.getMessage());
		fallback(ex);
		return null;
	}

	public TouristResponseDTO getTouristFallback(Exception ex) {
		log.error("GetTouristFallback: {}", ex.getMessage());
		fallback(ex);
		return null;
	}

	public void deleteTouristFalback(Exception ex) {
		log.error("DeleteTouristFallback: {}", ex.getMessage());
		fallback(ex);
	}

	private void fallback(Exception ex) {
		int statusCode = Integer.parseInt(ex.getMessage().substring(0, 3));
		String message = getErrorMessage(ex);
		GlobalApiException globalApiException = GlobalApiException.builder()
													.status(HttpStatus.valueOf(statusCode))
													.build();
		if(statusCode == 404) {
			globalApiException.setMessage("Resource not found.");
		}else if(statusCode == 405) {
			globalApiException.setMessage("HTTP verb used to access this page is not allowed.");
		}else{
			globalApiException.setMessage(message);
		}

		throw globalApiException;
	}

	private String getErrorMessage(Exception ex) {
		String message = "";
		if(ex.getMessage().contains("Message")) {
			message = ex.getMessage().substring(ex.getMessage().indexOf("Message") + 10, ex.getMessage().length() - 3);
		}
		return message;
	}
}