package com.egand.fabricktest.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class MoneyTransferInfo {
    @NotNull(message = "This field is mandatory")
    private Long fromAddressId;

    private Creditor to;

    @NotBlank(message = "This field is mandatory")
    @Size(max = 140, message = "This field should not be greater than 140 characters")
    private String description;

    @NotNull(message = "This field is mandatory")
    private Number amount;

    @NotBlank(message = "This field is mandatory")
    private String currency;

    @NotBlank(message = "This field is mandatory")
    private String executionDate;


}
