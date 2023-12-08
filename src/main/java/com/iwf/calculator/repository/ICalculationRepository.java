package com.iwf.calculator.repository;

import com.iwf.calculator.model.entity.Calculation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ICalculationRepository extends JpaRepository<Calculation, Long> {
    Optional<Calculation> findOneByExpression(String expression);
}
