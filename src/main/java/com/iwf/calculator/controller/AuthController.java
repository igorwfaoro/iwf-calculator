package com.iwf.calculator.controller;

import com.iwf.calculator.constant.ApiConstants;
import com.iwf.calculator.exception.AuthenticationException;
import com.iwf.calculator.model.dto.input.AuthInputDto;
import com.iwf.calculator.model.dto.input.CalculationInputDto;
import com.iwf.calculator.model.dto.view.AuthViewDto;
import com.iwf.calculator.model.dto.view.CalculationViewDto;
import com.iwf.calculator.service.AuthService;
import com.iwf.calculator.service.CalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/" + ApiConstants.V1 + "/" + ApiConstants.CONTROLLER_AUTH)
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(ApiConstants.RESOURCE_LOGIN)
    public AuthViewDto authenticate(@RequestBody AuthInputDto input) throws AuthenticationException {
        return authService.authenticate(input);
    }

    @PostMapping(ApiConstants.RESOURCE_REFRESH)
    public AuthViewDto refresh() throws AuthenticationException {
        return authService.refresh();
    }
}
