package com.iwf.calculator.controller;

import com.iwf.calculator.constant.ApiConstants;
import com.iwf.calculator.exception.ExpressionException;
import com.iwf.calculator.model.dto.input.CalculationInputDto;
import com.iwf.calculator.model.dto.view.CalculationViewDto;
import com.iwf.calculator.service.interfaces.ICalculationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiConstants.CONTROLLER_CALCULATIONS_ROUTE)
@Tag(name = "Calculations")
public class CalculationController {

    @Autowired
    private ICalculationService calculationService;

    @PostMapping(ApiConstants.RESOURCE_CALCULATIONS_CALCULATE)
    public CalculationViewDto calculate(@RequestBody CalculationInputDto input) throws ExpressionException {
        return calculationService.calculate(input);
    }

    @GetMapping
    public List<CalculationViewDto> getAll() {
        return calculationService.getAll();
    }

    @DeleteMapping(ApiConstants.RESOURCE_CALCULATIONS_CLEAR)
    public void clear() {
        calculationService.clear();
    }
}
