package com.nttdata.microservice.wallet.collections;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "wallets")
public class WalletCollection {
	
	@Id
	private ObjectId id;
	private String documentNumber;
	private String phone;
	private String imei;
	private String email;
	
	private String state;
	private Date createdAt;
	private Date updatedAt;
	private Date deletedAt;

}
