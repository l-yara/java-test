package com.industrialLogicTest.domain;

import java.time.LocalDate;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import lombok.Value;

/**
 * A single promotion, like "Buy 2 tins of soup and get a loaf of bread half price"
 */
@Value
public class Promotion {
    public static final ExpressionParser PARSER = new SpelExpressionParser();

    /**
     * is NOT used for id - rather for description
     */
    private final String name;
    private final LocalDate validFrom;
    private final LocalDate validTo;
    /**
     * A SpEL (https://docs.spring.io/spring/docs/4.3.10.RELEASE/spring-framework-reference/html/expressions.html)
     * expression calculating the discount value based on the {@linkplain com.industrialLogicTest.controller.PricingContext} (actually, for the {@linkplain com.industrialLogicTest.domain.Basket}). If not applicable, return 0.0
     */
    private final Expression discountExp;

    public Promotion(String name, LocalDate validFrom, LocalDate validTo, String discountExp) {
        this.name = name;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.discountExp = PARSER.parseExpression(discountExp);
    }

    @Override
    public String toString() {
        return "Promotion{" +
                "name='" + name + '\'' +
                ", valid from " + validFrom +
                " to " + validTo +
                ", discountExp='" + discountExp.getExpressionString() +
                "'}";
    }
}
