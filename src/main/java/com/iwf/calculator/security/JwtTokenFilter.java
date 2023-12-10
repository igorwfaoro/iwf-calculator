package com.iwf.calculator.security;

import com.auth0.jwt.JWT;
import com.iwf.calculator.constant.ApiConstants;
import com.iwf.calculator.exception.AuthenticationException;
import com.iwf.calculator.model.auth.AuthUser;
import com.iwf.calculator.service.AuthService;
import jakarta.security.auth.message.AuthException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private static final String[] REQUESTS_PATHS_WHITELIST = {
            "/" + ApiConstants.V1 + "/" + ApiConstants.CONTROLLER_AUTH + "/" + ApiConstants.RESOURCE_LOGIN
    };

    @Autowired
    private AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (Arrays.stream(REQUESTS_PATHS_WHITELIST).anyMatch(path -> path.equals(request.getRequestURI()))) {
            filterChain.doFilter(request, response);
            return;
        }

        String headerAuth = request.getHeader(HttpHeaders.AUTHORIZATION);

        var token = headerAuth.split(" ")[1].trim();

        AuthUser user;

        try {
            user = authService.validateToken(token);
        } catch (AuthenticationException e) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid token.");
            return;
        }

        var authentication = new UsernamePasswordAuthenticationToken(
                user,
                null
        );

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
