package com.egand.fabricktest.dto;

import lombok.Data;

import java.util.Date;

@Data
public class AccountBalance {

    private Date date;
    private Number balance;
    private Number availableBalance;
    private String currency;

}
