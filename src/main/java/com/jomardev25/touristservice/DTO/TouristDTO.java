package com.jomardev25.touristservice.DTO;

import java.util.List;

import com.jomardev25.touristservice.entity.Tourist;

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
public class TouristDTO {

	private int page;
	private int perPage;
	private int totalrecord;
	private int totalPages;
	private List<Tourist> data;

}
