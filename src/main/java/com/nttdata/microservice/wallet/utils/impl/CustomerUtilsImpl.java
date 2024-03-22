package com.nttdata.microservice.wallet.utils.impl;

import org.springframework.stereotype.Component;

import com.nttdata.microservice.wallet.collections.Customer;
import com.nttdata.microservice.wallet.dto.CustomerDto;
import com.nttdata.microservice.wallet.dto.CustomerPersonalInfoDto;
import com.nttdata.microservice.wallet.enums.CustomerTypeEnum;
import com.nttdata.microservice.wallet.utils.CustomerUtils;

@Component
public class CustomerUtilsImpl implements CustomerUtils {
	
	@Override
    public Customer toCustomer(CustomerDto customerDto) {
        if (customerDto.getEnterpriseInfo() == null) {
            return Customer.builder()
                    .id(customerDto.getId())
                    .identityNumber(customerDto.getPersonalInfo().getNumberDocument())
                    .email(customerDto.getPersonalInfo().getEmail())
                    .mobileNumber(customerDto.getPersonalInfo().getMobileNumber())
                    .build();
        } else {
            return Customer.builder()
                    .id(customerDto.getId())
                    .identityNumber(customerDto.getEnterpriseInfo().getRuc())
                    .email(customerDto.getEnterpriseInfo().getRepresentatives().get(0).getEmail())
                    .mobileNumber(customerDto.getEnterpriseInfo().getRepresentatives().get(0).getMobileNumber())
                    .build();
        }
    }
	
	@Override
    public CustomerDto toCustomerDto(Customer customer) {
		return CustomerDto.builder()
                .personType(CustomerTypeEnum.PERSONAL.toString())
                .personalInfo(CustomerPersonalInfoDto.builder()
                        .numberDocument(customer.getIdentityNumber())
                        .email(customer.getEmail())
                        .mobileNumber(customer.getMobileNumber())
                        .build())
                .build();
    }
	

}
