package com.jomardev25.touristservice.DTO;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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

	@NotNull(message = "Id field must not be empty.")
	private int id;

	@NotEmpty(message = "Order By field must not be empty.")
	private String orderedBy;

	@NotNull(message = "Date Order field must not be empty.")
	private Date dateOrdered;

	@NotEmpty(message = "Product field must not be empty.")
	private String product;

}