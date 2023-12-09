package com.iwf.calculator.services;

import com.iwf.calculator.model.dto.input.CalculationInputDto;
import com.iwf.calculator.model.entity.Calculation;
import com.iwf.calculator.repository.ICalculationRepository;
import com.iwf.calculator.service.CalculationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CalculationServiceTest {

    @Mock
    private ICalculationRepository calculationRepository;

    @InjectMocks
    private CalculationService calculationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldSaveNewExpression() {

        var input = new CalculationInputDto("2+2");
        var calculation = new Calculation(1L, input.getExpression(), 4F, LocalDateTime.of(2024, 12, 9, 17, 30));

        when(calculationRepository.findOneByExpression(input.getExpression())).thenReturn(Optional.of(calculation));
        when(calculationRepository.save(any(Calculation.class))).thenReturn(calculation);

        var result = calculationService.calculate(input);

        assertNotNull(result);
        assertEquals("2+2", result.getExpression());
        assertEquals(4F, result.getResult());
    }

    @Test
    public void shouldReturnExistingCalculation() {
        var input = new CalculationInputDto("2+2");
        var calculation = new Calculation(1L, input.getExpression(), 4F, LocalDateTime.of(2024, 12, 9, 17, 30));

        when(calculationRepository.findOneByExpression(input.getExpression())).thenReturn(Optional.of(calculation));

        var result = calculationService.calculate(input);

        assertNotNull(result);
        assertEquals("2+2", result.getExpression());
        assertEquals(4F, result.getResult());
    }

    @Test
    public void shouldGetAllCalculations() {
        var calculations = List.of(
                new Calculation(1L, "2+2", 4F, LocalDateTime.of(2024, 12, 9, 17, 30)),
                new Calculation(2L, "3*3", 9F, LocalDateTime.of(2024, 12, 9, 18, 30))
        );

        when(calculationRepository.findAll()).thenReturn(calculations);

        var result = calculationService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void shouldClearCalculations() {
        calculationService.clear();
        verify(calculationRepository, times(1)).deleteAll();
    }

}
