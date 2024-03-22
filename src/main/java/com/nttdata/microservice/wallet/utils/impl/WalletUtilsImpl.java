package com.nttdata.microservice.wallet.utils.impl;

import org.springframework.stereotype.Component;

import com.nttdata.microservice.wallet.collections.WalletCollection;
import com.nttdata.microservice.wallet.dto.WalletCreateDto;
import com.nttdata.microservice.wallet.dto.WalletDto;
import com.nttdata.microservice.wallet.utils.WalletUtils;

@Component
public class WalletUtilsImpl implements WalletUtils {
	@Override
    public WalletCollection toWallet(WalletCreateDto walletDto) {
        return WalletCollection.builder()
                .customer(walletDto.getCustomer())
                .build();
    }

	@Override
	public WalletDto toWalletDto(WalletCollection wallet) {
		return WalletDto.builder()
				.id(String.valueOf(wallet.getId()))
				.amount(wallet.getAmount())
				.customer(wallet.getCustomer())
				.operations(wallet.getOperations())
				.state(wallet.getState())
				.build();
	}
}
