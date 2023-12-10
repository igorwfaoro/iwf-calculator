package com.iwf.calculator.model.dto.view;

import com.iwf.calculator.model.entity.Calculation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalculationViewDto {
    private Long id;
    private String expression;
    private Float result;

    public static CalculationViewDto fromEntity(Calculation entity) {
        var viewDto = new CalculationViewDto();
        viewDto.id = entity.getId();
        viewDto.expression = entity.getExpression();

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ROOT);
        symbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("#.##", symbols);

        viewDto.result = Float.parseFloat(df.format(entity.getResult()));

        return viewDto;
    }
}
