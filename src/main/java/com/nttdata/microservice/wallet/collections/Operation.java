package com.nttdata.microservice.wallet.collections;

import java.util.Date;

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
@ToString
@Builder
public class Operation {
	private String id;
    private String operationNumber;
    private String status;
    private Date time;
    private String operationType;
    private String paymentType;
    private Double amount;
}
