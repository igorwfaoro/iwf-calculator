package com.iwf.calculator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iwf.calculator.CalculatorApplication;
import com.iwf.calculator.model.auth.AuthResult;
import com.iwf.calculator.model.dto.input.AuthInputDto;
import com.iwf.calculator.model.dto.input.CalculationInputDto;
import com.iwf.calculator.model.dto.view.CalculationViewDto;
import com.iwf.calculator.service.interfaces.ICalculationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest(classes = CalculatorApplication.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CalculationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ICalculationService calculationService;

    private String authToken;

    @BeforeEach
    public void setup() throws Exception {
        var result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/auth/login")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(objectMapper.writeValueAsString(new AuthInputDto("testuser", "password")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        var authResponse = objectMapper.readValue(result.getResponse().getContentAsString(), AuthResult.class);
        authToken = authResponse.getToken();
    }

    @Test
    public void shouldCalculateExpressions() throws Exception {
        Map<String, String> expressions = new HashMap<>();
        expressions.put("(2+2)*3", "12.0");
        expressions.put("2.3*2.3+5", "10.29");
        expressions.put("2.33/3", "0.78");

        for (Map.Entry<String, String> entry : expressions.entrySet()) {
            CalculationInputDto input = new CalculationInputDto(entry.getKey());
            mockMvc.perform(post("/v1/calculations/calculate")
                            .header("Authorization", "Bearer " + authToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.result").value(entry.getValue()));
        }
    }

    @Test
    public void shouldCalculateExpressionWithCache() throws Exception {
        CalculationInputDto input = new CalculationInputDto("2.2+2.2");

        MvcResult result1 = mockMvc.perform(post("/v1/calculations/calculate")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("4.4"))
                .andReturn();

        Long idFromFirstCall = objectMapper.readValue(result1.getResponse().getContentAsString(), CalculationViewDto.class).getId();

        MvcResult result2 = mockMvc.perform(post("/v1/calculations/calculate")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("4.4"))
                .andReturn();

        Long idFromSecondCall = objectMapper.readValue(result2.getResponse().getContentAsString(), CalculationViewDto.class).getId();

        assertEquals(idFromFirstCall, idFromSecondCall);
    }

    @Test
    public void shouldReturnErrorForDivisionByZero() throws Exception {
        CalculationInputDto input = new CalculationInputDto("1/0");

        mockMvc.perform(post("/v1/calculations/calculate")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Division by zero!"));
    }

    @Test
    public void shouldReturnBadRequestForInvalidExpression() throws Exception {
        CalculationInputDto input = new CalculationInputDto("aaa");

        mockMvc.perform(post("/v1/calculations/calculate")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnAllCalculations() throws Exception {
        var calculations = new LinkedList<CalculationViewDto>();
        calculations.add(new CalculationViewDto(1L, "2+2", 4F));
        calculations.add(new CalculationViewDto(2L, "5/2", 2.5F));
        calculations.add(new CalculationViewDto(3L, "4*5", 20F));
        calculations.add(new CalculationViewDto(4L, "10-3", 7F));

        mockMvc.perform(get("/v1/calculations")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(calculations)));
    }

    @Test
    public void shouldClearAllCalculations() throws Exception {
        mockMvc.perform(delete("/v1/calculations/clear")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        var calculations = calculationService.getAll();
        assertTrue(calculations.isEmpty());
    }
}

