package com.iwf.calculator.controller;

import com.iwf.calculator.constant.ApiConstants;
import com.iwf.calculator.exception.ExpressionException;
import com.iwf.calculator.model.dto.input.CalculationInputDto;
import com.iwf.calculator.model.dto.view.CalculationViewDto;
import com.iwf.calculator.service.interfaces.ICalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/" + ApiConstants.V1 + "/" + ApiConstants.CONTROLLER_CALCULATIONS)
public class CalculationController {

    @Autowired
    private ICalculationService calculationService;

    @PostMapping(ApiConstants.RESOURCE_CALCULATE)
    public CalculationViewDto calculate(@RequestBody CalculationInputDto input) throws ExpressionException {
        return calculationService.calculate(input);
    }

    @GetMapping
    public List<CalculationViewDto> getAll() {
        return calculationService.getAll();
    }

    @DeleteMapping(ApiConstants.RESOURCE_CLEAR)
    public void clear() {
        calculationService.clear();
    }
}
