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
public class CustomerDto {
	private String id;
    private String personType;
    private String state;
    private CustomerPersonalInfoDto personalInfo;
    private CustomerEnterpriseInfoDto enterpriseInfo;

}
