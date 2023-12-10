package com.iwf.calculator.model.dto.view;

import com.iwf.calculator.model.entity.Calculation;
import lombok.Data;

@Data
public class CalculationViewDto {
    private Long id;
    private String expression;
    private Float result;

    public static CalculationViewDto fromEntity(Calculation entity) {
        var viewDto = new CalculationViewDto();
        viewDto.id = entity.getId();
        viewDto.expression = entity.getExpression();
        viewDto.result = entity.getResult();
        return viewDto;
    }
}
