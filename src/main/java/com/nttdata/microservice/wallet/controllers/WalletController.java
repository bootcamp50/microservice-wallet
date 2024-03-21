package com.nttdata.microservice.wallet.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nttdata.microservice.wallet.services.IWalletService;


@RestController
@RequestMapping(value = "wallet")
public class WalletController {

	private static Logger logger = Logger.getLogger(WalletController.class);
	@Autowired
	private IWalletService service;
	
}
