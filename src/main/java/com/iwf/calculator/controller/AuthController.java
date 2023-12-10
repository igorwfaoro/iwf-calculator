package com.iwf.calculator.controller;

import com.iwf.calculator.constant.ApiConstants;
import com.iwf.calculator.exception.AuthenticationException;
import com.iwf.calculator.model.dto.input.AuthInputDto;
import com.iwf.calculator.model.auth.AuthResult;
import com.iwf.calculator.service.interfaces.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/" + ApiConstants.V1 + "/" + ApiConstants.CONTROLLER_AUTH)
public class AuthController {

    @Autowired
    private IAuthService authService;

    @PostMapping(ApiConstants.RESOURCE_LOGIN)
    public AuthResult authenticate(@RequestBody AuthInputDto input) throws AuthenticationException {
        return authService.authenticate(input);
    }

    @PostMapping(ApiConstants.RESOURCE_REFRESH)
    public AuthResult refresh() throws AuthenticationException {
        return authService.refresh();
    }
}
