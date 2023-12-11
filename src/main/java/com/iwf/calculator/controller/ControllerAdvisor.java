package com.iwf.calculator.controller;


import com.iwf.calculator.exception.AuthenticationException;
import com.iwf.calculator.exception.AuthorizationException;
import com.iwf.calculator.exception.ExpressionException;
import com.iwf.calculator.model.dto.view.ErrorViewDto;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@RestController
public class ControllerAdvisor {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorViewDto handleExceptions(Exception ex) {
        LOGGER.error(ex.getLocalizedMessage(), ex);
        return ErrorViewDto.create("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ErrorViewDto handleExceptions(HttpServletRequest request, Exception ex) {
        return ErrorViewDto.create(ex.getMessage(), HttpStatus.METHOD_NOT_ALLOWED.value());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ErrorViewDto handleExceptions(HttpMessageNotReadableException ex) {
        return ErrorViewDto.create("Required request body not present", HttpStatus.BAD_REQUEST.value());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorViewDto handleExceptions(IllegalArgumentException ex) {
        return ErrorViewDto.create(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AuthenticationException.class)
    public ErrorViewDto handleExceptions(AuthenticationException ex) {
        return ErrorViewDto.create(ex.getMessage(), HttpStatus.FORBIDDEN.value());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthorizationException.class)
    public ErrorViewDto handleExceptions(AuthorizationException ex) {
        return ErrorViewDto.create(ex.getMessage(), HttpStatus.UNAUTHORIZED.value());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ExpressionException.class)
    public ErrorViewDto handleExceptions(ExpressionException ex) {
        return ErrorViewDto.create(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
    }
}
