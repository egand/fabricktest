package com.egand.fabricktest.controller;

import com.egand.fabricktest.dto.AccountBalance;
import com.egand.fabricktest.dto.GenericResponse;
import com.egand.fabricktest.dto.Transactions;
import com.egand.fabricktest.service.FabrickApiService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BankingAccountControllerTest {

    @InjectMocks
    private BankingAccountController controller;

    @Mock
    private FabrickApiService fabrickService;

    private MockMvc mockMvc;

    private static final String CTRL_URL = "/account";


    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void accountBalance() throws Exception {
        GenericResponse<AccountBalance> response = new GenericResponse<>();
        response.setStatus("OK");
        when(fabrickService.getAccountBalance(eq(123L))).thenReturn(Mono.just(response));
        mockMvc.perform(MockMvcRequestBuilders.get(CTRL_URL + "/123/balance"))
                .andExpect(status().isOk());
    }

    @Test
    void transactionsHistory() throws Exception {
        GenericResponse<Transactions> response = new GenericResponse<>();
        response.setStatus("OK");
        when(fabrickService.getAccountTransactions(eq(123L), any(Date.class), any(Date.class))).thenReturn(Mono.just(response));
        mockMvc.perform(MockMvcRequestBuilders.get(CTRL_URL + "/123/transactions")
                    .queryParam("from", "2022-01-01")
                    .queryParam("to", "2022-12-01"))
                .andExpect(status().isOk());
    }
}