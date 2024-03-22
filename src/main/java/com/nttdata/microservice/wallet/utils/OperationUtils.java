package com.nttdata.microservice.wallet.utils;

import com.nttdata.microservice.wallet.collections.Operation;
import com.nttdata.microservice.wallet.dto.OperationGeneratePurchaseDto;

public interface OperationUtils {
	Operation toOperation(OperationGeneratePurchaseDto operationDto);
}
