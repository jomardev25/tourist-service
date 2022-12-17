package com.jomardev25.touristservice.DTO;

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
public class OrderUpdateStatusRequestDTO {

	@NotEmpty(message = "Order Status field must not be empty.")
	@NotNull(message = "Order Status field must not be empty.")
	private String status;

}