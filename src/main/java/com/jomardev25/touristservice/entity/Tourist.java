package com.jomardev25.touristservice.entity;

import java.util.Date;

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
public class Tourist {

	private long id;
	private String touristName;
	private String touristEmail;
	private String touristLocation;
	private Date createdat;

}
