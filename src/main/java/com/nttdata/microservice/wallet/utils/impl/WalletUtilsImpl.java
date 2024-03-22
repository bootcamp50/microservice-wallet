package com.nttdata.microservice.wallet.utils.impl;

import org.springframework.stereotype.Component;

import com.nttdata.microservice.wallet.collections.WalletCollection;
import com.nttdata.microservice.wallet.dto.WalletCreateDto;
import com.nttdata.microservice.wallet.utils.WalletUtils;

@Component
public class WalletUtilsImpl implements WalletUtils {
	@Override
    public WalletCollection toWallet(WalletCreateDto walletDto) {
        return WalletCollection.builder()
                .customer(walletDto.getCustomer())
                .build();
    }
}
