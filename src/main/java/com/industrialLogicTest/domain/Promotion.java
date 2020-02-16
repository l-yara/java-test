package com.industrialLogicTest.domain;

import java.time.LocalDate;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import lombok.Value;
import lombok.extern.log4j.Log4j2;

/**
 * A single promotion, like "Buy 2 tins of soup and get a loaf of bread half price"
 */
@Value
@Log4j2
public class Promotion {
    public static final ExpressionParser PARSER = new SpelExpressionParser();

    private final String name;
    private final LocalDate validFrom;
    private final LocalDate validTo;
    private final Expression discountExp;

    public Promotion(String name, LocalDate validFrom, LocalDate validTo, String discountExp) {
        this.name = name;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.discountExp = PARSER.parseExpression(discountExp);
    }
}
