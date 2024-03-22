package com.nttdata.microservice.wallet.services;


import com.nttdata.microservice.wallet.collections.WalletCollection;
import com.nttdata.microservice.wallet.dto.CustomerDto;
import com.nttdata.microservice.wallet.dto.WalletAcceptPurchaseDto;
import com.nttdata.microservice.wallet.dto.WalletCreateDto;
import com.nttdata.microservice.wallet.dto.WalletGeneratePurchaseDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IWalletService {
	Mono<WalletCollection> create(WalletCreateDto walletDto);
    Mono<WalletCollection> findById(String id);
    Flux<WalletCollection> findAll();
    
    Mono<CustomerDto> findCustomerById(String id);
    Mono<WalletCollection> generatePurchaseRequest(WalletGeneratePurchaseDto walletDto);
    Mono<WalletCollection> acceptPurchaseRequest(WalletAcceptPurchaseDto walletDto);
}
