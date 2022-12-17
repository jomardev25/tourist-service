package com.jomardev25.touristservice.DTO;

import java.util.List;

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
public class OrderResponseDTO {

	private int page;
	private int perPage;
	private int totalPages;
	private long totalRecord;
	private boolean first;
	private boolean last;
	private List<OrderSingleResponseDTO> data;

}
