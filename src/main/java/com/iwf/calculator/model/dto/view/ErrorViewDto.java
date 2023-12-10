package com.iwf.calculator.model.dto.view;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ErrorViewDto {
    private String message;
    private Integer statusCode;

    public static ErrorViewDto create(String message, Integer statusCode) {
        var error = new ErrorViewDto();
        error.message = message;
        error.statusCode = statusCode;
        return error;
    }
}
