package com.egand.fabricktest.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class Creditor {
    @NotBlank(message = "This field is mandatory")
    @Size(max = 70, message = "This field should not be greater than 70 characters")
    private String name;
    private Account account;

}
