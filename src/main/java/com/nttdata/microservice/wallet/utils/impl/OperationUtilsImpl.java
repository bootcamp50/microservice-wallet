package com.nttdata.microservice.wallet.utils.impl;


import org.springframework.stereotype.Component;

import com.nttdata.microservice.wallet.collections.Operation;
import com.nttdata.microservice.wallet.dto.OperationGeneratePurchaseDto;
import com.nttdata.microservice.wallet.utils.OperationUtils;

@Component
public class OperationUtilsImpl implements OperationUtils {
	@Override
    public Operation toOperation(OperationGeneratePurchaseDto operationDto) {
        return Operation.builder()
                .paymentType(operationDto.getPaymentType())
                .amount(operationDto.getAmount())
                .build();
    }
}
