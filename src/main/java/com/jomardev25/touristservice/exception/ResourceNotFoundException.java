package com.jomardev25.touristservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private String resource;
	private String field;
	private Object value;


	public ResourceNotFoundException(String resource, String field, Object value) {
		super(String.format("%s not found with %s: %s ", resource, field, value));
		this.resource = resource;
		this.field = field;
		this.value = value;
	}
}
