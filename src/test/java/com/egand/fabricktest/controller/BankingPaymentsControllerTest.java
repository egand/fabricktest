package com.egand.fabricktest.controller;

import com.egand.fabricktest.dto.AccountBalance;
import com.egand.fabricktest.dto.Creditor;
import com.egand.fabricktest.dto.GenericResponse;
import com.egand.fabricktest.dto.MoneyTransferInfo;
import com.egand.fabricktest.exception.FabrickException;
import com.egand.fabricktest.service.FabrickApiService;
import com.egand.fabricktest.utils.TestUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.util.NestedServletException;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BankingPaymentsControllerTest {

    @InjectMocks
    private BankingPaymentsController controller;

    @Mock
    private FabrickApiService fabrickService;

    private MockMvc mockMvc;

    private static final String CTRL_URL = "/payments";

    private MoneyTransferInfo moneyTransferInfo;


    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        Creditor c = new Creditor();
        c.setName("name");
        moneyTransferInfo = new MoneyTransferInfo();
        moneyTransferInfo.setTo(c);
        moneyTransferInfo.setFromAddressId(123L);
        moneyTransferInfo.setDescription("descr");
        moneyTransferInfo.setCurrency("EUR");
        moneyTransferInfo.setAmount(1);
        moneyTransferInfo.setExecutionDate("01/01/2019");
    }

    @Test
    void createMoneyTransfer() throws Exception {

        GenericResponse<Void> response = new GenericResponse<>();
        response.setStatus("OK");
        when(fabrickService.createMoneyTransfer(any(MoneyTransferInfo.class))).thenReturn(Mono.just(response));
        mockMvc.perform(MockMvcRequestBuilders.post(CTRL_URL + "/transfer").contentType(APPLICATION_JSON).content(TestUtils.toJson(moneyTransferInfo)))
                .andExpect(status().isOk());
    }

    @Test
    void createMoneyTransfer_ExpectedFabrickException_WhenStatusIsKO() throws Exception {
        GenericResponse<Void> response = new GenericResponse<>();
        response.setStatus("KO");
        when(fabrickService.createMoneyTransfer(any(MoneyTransferInfo.class))).thenReturn(Mono.just(response));
        Assertions.assertThatThrownBy(() -> {
            mockMvc.perform(MockMvcRequestBuilders.post(CTRL_URL + "/transfer").contentType(APPLICATION_JSON).content(TestUtils.toJson(moneyTransferInfo)))
                    .andExpect(status().isBadRequest());
        }).hasCause(new FabrickException());

    }

    @Test
    void createMoneyTransfer_ExpectedBadRequest_WhenArgsAreNotValid() throws Exception {
        moneyTransferInfo.setDescription("");
        GenericResponse<Void> response = new GenericResponse<>();
        response.setStatus("KO");
        mockMvc.perform(MockMvcRequestBuilders.post(CTRL_URL + "/transfer").contentType(APPLICATION_JSON).content(TestUtils.toJson(moneyTransferInfo)))
                .andExpect(status().isBadRequest());

    }
}