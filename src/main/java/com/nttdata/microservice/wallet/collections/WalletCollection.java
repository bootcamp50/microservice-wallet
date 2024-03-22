package com.nttdata.microservice.wallet.collections;

import java.util.ArrayList;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
@Document(value = "wallets")
public class WalletCollection {
	
	
	@Id
    private ObjectId id;
    private String state;
    private Customer customer;
    private Double amount;
    private ArrayList<Operation> operations;

}
