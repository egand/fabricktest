package com.egand.fabricktest.controller;

import com.egand.fabricktest.dto.AccountBalance;
import com.egand.fabricktest.dto.GenericResponse;
import com.egand.fabricktest.dto.Transactions;
import com.egand.fabricktest.exception.FabrickException;
import com.egand.fabricktest.service.FabrickApiService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Slf4j
@RestController
@RequestMapping(path = "/account", produces = {MediaType.APPLICATION_JSON_VALUE})
public class BankingAccountController {

    private final FabrickApiService fabrickService;

    @Autowired
    public BankingAccountController(FabrickApiService fabrickService) {
        this.fabrickService = fabrickService;
    }

    /**
     * Recupera il saldo dell'account passato in input.
     * @param accountId account in input
     * @return Il saldo, con la valuta di riferimento
     * @throws FabrickException nel caso l'endpoint richiamato dalle API della banca rispondesse KO
     */
    @GetMapping(path = "/{accountId}/balance")
    @ApiOperation(value = "Balance", notes = "Balance")
    public ResponseEntity<AccountBalance> accountBalance(@PathVariable("accountId") Long accountId) throws FabrickException {
        GenericResponse<AccountBalance> response = fabrickService.getAccountBalance(accountId).block();
        FabrickApiService.checkStatus(response);
        return ResponseEntity.ok(response.getPayload());
    }

    /**
     * Recupera la lista delle transazioni per l'account passato in input con data compresa tra from e to
     * @param accountId account in input
     * @param from data inizio
     * @param to data fine
     * @return lista delle transazioni
     * @throws FabrickException nel caso l'endpoint richiamato dalle API della banca rispondesse KO
     */
    @GetMapping(path = "/{accountId}/transactions")
    @ApiOperation(value = "Balance", notes = "Balance")
    public ResponseEntity<Transactions> transactionsHistory(@PathVariable("accountId") Long accountId, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)@RequestParam("from") Date from, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)@RequestParam("to") Date to) throws FabrickException {
        GenericResponse<Transactions> response = fabrickService.getAccountTransactions(accountId, from, to).block();
        FabrickApiService.checkStatus(response);
        return ResponseEntity.ok(response.getPayload());
    }

}
