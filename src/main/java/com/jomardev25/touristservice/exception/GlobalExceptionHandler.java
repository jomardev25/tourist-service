package com.jomardev25.touristservice.exception;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.jomardev25.touristservice.DTO.ErrorResponseDTO;
import com.jomardev25.touristservice.utils.StringUtils;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler  {

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest webRequest){
		ErrorResponseDTO errorResponse = new ErrorResponseDTO(new Date(), ex.getMessage(), webRequest.getDescription(false));
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(GlobalApiException.class)
    public ResponseEntity<ErrorResponseDTO> handleGlobalApiException(GlobalApiException ex, WebRequest webRequest){
		ErrorResponseDTO errorResponse = new ErrorResponseDTO(new Date(), ex.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(errorResponse, ex.getStatus());
    }

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponseDTO> handleGlobalException(Exception ex, WebRequest webRequest){
		ErrorResponseDTO errorResponse = new ErrorResponseDTO(new Date(), ex.getMessage(), webRequest.getDescription(false));
	    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex,
			HttpHeaders headers,
			HttpStatus status,
			WebRequest request
	) {

		Map<String, String> errors = new HashMap<String, String>();
		ex.getBindingResult().getAllErrors().forEach(error -> {
			String field = ((FieldError)error).getField();
			String message = error.getDefaultMessage();
			errors.put(StringUtils.camelToSnake(field), message);
		});

		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}

}
