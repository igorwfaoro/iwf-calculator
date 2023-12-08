package com.iwf.calculator.exception;

import com.iwf.calculator.constant.ErrorMessages;

public class AuthenticationException extends Exception {

    public AuthenticationException() {
        super(ErrorMessages.AUTHENTICATION_ERROR);
    }
}
