package com.nttdata.microservice.wallet.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nttdata.microservice.wallet.repository.IWalletRepository;
import com.nttdata.microservice.wallet.services.IWalletService;

@Service
public class WalletServiceImpl implements IWalletService {
	
	@Autowired
	private IWalletRepository repository;

}
