package com.jomardev25.touristservice.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

import com.jomardev25.touristservice.DTO.TouristRequestDTO;
import com.jomardev25.touristservice.DTO.TouristResponseDTO;
import com.jomardev25.touristservice.service.TouristService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tourists")
public class TouristController {

	private final TouristService touristService;

	@GetMapping
	public ResponseEntity<List<TouristResponseDTO>> getAllTourists() {
		return ResponseEntity.ok().body(touristService.getAllTourists());
	}

	@GetMapping("/{id}")
	public ResponseEntity<TouristResponseDTO> getByTouristById(@PathVariable(name = "id") long id) {
		return ResponseEntity.ok().body(touristService.getTouristById(id));
	}

	@PostMapping
	public ResponseEntity<TouristResponseDTO> createTourist(@Valid @RequestBody TouristRequestDTO request) {
		return new ResponseEntity<TouristResponseDTO>(touristService.createTourist(request), HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<TouristResponseDTO> updateTourist(@PathVariable(name = "id") long id,  @Valid @RequestBody TouristRequestDTO request) {
		return new ResponseEntity<TouristResponseDTO>(touristService.updateTourist(id, request), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public  ResponseEntity<String> deleteTourist(@PathVariable(name = "id") long id) {
		touristService.deleteTourist(id);
		return ResponseEntity.ok().body("Tourist information deleted successfully.");
	}
}
