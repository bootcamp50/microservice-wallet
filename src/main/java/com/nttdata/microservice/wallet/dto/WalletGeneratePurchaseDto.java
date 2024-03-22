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
@ToString
@Builder
public class WalletGeneratePurchaseDto {
	private String id;
    private OperationGeneratePurchaseDto operation;
}
