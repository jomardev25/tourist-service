package com.jomardev25.touristservice.service;

import java.util.List;

import com.jomardev25.touristservice.DTO.TouristRequestDTO;
import com.jomardev25.touristservice.DTO.TouristResponseDTO;

public interface TouristService {

	public List<TouristResponseDTO> getAllTourists();

	public TouristResponseDTO getTouristById(long id);

	public TouristResponseDTO createTourist(TouristRequestDTO request);

	public TouristResponseDTO updateTourist(long id, TouristRequestDTO request);

	public void deleteTourist(long id);

}
