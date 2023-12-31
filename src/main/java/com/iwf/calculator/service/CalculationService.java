package com.iwf.calculator.service;

import com.iwf.calculator.exception.ExpressionException;
import com.iwf.calculator.model.dto.input.CalculationInputDto;
import com.iwf.calculator.model.dto.view.CalculationViewDto;
import com.iwf.calculator.model.entity.Calculation;
import com.iwf.calculator.repository.ICalculationRepository;
import com.iwf.calculator.service.interfaces.ICalculationService;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalculationService implements ICalculationService {

    @Autowired
    private ICalculationRepository calculationRepository;

    public CalculationViewDto calculate(CalculationInputDto input) throws ExpressionException {
        var existentCalculation = calculationRepository.findOneByExpression(input.getExpression());

        if (existentCalculation.isPresent()) {
            return CalculationViewDto.fromEntity(existentCalculation.get());
        }

        float result;
        try {
            result = (float) new ExpressionBuilder(input.getExpression())
                    .build()
                    .evaluate();
        } catch (ArithmeticException e) {
            throw new ExpressionException(e.getMessage());
        }

        var newCalculation = Calculation.create(input.getExpression(), result);
        calculationRepository.save(newCalculation);

        return CalculationViewDto.fromEntity(newCalculation);
    }

    public List<CalculationViewDto> getAll() {
        var calculations = calculationRepository.findAll();
        return calculations.stream().map(CalculationViewDto::fromEntity).toList();
    }

    public void clear() {
        calculationRepository.deleteAll();
    }
}
