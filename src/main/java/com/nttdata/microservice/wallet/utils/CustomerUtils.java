package com.nttdata.microservice.wallet.utils;

import com.nttdata.microservice.wallet.collections.Customer;
import com.nttdata.microservice.wallet.dto.CustomerDto;

public interface CustomerUtils {
	
	Customer toCustomer(CustomerDto customerDto);
	CustomerDto toCustomerDto(Customer customer);
	
}
