package com.jomardev25.touristservice.DTO;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderRequestDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull(message = "Tourist Id field must not be empty.")
	private int touristId;

	@NotNull(message = "Date Order field must not be empty.")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date dateOrdered;

	@NotNull(message = "Product field must not be empty.")
	private int productId;

}