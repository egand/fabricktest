package com.egand.fabricktest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Error {
    private String code;
    private String description;
    private String params;

    public Error(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
