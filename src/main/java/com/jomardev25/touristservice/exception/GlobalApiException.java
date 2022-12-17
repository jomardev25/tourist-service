package com.jomardev25.touristservice.exception;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GlobalApiException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private HttpStatus status;
	private String message;

	public GlobalApiException(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}

}
