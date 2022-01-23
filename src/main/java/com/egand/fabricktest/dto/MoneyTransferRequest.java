package com.egand.fabricktest.dto;

import lombok.Data;

@Data
public class MoneyTransferRequest {

    private Creditor creditor;
    private String description;
    private Number amount;
    private String currency;
    private String executionDate;

}
