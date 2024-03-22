package com.nttdata.microservice.wallet.controllers;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nttdata.microservice.wallet.collections.WalletCollection;
import com.nttdata.microservice.wallet.dto.WalletAcceptPurchaseDto;
import com.nttdata.microservice.wallet.dto.WalletCreateDto;
import com.nttdata.microservice.wallet.dto.WalletDto;
import com.nttdata.microservice.wallet.dto.WalletGeneratePurchaseDto;
import com.nttdata.microservice.wallet.exceptions.CustomerInactiveException;
import com.nttdata.microservice.wallet.services.IWalletService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@Slf4j
@RequestMapping(value = "wallet")
public class WalletController {

	@Autowired
	private IWalletService service;
	
	@GetMapping("/findAll")
    public Flux<WalletDto> findAll(){
        log.info("Get operation in /wallets");
        return service.findAllDto();
    }
	
	@GetMapping("/findWalletById/{id}")
    public Mono<ResponseEntity<WalletCollection>> findWalletById(@PathVariable("id") String id) {
        log.info("Get operation in /wallets/{}", id);
        return service.findById(id)
                .flatMap(retrievedWallet -> Mono.just(ResponseEntity.ok(retrievedWallet)))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @PostMapping("/createWallet")
    public Mono<ResponseEntity<WalletCollection>> createWallet(@RequestBody WalletCreateDto walletDto) {
        log.info("Post operation in /wallets");
        return service.create(walletDto)
                .flatMap(createdWallet -> Mono.just(ResponseEntity.status(HttpStatus.CREATED).body(createdWallet)))
                .onErrorResume(CustomerInactiveException.class, error -> Mono.just(ResponseEntity.status(HttpStatus.LOCKED).build()))
                .onErrorResume(IllegalArgumentException.class, error -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build()));
    }

    

    //region UseCases
    @PostMapping("/generatePurchaseRequest")
    public Mono<ResponseEntity<WalletCollection>> generatePurchaseRequest(@RequestBody WalletGeneratePurchaseDto walletDto) {
        log.info("Post operation in /wallets/operations/request");
        return service.generatePurchaseRequest(walletDto)
                .flatMap(updatedWallet -> Mono.just(ResponseEntity.status(HttpStatus.CREATED).body(updatedWallet)))
                .onErrorResume(NoSuchElementException.class, error -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
    }


    @PostMapping("/acceptPurchaseRequest")
    public Mono<ResponseEntity<WalletCollection>> acceptPurchaseRequest(@RequestBody WalletAcceptPurchaseDto walletDto) {
        log.info("Post operation in /wallets/operations/request");
        return service.acceptPurchaseRequest(walletDto)
                .flatMap(updatedWallet -> Mono.just(ResponseEntity.status(HttpStatus.CREATED).body(updatedWallet)))
                .onErrorResume(IllegalArgumentException.class, error -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build()))
                .onErrorResume(NoSuchElementException.class, error -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
    }
    
}
