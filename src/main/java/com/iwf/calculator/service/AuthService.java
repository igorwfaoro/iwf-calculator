package com.iwf.calculator.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.iwf.calculator.exception.AuthenticationException;
import com.iwf.calculator.model.dto.auth.UserAuth;
import com.iwf.calculator.model.dto.input.AuthInputDto;
import com.iwf.calculator.model.dto.view.AuthViewDto;
import com.iwf.calculator.repository.IUserRepository;
import com.iwf.calculator.util.JsonUtil;
import com.iwf.calculator.util.JwtTokenUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthService {

    private static final long JWT_TOKEN_VALIDITY_HOURS = 168;

    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    private IUserRepository userRepository;

    public AuthViewDto login(AuthInputDto input) {
        return null;
    }

    public AuthViewDto authenticate(AuthInputDto input) throws AuthenticationException {
        var user = userRepository.findOneByUsername(input.getUsername());

        if (user.isEmpty()) {
            throw new AuthenticationException();
        }

        var userAuth = UserAuth.fromUser(user.get());

        try {
            var token = makeToken(userAuth);
            return AuthViewDto.create(userAuth, token);
        } catch (JsonProcessingException e) {
            throw new AuthenticationException();
        }
    }

    public AuthViewDto refresh() throws AuthenticationException {
        try {
            UserAuth user = JwtTokenUtil.getTokenPayload(UserAuth.class);
            return AuthViewDto.create(user, makeToken(user));
        } catch (JsonProcessingException e) {
            throw new AuthenticationException();
        }
    }

//    public UserAuth getAuthenticatedUser() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.getPrincipal() != null
//                && authentication.getPrincipal() instanceof UserAuth) {
//            return (UserAuth) authentication.getPrincipal();
//        }
//        return null;
//    }

    public Boolean validateToken(String token) throws JsonProcessingException {
        UserAuth user = null;
        try {
            user = JwtTokenUtil.getTokenPayload(token, UserAuth.class);
        } catch(Exception e) {
            return false;
        }
        if(user == null)
            return false;

        // TODO: resolve this
//        if (new Date().after(user.getExp()))
//            return false;

        return true;
    }

    private String makeToken(UserAuth user) throws JsonProcessingException {
        return Jwts.builder()
                .setPayload(JsonUtil.serialize(user))
                .signWith(SignatureAlgorithm.HS512, secret)
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY_HOURS * 60 * 60 * 1000))
                .compact();
    }

}
