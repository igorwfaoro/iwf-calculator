package com.iwf.calculator.service;

import com.iwf.calculator.exception.AuthenticationException;
import com.iwf.calculator.model.auth.AuthResult;
import com.iwf.calculator.model.dto.input.AuthInputDto;
import com.iwf.calculator.model.auth.AuthUser;
import com.iwf.calculator.model.entity.User;
import com.iwf.calculator.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceTest {
    private AuthInputDto mockInputDto;
    private User mockUser;
    private AuthUser mockAuthUser;

    @InjectMocks
    private AuthService authService;

    @Mock
    private IUserRepository userRepository;

    @BeforeEach
    void setUpEach() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(authService, "secret", "secret_test");

        LocalDateTime mockDate = LocalDateTime.of(2024, 12, 9, 17, 30);
        mockInputDto = new AuthInputDto("username", "12345");
        mockUser = new User(1L, "username", "User Name", "$2a$10$au48r6VHDWZSsuU75bkJs.ra9cr47xdnFGKN8u/PYtLSccJKXJbZW", mockDate);
        mockAuthUser = AuthUser.fromEntity(mockUser);
    }

    @Test
    public void shouldAuthenticateWithValidCredentials() throws AuthenticationException {
        when(userRepository.findOneByUsername(mockInputDto.getUsername())).thenReturn(Optional.of(mockUser));
        when(userRepository.findById(mockAuthUser.getId())).thenReturn(Optional.of(mockUser));
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        AuthResult result = authService.authenticate(mockInputDto);

        assertNotNull(result);
        assertNotNull(result.getToken());
        assertEquals(mockAuthUser, result.getUser());
    }

    @Test
    public void shouldThrowAuthenticationExceptionWhenAuthenticateWithInvalidCredentials() {
        AuthInputDto inputDto = new AuthInputDto("username", "12345");
        when(userRepository.findOneByUsername(inputDto.getUsername())).thenReturn(Optional.empty());
        assertThrows(AuthenticationException.class, () -> authService.authenticate(inputDto));
    }

    @Test
    public void shouldRefreshToken() throws AuthenticationException {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(mockAuthUser, null);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        when(userRepository.findById(mockAuthUser.getId())).thenReturn(Optional.of(mockUser));
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        AuthResult result = authService.refresh();

        assertNotNull(result);
        assertNotNull(result.getToken());
        assertEquals(mockAuthUser, result.getUser());
    }

    @Test
    public void shouldValidateTokenWithValidToken() throws AuthenticationException {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyLWlkIjoxLCJleHAiOjIwMTc3NTI3MDZ9.UfYeP-zw3dxJd0pyVn_jp9UjHoJzUJFEgjUHZw1kNOE";

        when(userRepository.findById(mockAuthUser.getId())).thenReturn(Optional.of(mockUser));

        AuthUser result = authService.validateToken(token);

        assertNotNull(result);
        assertEquals(mockAuthUser, result);
    }

    @Test
    public void shouldThrowAuthenticationExceptionWhenValidateTokenWithInvalidToken() {
        String invalidToken = "invalid_token";
        assertThrows(AuthenticationException.class, () -> authService.validateToken(invalidToken));
    }

    @Test
    public void shouldGetAuthenticatedUser() {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(mockAuthUser, null);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        AuthUser result = authService.getAuthenticatedUser();

        assertNotNull(result);
        assertEquals(mockAuthUser, result);
    }
}
