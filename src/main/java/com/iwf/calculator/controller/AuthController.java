package com.iwf.calculator.controller;

import com.iwf.calculator.constant.ApiConstants;
import com.iwf.calculator.exception.AuthenticationException;
import com.iwf.calculator.model.dto.input.AuthInputDto;
import com.iwf.calculator.model.auth.AuthResult;
import com.iwf.calculator.service.interfaces.IAuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiConstants.CONTROLLER_AUTH_ROUTE)
@Tag(name = "Auth")
public class AuthController {

    @Autowired
    private IAuthService authService;

    @PostMapping(ApiConstants.RESOURCE_AUTH_LOGIN)
    public AuthResult authenticate(@RequestBody AuthInputDto input) throws AuthenticationException {
        return authService.authenticate(input);
    }

    @PostMapping(ApiConstants.RESOURCE_AUTH_REFRESH)
    public AuthResult refresh() throws AuthenticationException {
        return authService.refresh();
    }
}
