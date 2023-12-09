package com.iwf.calculator.service.interfaces;

import com.iwf.calculator.exception.AuthenticationException;
import com.iwf.calculator.model.auth.AuthResult;
import com.iwf.calculator.model.auth.AuthUser;
import com.iwf.calculator.model.dto.input.AuthInputDto;

public interface IAuthService {
    AuthResult authenticate(AuthInputDto input) throws AuthenticationException;
    AuthResult refresh() throws AuthenticationException;
    AuthUser getAuthenticatedUser();
    AuthUser validateToken(String token) throws AuthenticationException;
}
