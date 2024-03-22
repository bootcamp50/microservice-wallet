package com.nttdata.microservice.wallet.utils;

import com.nttdata.microservice.wallet.collections.WalletCollection;
import com.nttdata.microservice.wallet.dto.WalletCreateDto;

public interface WalletUtils {
	WalletCollection toWallet(WalletCreateDto walletDTO);
}
