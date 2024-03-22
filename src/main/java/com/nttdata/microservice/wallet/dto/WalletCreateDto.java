package com.nttdata.microservice.wallet.dto;

import com.nttdata.microservice.wallet.collections.Customer;

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
public class WalletCreateDto {
	private Customer customer;
}
