package com.nttdata.microservice.wallet.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.nttdata.microservice.wallet.collections.Customer;
import com.nttdata.microservice.wallet.collections.Operation;
import com.nttdata.microservice.wallet.collections.WalletCollection;
import com.nttdata.microservice.wallet.dto.CustomerDto;
import com.nttdata.microservice.wallet.dto.WalletAcceptPurchaseDto;
import com.nttdata.microservice.wallet.dto.WalletCreateDto;
import com.nttdata.microservice.wallet.dto.WalletDto;
import com.nttdata.microservice.wallet.dto.WalletGeneratePurchaseDto;
import com.nttdata.microservice.wallet.enums.CustomerStateEnum;
import com.nttdata.microservice.wallet.enums.CustomerTypeEnum;
import com.nttdata.microservice.wallet.enums.OperationStateEnum;
import com.nttdata.microservice.wallet.enums.OperationTypeEnum;
import com.nttdata.microservice.wallet.enums.WalletStateEnum;
import com.nttdata.microservice.wallet.exceptions.CustomerInactiveException;
import com.nttdata.microservice.wallet.repository.IWalletRepository;
import com.nttdata.microservice.wallet.services.IWalletService;
import com.nttdata.microservice.wallet.utils.CustomerUtils;
import com.nttdata.microservice.wallet.utils.OperationUtils;
import com.nttdata.microservice.wallet.utils.WalletUtils;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class WalletServiceImpl implements IWalletService {
	
	@Autowired
	private IWalletRepository repository;
	
	@Autowired
	private WebClient.Builder webClientBuilder;
	
	@Autowired
	private OperationUtils operationUtils;
	
	@Autowired
	private CustomerUtils customerUtils;
	
	@Autowired
	private WalletUtils walletUtils;
	
	@Override
    public Mono<WalletCollection> findById(String id) {
        log.info("Start of operation to find a wallet by id");

        log.info("Retrieving wallet with id: [{}]", id);
        Mono<WalletCollection> retrievedWallet = repository.findById(new ObjectId(id));
        log.info("Wallet with id: [{}] was retrieved successfully", id);

        log.info("End of operation to find a wallet by id");
        return retrievedWallet;
    }
	
	@Override
    public Flux<WalletDto> findAllDto() {

        log.info("Start of operation to retrieve all wallets");

        log.info("Retrieving all credits");
        Flux<WalletDto> retrievedWallet = repository.findAll().map(walletUtils::toWalletDto);
        log.info("All wallets retrieved successfully");

        log.info("End of operation to retrieve all wallets");
        return retrievedWallet;
    }
	

    @Override
    public Flux<WalletCollection> findAll() {

        log.info("Start of operation to retrieve all wallets");

        log.info("Retrieving all credits");
        Flux<WalletCollection> retrievedWallet = repository.findAll();
        log.info("All wallets retrieved successfully");

        log.info("End of operation to retrieve all wallets");
        return retrievedWallet;
    }
    
    @Override
    public Mono<CustomerDto> findCustomerById(String id) {
        log.info("Start of operation to retrieve customer with id [{}] from customer-service", id);

        log.info("Retrieving customer");
        String url = "http://bank-gateway:8090/person/getCustomer/" + id;
        log.info("url: " + url);
        Mono<CustomerDto> retrievedCustomer = webClientBuilder.build().get()
                        .uri(url)
                        .retrieve()
                        .onStatus(httpStatus -> httpStatus == HttpStatus.NOT_FOUND, clientResponse -> Mono.empty())
                        .bodyToMono(CustomerDto.class);
        log.info("Customer retrieved successfully");

        log.info("End of operation to retrieve customer with id: [{}]", id);
        return retrievedCustomer;
    }
    
    
    @Override
    public Mono<WalletCollection> create(WalletCreateDto walletDTO) {
        log.info("Start of operation to create a credit");

        Mono<WalletCollection> createdWallet = findCustomerById(walletDTO.getCustomer().getId())
                .switchIfEmpty(Mono.just(customerUtils.toCustomerDto(walletDTO.getCustomer())))
                .flatMap(retrievedCustomer -> {
                    log.info("Validating customer");
                    return walletToCreateCustomerValidation(retrievedCustomer);
                })
                .flatMap(validatedCustomer -> {
                    WalletCollection walletToCreate = walletUtils.toWallet(walletDTO);
                    Customer customer = customerUtils.toCustomer(validatedCustomer);

                    walletToCreate.setCustomer(customer);
                    walletToCreate.setState(WalletStateEnum.ACTIVE.toString());
                    walletToCreate.setAmount(50.0);

                    log.info("Creating new wallet: [{}]", walletToCreate.toString());
                    return repository.save(walletToCreate);
                })
                .switchIfEmpty(Mono.error(new NoSuchElementException("Customer does not exist")));

        log.info("End of operation to create a credit");
        return createdWallet;
    }
    
    private Mono<CustomerDto> walletToCreateCustomerValidation(CustomerDto customerFromMicroservice) {
        log.info("Customer exists");

        if (customerFromMicroservice.getState() != null &&
            customerFromMicroservice.getState().contentEquals(CustomerStateEnum.INACTIVE.toString())) {
            log.warn("Customer have blocked status");
            log.warn("Proceeding to abort create wallet");
            return Mono.error(new CustomerInactiveException("The customer have blocked status"));
        }

        if (customerFromMicroservice.getPersonType() != null &&
            customerFromMicroservice.getPersonType().contentEquals(CustomerTypeEnum.PERSONAL.toString()) &&
            (customerFromMicroservice.getPersonalInfo().getNumberDocument() == null ||
             customerFromMicroservice.getPersonalInfo().getMobileNumber() == null ||
             customerFromMicroservice.getPersonalInfo().getEmail() == null)) {
            log.warn("Customer does not contain identity number, mobile number or email");
            log.warn("Proceeding to abort create wallet");
            return Mono.error(new IllegalArgumentException("Customer does not contain identity number, mobile number or email"));
        }

        if (customerFromMicroservice.getPersonType() != null &&
            customerFromMicroservice.getPersonType().contentEquals(CustomerTypeEnum.ENTERPRISE.toString()) &&
            (customerFromMicroservice.getEnterpriseInfo().getRuc() == null ||
             customerFromMicroservice.getEnterpriseInfo().getRepresentatives().get(0).getMobileNumber() == null ||
             customerFromMicroservice.getEnterpriseInfo().getRepresentatives().get(0).getEmail() == null)) {
            log.warn("Customer does not contain ruc, mobile number or email");
            log.warn("Proceeding to abort create wallet");
            return Mono.error(new IllegalArgumentException("Customer does not contain ruc, mobile number or email"));
        }

        log.info("Customer successfully validated");
        return Mono.just(customerFromMicroservice);
    }
    
    
    @Override
    public Mono<WalletCollection> generatePurchaseRequest(WalletGeneratePurchaseDto walletDto) {
        log.info("Start to save a new purchase request for the wallet with id: [{}]", walletDto.getId());

        Mono<WalletCollection> updatedWallet = repository.findById(new ObjectId(walletDto.getId()))
                .flatMap(retrievedWallet -> storeOperationIntoWallet(walletDto, retrievedWallet))
                .flatMap(transformedWallet -> {
                    log.info("Saving operation into wallet: [{}]", transformedWallet.toString());
                    return repository.save(transformedWallet);
                })
                .switchIfEmpty(Mono.error(new NoSuchElementException("Wallet does not exist")));

        log.info("End to save a new purchase request for the wallet with id: [{}]", walletDto.getId());
        return updatedWallet;
    }
    
    private Mono<WalletCollection> storeOperationIntoWallet(WalletGeneratePurchaseDto walletDto, WalletCollection walletInDatabase) {
        Operation operation = operationUtils.toOperation(walletDto.getOperation());
        operation.setId(UUID.randomUUID().toString());
        operation.setStatus(OperationStateEnum.PENDING.toString());
        operation.setTime(new Date());
        operation.setOperationType(OperationTypeEnum.BUY.toString());

        ArrayList<Operation> operations = walletInDatabase.getOperations() == null ? new ArrayList<>() : walletInDatabase.getOperations();
        operations.add(operation);

        walletInDatabase.setOperations(operations);

        return Mono.just(walletInDatabase);
    }

	@Override
    public Mono<WalletCollection> acceptPurchaseRequest(WalletAcceptPurchaseDto walletDto) {
        log.info("Start of operation to accept purchase request");

        log.info("Looking for purchase request operation");
        Mono<WalletCollection> updatedWallet = findAll()
                .filter(retrievedWallet -> {
                    if (retrievedWallet.getOperations() != null) {
                        return retrievedWallet.getOperations()
                                .stream()
                                .anyMatch(operation -> operation.getId() != null &&
                                        operation.getId().contentEquals(walletDto.getOperationId()));
                    }
                    return false;
                })
                .single()
                .flatMap(buyerWallet -> {
                    log.info("Validating consumption operation");
                    return acceptPurchaseValidation(walletDto, buyerWallet);
                })
                .flatMap(buyerValidatedWallet -> {
                    Operation sellerOperation = Operation.builder()
                            .id(UUID.randomUUID().toString())
                            .status(OperationStateEnum.ACEPTED.toString())
                            .time(new Date())
                            .operationType(OperationTypeEnum.SELL.toString())
                            .build();
                    ArrayList<Operation> buyerOperations = buyerValidatedWallet.getOperations();
                    ArrayList<Operation> buyerMappedOperations = new ArrayList<>(buyerOperations.stream()
                            .map(operation -> {
                                if (operation.getId() != null && operation.getId().contentEquals(walletDto.getOperationId())) {
                                    Double bootcoinAmount = buyerValidatedWallet.getAmount() + operation.getAmount();
                                    buyerValidatedWallet.setAmount(bootcoinAmount);

                                    log.info("Updating buyer purchase operation");
                                    String operationNumber = UUID.randomUUID().toString();
                                    operation.setOperationNumber(operationNumber);
                                    operation.setStatus(OperationStateEnum.ACEPTED.toString());

                                    log.info("Updating seller purchase operation");
                                    sellerOperation.setOperationNumber(operationNumber);
                                    sellerOperation.setPaymentType(operation.getPaymentType());
                                    sellerOperation.setAmount(operation.getAmount());
                                }
                                return operation;
                            })
                            .collect(Collectors.toList()));

                    buyerValidatedWallet.setOperations(buyerMappedOperations);
                    log.info("Updating purchase request into wallet: [{}]", buyerValidatedWallet.toString());
                    repository.save(buyerValidatedWallet).subscribe();

                    Mono<WalletCollection> nestedUpdatedSellerWallet = findById(walletDto.getId())
                            .flatMap(sellerWallet -> {
                                ArrayList<Operation> operations = sellerWallet.getOperations() == null ? new ArrayList<>() : sellerWallet.getOperations();
                                operations.add(sellerOperation);
                                sellerWallet.setOperations(operations);
                                sellerWallet.setAmount(sellerWallet.getAmount() - sellerOperation.getAmount());
                                return repository.save(sellerWallet);
                            });

                    log.info("Creating accepting operation into seller wallet");
                    return nestedUpdatedSellerWallet;
                })
                .switchIfEmpty(Mono.error(new NoSuchElementException("Operation does not exist")));

        log.info("End of operation to accept purchase request");
        return updatedWallet;
    }
	
	private Mono<WalletCollection> acceptPurchaseValidation(WalletAcceptPurchaseDto walletDTO, WalletCollection buyerWallet) {
        ArrayList<Operation> operations = buyerWallet.getOperations();
        Optional<Operation> validatedOperation = operations.stream()
                .filter(operation -> operation.getId() != null && operation.getId().contentEquals(walletDTO.getOperationId()))
                .findFirst();

        if(validatedOperation.isPresent()) {
            return findById(walletDTO.getId())
                    .flatMap(sellerWallet -> {
                        if (sellerWallet.getAmount() - validatedOperation.get().getAmount() < 0) {
                            log.info("Account has insufficient funds");
                            log.warn("Proceeding to abort do operation");
                            return Mono.error(new IllegalArgumentException("The account has insufficient funds"));
                        } else {
                            log.info("Operation successfully validated");
                            return Mono.just(buyerWallet);
                        }
                    });
        } else {
            log.info("Could not retrieve operation");
            log.warn("Proceeding to abort do operation");
            return Mono.error(new IllegalArgumentException("Could not retrieve operation"));
        }
    }
	
    
    

}
