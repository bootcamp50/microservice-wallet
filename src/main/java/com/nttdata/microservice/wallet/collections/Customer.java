package com.nttdata.microservice.wallet.collections;

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
public class Customer {
	private String id;
    private String identityNumber;
    private String email;
    private String mobileNumber;
}
