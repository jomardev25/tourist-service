package com.jomardev25.touristservice.DTO;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

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
public class TouristRequestDTO {

	@NotEmpty(message = "Tourist Name field must not be empty.")
	private String touristName;

	@NotEmpty(message = "Tourist Email field must not be empty.")
	@Email(message = "Tourist Email field must be a valid email address.")
	private String touristEmail;

	@NotEmpty(message = "Tourist Email field must not be empty.")
	private String touristLocation;

}
