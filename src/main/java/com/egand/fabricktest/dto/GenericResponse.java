package com.egand.fabricktest.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GenericResponse<T> {

    private String status;
    private List<Error> errors = new ArrayList<>();
    private T payload;

}
