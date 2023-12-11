package com.iwf.calculator.model.dto.view;

import lombok.Data;

@Data
public class ErrorViewDto {
    private String error;
    private Integer statusCode;

    public static ErrorViewDto create(String message, Integer statusCode) {
        var dto = new ErrorViewDto();
        dto.error = message;
        dto.statusCode = statusCode;
        return dto;
    }
}
