package com.egand.fabricktest.controller;

import com.egand.fabricktest.dto.Error;
import com.egand.fabricktest.dto.GenericResponse;
import com.egand.fabricktest.exception.FabrickException;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.Collections;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public GenericResponse<Void> handleException(Exception ex) {
        log.error(ex.getMessage(), ex);
        GenericResponse<Void> response = new GenericResponse<>();
        response.setErrors(Collections.singletonList(new Error("000", "Generic Error")));
        response.setStatus("KO");
        return response;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public GenericResponse<Void> handleValidationException(
            MethodArgumentNotValidException ex) {
        GenericResponse<Void> response = new GenericResponse<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            response.getErrors().add(new Error("001", ((FieldError) error).getField() + ": " + error.getDefaultMessage()));
        });
        response.setStatus("KO");
        return response;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FabrickException.class)
    public GenericResponse<Void> handleFabrickException(
            FabrickException ex) {
        GenericResponse<Void> response = new GenericResponse<>();
        response.setErrors(ex.getErrors());
        response.setStatus("KO");
        return response;
    }
}
