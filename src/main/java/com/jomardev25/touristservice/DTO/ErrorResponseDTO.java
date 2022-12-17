package com.jomardev25.touristservice.DTO;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ErrorResponseDTO {

	private Date timestamp;
	private String message;
	private String details;

}
