package com.iwf.calculator.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.iwf.calculator.exception.AuthenticationException;
import com.iwf.calculator.exception.AuthorizationException;
import com.iwf.calculator.model.auth.AuthUser;
import com.iwf.calculator.model.dto.input.AuthInputDto;
import com.iwf.calculator.model.auth.AuthResult;
import com.iwf.calculator.repository.IUserRepository;
import com.iwf.calculator.service.interfaces.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthService implements IAuthService {
    private static final long JWT_TOKEN_VALIDITY_HOURS = 168;

    private static final String USER_ID_CLAIM = "user-id";

    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    private IUserRepository userRepository;

    public AuthResult authenticate(AuthInputDto input) throws AuthenticationException {
        var user = userRepository.findOneByUsername(input.getUsername());

        if (user.isEmpty() || !BCrypt.checkpw(input.getPassword(), user.get().getPassword())) {
            throw new AuthenticationException();
        }

        var authUser = AuthUser.fromEntity(user.get());

        try {
            var token = makeToken(authUser);
            return AuthResult.create(authUser, token);
        } catch (JsonProcessingException e) {
            throw new AuthenticationException();
        }
    }

    public AuthResult refresh() throws AuthenticationException {
        try {
            AuthUser user = getAuthenticatedUser();
            return AuthResult.create(user, makeToken(user));
        } catch (JsonProcessingException e) {
            throw new AuthenticationException();
        }
    }

    public AuthUser getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() != null
                && authentication.getPrincipal() instanceof AuthUser) {
            return (AuthUser) authentication.getPrincipal();
        }
        return null;
    }

    public AuthUser validateToken(String token) throws AuthorizationException {

        DecodedJWT decodedToken;
        try {
            decodedToken = JWT.require(Algorithm.HMAC256(secret))
                    .build()
                    .verify(token);
        } catch (Exception e) {
            throw new AuthorizationException();
        }

        Long userId = decodedToken.getClaim(USER_ID_CLAIM).asLong();

        var user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new AuthorizationException();
        }

        return AuthUser.fromEntity(user.get());
    }

    private String makeToken(AuthUser user) throws JsonProcessingException {
        return JWT.create()
                .withClaim(USER_ID_CLAIM, user.getId())
                .withExpiresAt(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY_HOURS * 60 * 60 * 1000))
                .sign(Algorithm.HMAC256(secret));
    }

}
