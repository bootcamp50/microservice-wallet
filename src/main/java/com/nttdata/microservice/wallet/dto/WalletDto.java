package com.nttdata.microservice.wallet.dto;

import java.util.ArrayList;

import org.bson.types.ObjectId;

import com.nttdata.microservice.wallet.collections.Customer;
import com.nttdata.microservice.wallet.collections.Operation;

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
public class WalletDto {
	private String id;
    private String state;
    private Customer customer;
    private Double amount;
    private ArrayList<Operation> operations;
}
