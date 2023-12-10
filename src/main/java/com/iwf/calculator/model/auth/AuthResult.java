package com.iwf.calculator.model.auth;
import lombok.Data;

@Data
public class AuthResult {

    private AuthUser user;
    private String token;

    public static AuthResult create(AuthUser user, String token) {
        var viewDto = new AuthResult();
        viewDto.user = user;
        viewDto.token = token;
        return viewDto;
    }
}
