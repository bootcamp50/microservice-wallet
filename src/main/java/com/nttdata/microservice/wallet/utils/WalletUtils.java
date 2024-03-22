package com.nttdata.microservice.wallet.utils;

import com.nttdata.microservice.wallet.collections.WalletCollection;
import com.nttdata.microservice.wallet.dto.WalletCreateDto;
import com.nttdata.microservice.wallet.dto.WalletDto;

public interface WalletUtils {
	WalletCollection toWallet(WalletCreateDto walletDTO);
	WalletDto toWalletDto(WalletCollection wallet);
}
