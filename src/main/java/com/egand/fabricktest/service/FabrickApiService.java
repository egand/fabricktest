package com.egand.fabricktest.service;

import com.egand.fabricktest.component.FabrickApiProperties;
import com.egand.fabricktest.dto.*;
import com.egand.fabricktest.dto.Error;
import com.egand.fabricktest.exception.FabrickException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;

@Slf4j
@Service
public class FabrickApiService {

    private final WebClient webClient;

    @Autowired
    public FabrickApiService(WebClient.Builder webClientBuilder, FabrickApiProperties properties) {
        // log.info(properties.toString());
        this.webClient = webClientBuilder
                .baseUrl(properties.getBaseUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("Api-Key", properties.getApiKey())
                .defaultHeader("Auth-Schema", properties.getAuthSchema())
                .build();
    }

    public Mono<GenericResponse<AccountBalance>> getAccountBalance(Long accountId) {
        return this.webClient
                .get()
                .uri("/api/gbs/banking/v4.0/accounts/{accountId}/balance", accountId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<GenericResponse<AccountBalance>>(){});
    }

    public Mono<GenericResponse<Void>> createMoneyTransfer(MoneyTransferInfo informations) {
        MoneyTransferRequest request = new MoneyTransferRequest();
        request.setAmount(informations.getAmount());
        request.setCreditor(informations.getTo());
        request.setCurrency(informations.getCurrency());
        request.setDescription(informations.getDescription());
        request.setExecutionDate(informations.getExecutionDate());
        return this.webClient
                .post()
                .uri("/api/gbs/banking/v4.0/accounts/{accountId}/payments/money-transfers", informations.getFromAddressId())
                .body(Mono.just(request), MoneyTransferRequest.class)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<GenericResponse<Void>>(){}); // la chiamata andra' in KO, non mi preoccupo del body
    }

    public Mono<GenericResponse<Transactions>> getAccountTransactions(Long accountId, Date from, Date to) throws FabrickException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return this.webClient
                .get()
                .uri( uriBuilder -> uriBuilder
                        .path("/api/gbs/banking/v4.0/accounts/{accountId}/transactions")
                        .queryParam("fromAccountingDate", df.format(from))
                        .queryParam("toAccountingDate", df.format(to))
                        .build(accountId)
                )
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<GenericResponse<Transactions>>(){});
    }

    public static <T> void checkStatus(GenericResponse<T> response) throws FabrickException {
        if (response == null) {
            throw new FabrickException();
        }
        if (response.getStatus() == null || "KO".equalsIgnoreCase(response.getStatus())) {
            throw new FabrickException(response.getErrors());
        }
    }


}
