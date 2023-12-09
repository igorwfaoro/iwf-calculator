package com.iwf.calculator.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import java.time.LocalDateTime;

@Entity(name = "Calculations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Calculation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column()
    private Long id;

    @Column(nullable = false)
    private String expression;

    @Column(nullable = false)
    private Float result;

    @Column(nullable = false)
    @Generated(GenerationTime.ALWAYS)
    private LocalDateTime createdAt;

    public static Calculation create(String content, Float result) {
        var operation = new Calculation();
        operation.expression = content;
        operation.result = result;
        return operation;
    }
}
