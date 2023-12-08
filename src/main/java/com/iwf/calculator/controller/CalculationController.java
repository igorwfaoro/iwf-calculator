package com.iwf.calculator.controller;

import com.iwf.calculator.constant.ApiConstants;
import com.iwf.calculator.model.dto.input.CalculationInputDto;
import com.iwf.calculator.model.dto.view.CalculationViewDto;
import com.iwf.calculator.service.CalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/" + ApiConstants.V1 + "/" + ApiConstants.CONTROLLER_CALCULATIONS)
public class CalculationController {

    @Autowired
    private CalculationService calculationService;

    @PostMapping(ApiConstants.RESOURCE_CALCULATE)
    public CalculationViewDto calculate(@RequestBody CalculationInputDto input) {
        return calculationService.calculate(input);
    }

    @GetMapping
    public List<CalculationViewDto> getAll() {
        return calculationService.getAll();
    }
}
