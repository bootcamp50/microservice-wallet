package com.nttdata.microservice.wallet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CustomerPersonalInfoDto {
	private String firstName;
	private String lastName;
	private String typeDocument;
	private String numberDocument;
	private String email;
    private String phoneNumber;
    private String mobileNumber;
}
