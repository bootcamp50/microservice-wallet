package com.nttdata.microservice.wallet.dto;

import java.util.ArrayList;

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
public class CustomerEnterpriseInfoDto {
	private String ruc;
	private String companyName;
	private ArrayList<CustomerPersonalInfoDto> representatives;
}
