package com.egand.fabricktest.controller;

import com.egand.fabricktest.dto.AccountBalance;
import com.egand.fabricktest.dto.GenericResponse;
import com.egand.fabricktest.dto.Transactions;
import com.egand.fabricktest.exception.FabrickException;
import com.egand.fabricktest.service.FabrickApiService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Log
@RestController
@RequestMapping(path = "/account", produces = {MediaType.APPLICATION_JSON_VALUE})
public class BankingAccountController {

    private final FabrickApiService fabrickService;

    @Autowired
    public BankingAccountController(FabrickApiService fabrickService) {
        this.fabrickService = fabrickService;
    }

    @GetMapping(path = "/{accountId}/balance")
    @ApiOperation(value = "Balance", notes = "Balance")
    public ResponseEntity<AccountBalance> accountBalance(@PathVariable("accountId") Long accountId) throws FabrickException {
        GenericResponse<AccountBalance> response = fabrickService.getAccountBalance(accountId).block();
        FabrickApiService.checkStatus(response);
        return ResponseEntity.ok(response.getPayload());
    }

    @GetMapping(path = "/{accountId}/transactions")
    @ApiOperation(value = "Balance", notes = "Balance")
    public ResponseEntity<Transactions> transactionsHistory(@PathVariable("accountId") Long accountId, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)@RequestParam("from") Date from, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)@RequestParam("to") Date to) throws FabrickException {
        GenericResponse<Transactions> response = fabrickService.getAccountTransactions(accountId, from, to).block();
        FabrickApiService.checkStatus(response);
        return ResponseEntity.ok(response.getPayload());
    }

}
