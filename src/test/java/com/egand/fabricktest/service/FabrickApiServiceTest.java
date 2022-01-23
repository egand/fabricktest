package com.egand.fabricktest.service;

import com.egand.fabricktest.component.FabrickApiProperties;
import com.egand.fabricktest.dto.*;
import com.egand.fabricktest.exception.FabrickException;
import com.egand.fabricktest.utils.TestUtils;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FabrickApiServiceTest {

    private static MockWebServer mockWebService;

    private static FabrickApiService service;

    private static SimpleDateFormat dateFormat;

    @BeforeAll
    static void setUp() throws IOException {
        mockWebService = new MockWebServer();
        mockWebService.start();
        FabrickApiProperties properties = new FabrickApiProperties();
        properties.setBaseUrl(String.format("http://localhost:%s", mockWebService.getPort()));
        properties.setApiKey("apiKey");
        properties.setAuthSchema("authSchema");
        service = new FabrickApiService(WebClient.builder(), properties);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebService.shutdown();
    }

    @Test
    void getAccountBalance() throws InterruptedException {
        GenericResponse<AccountBalance> response = new GenericResponse<>();
        AccountBalance ab = new AccountBalance();
        ab.setBalance(1);
        ab.setCurrency("EUR");
        response.setPayload(ab);
        mockWebService.enqueue(
                new MockResponse().setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(TestUtils.toJson(response))
        );

        GenericResponse<AccountBalance> result = service.getAccountBalance(123L).block();
        RecordedRequest request = mockWebService.takeRequest();
        assertEquals("GET", request.getMethod());
        assertEquals("/api/gbs/banking/v4.0/accounts/123/balance", request.getPath());
        assertNotNull(result);
        assertEquals(1, result.getPayload().getBalance().intValue());
        assertEquals("EUR", result.getPayload().getCurrency());
    }

    @Test
    void createMoneyTransfer() throws InterruptedException {
        MoneyTransferInfo request = new MoneyTransferInfo();
        request.setAmount(1);
        request.setCurrency("EUR");
        request.setDescription("descr");
        request.setFromAddressId(123L);
        request.setTo(new Creditor());
        GenericResponse<Void> response = new GenericResponse<>();

        mockWebService.enqueue(
                new MockResponse().setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(TestUtils.toJson(response))
        );

        GenericResponse<Void> result = service.createMoneyTransfer(request).block();
        RecordedRequest recordedRequest = mockWebService.takeRequest();
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("/api/gbs/banking/v4.0/accounts/123/payments/money-transfers", recordedRequest.getPath());
        assertNotNull(result);
    }

    @Test
    void getAccountTransactions() throws ParseException, InterruptedException, FabrickException {
        GenericResponse<Transactions> response = new GenericResponse<>();
        response.setStatus("OK");

        Transactions transactions = new Transactions();
        transactions.setList(Collections.singletonList(new Transaction()));
        response.setPayload(transactions);
        mockWebService.enqueue(
                new MockResponse().setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(TestUtils.toJson(response))
        );
        Date from = dateFormat.parse("2022-01-01");
        Date to = dateFormat.parse("2022-12-01");
        GenericResponse<Transactions> result = service.getAccountTransactions(123L, from, to).block();

        RecordedRequest recordedRequest = mockWebService.takeRequest();
        assertEquals("GET", recordedRequest.getMethod());
        HttpUrl requestUrl = recordedRequest.getRequestUrl();
        assertNotNull(requestUrl);
        assertEquals("/api/gbs/banking/v4.0/accounts/123/transactions", requestUrl.encodedPath());
        String qryParams = requestUrl.encodedQuery();
        assertNotNull(qryParams);
        assertTrue(qryParams.contains("fromAccountingDate"));
        assertTrue(qryParams.contains("toAccountingDate"));
        assertNotNull(result);
        assertNotNull(result.getPayload().getList());
        assertEquals(1, result.getPayload().getList().size());
    }

    @Test
    void checkStatus_ExpectedFabrickException_WhenArgumentIsNull() {
        assertThrows(FabrickException.class, () -> {
            FabrickApiService.checkStatus(null);
        });
    }

    @Test
    void checkStatus_ExpectedFabrickException_WhenStatusIsKO() {
        assertThrows(FabrickException.class, () -> {
            GenericResponse<Void> response = new GenericResponse<>();
            response.setStatus("KO");
            FabrickApiService.checkStatus(response);
        });
    }
}