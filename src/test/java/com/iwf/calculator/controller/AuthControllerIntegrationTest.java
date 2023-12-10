package com.iwf.calculator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iwf.calculator.CalculatorApplication;
import com.iwf.calculator.model.auth.AuthResult;
import com.iwf.calculator.model.dto.input.AuthInputDto;
import com.iwf.calculator.service.interfaces.IAuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = CalculatorApplication.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IAuthService authService;

    AuthResult authResult;

    @BeforeEach
    public void setup() throws Exception {
        var authResponse = mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/auth/login")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(objectMapper.writeValueAsString(new AuthInputDto("testuser", "password")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        authResult = objectMapper.readValue(authResponse.getResponse().getContentAsString(), AuthResult.class);
    }

    @Test
    public void shouldAuthenticate() throws Exception {
        var validAuthUser = authService.validateToken(authResult.getToken());

        assertEquals(validAuthUser.getUsername(), authResult.getUser().getUsername());
        assertEquals(validAuthUser.getFullName(), authResult.getUser().getFullName());
    }

    @Test
    public void shouldRefreshToken() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/auth/refresh")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .header("Authorization", "Bearer " + authResult.getToken())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }
}

