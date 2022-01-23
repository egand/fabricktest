package com.egand.fabricktest.exception;

import com.egand.fabricktest.dto.Error;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class FabrickException extends Exception {

    private List<Error> errors;

    public FabrickException(List<Error> errors) {
        this.errors = errors;
    }

    public FabrickException() {
        errors = new ArrayList<>();
        errors.add(new Error("000", "Generic Error"));
    }
}
