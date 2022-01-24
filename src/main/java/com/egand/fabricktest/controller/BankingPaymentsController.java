package com.egand.fabricktest.controller;

import com.egand.fabricktest.dto.Error;
import com.egand.fabricktest.dto.GenericResponse;
import com.egand.fabricktest.dto.MoneyTransferInfo;
import com.egand.fabricktest.service.FabrickApiService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Collections;

@Slf4j
@RestController
@RequestMapping(path = "/payments", produces = {MediaType.APPLICATION_JSON_VALUE})
public class BankingPaymentsController {

    private final FabrickApiService fabrickService;

    @Autowired
    public BankingPaymentsController(FabrickApiService fabrickService) {
        this.fabrickService = fabrickService;
    }

    /**
     * Effettua un bonifico, utilizzando le informazioni passate in input
     * @param moneyTransferInfo
     * @return Stato dell'operazione (OK, KO, PENDING)
     * @throws Exception
     */
    @PostMapping(path = "/transfer")
    @ApiOperation(value = "Transfer", notes = "Transfer")
    public ResponseEntity<GenericResponse<Void>> moneyTransfer(@Valid @RequestBody MoneyTransferInfo moneyTransferInfo) throws Exception {
        GenericResponse<Void> response = fabrickService.createMoneyTransfer(moneyTransferInfo).block();
        FabrickApiService.checkStatus(response);
        return ResponseEntity.ok(response);
    }

}
