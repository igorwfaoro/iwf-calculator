package com.iwf.calculator.service.interfaces;

import com.iwf.calculator.model.dto.input.CalculationInputDto;
import com.iwf.calculator.model.dto.view.CalculationViewDto;

import java.util.List;

public interface ICalculationService {
    CalculationViewDto calculate(CalculationInputDto input);
    List<CalculationViewDto> getAll();
    void clear();
}
