package com.jomardev25.touristservice.DTO;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jomardev25.touristservice.entity.OrderStatus;
import com.jomardev25.touristservice.entity.Product;

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
public class OrderSingleResponseDTO {

	private int id;
	private String orderedBy;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date dateOrdered;
	private Product product;
	private OrderStatus status;
}
