package com.industrialLogicTest.controller;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;

import org.javamoney.moneta.Money;

import com.industrialLogicTest.domain.Basket;
import com.industrialLogicTest.domain.Promotion;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class PriceCalculator {

    private Collection<Promotion> promotions;

    public PriceCalculator(Promotion... promotions) {
        this(Arrays.asList(promotions));
    }

    public PriceCalculator(Collection<Promotion> promotions) {
        this.promotions = promotions;
    }

    public double calculatePrice(Basket basket) {
        double totalPrice = basket.getContent().entrySet().stream()
                .map(e -> e.getKey().getPrice().multiply(e.getValue()))
                .reduce(Money::add)
                .map(m -> m.getNumber().doubleValue()).orElse(0.0);

        double allDiscounts = promotions.stream()
                .mapToDouble(p -> this.applyPromotion(basket, p)).sum();

        return totalPrice - allDiscounts;
    }

    //apply single promotion to the basket - visible for testing
    double applyPromotion(Basket basket, Promotion promotion) {
        PricingContext context = new PricingContext(basket);
        LocalDate contextDate = context.getDate();
        if (!promotion.getValidFrom().isAfter(contextDate) &&
                !promotion.getValidTo().isBefore(contextDate)) {
            //if dates OK
            try {
                Double value = promotion.getDiscountExp().getValue(context, Double.class);
                return (value == null || value.isNaN()) ? 0.0 : value;
            } catch (Exception ignore) {
                log.error("Unable to apply '" + promotion.getDiscountExp().getExpressionString() + "' to " + basket);
            }
        }
        return 0.0;
    }

    /**
     * Calculate exact last day of the given month
     *
     * @param today  a given date
     * @param months 0 - this month, 1 - next month, 2 - month after etc
     * @return last day of the month
     */
    public static LocalDate endOfMonth(LocalDate today, int months) {
        return today.withDayOfMonth(1).plusMonths(months + 1).minusDays(1);
    }
}
