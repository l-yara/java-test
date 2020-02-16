package com.industrialLogicTest.controller;

import static com.industrialLogicTest.controller.PriceCalculator.endOfMonth;
import static com.industrialLogicTest.controller.PricingContextTest.assertDouble;
import static com.industrialLogicTest.controller.PricingContextTest.assertZero;
import static com.industrialLogicTest.domain.TestData.FUTURE_3;
import static com.industrialLogicTest.domain.TestData.FUTURE_5;
import static com.industrialLogicTest.domain.TestData.PAST_3;
import static com.industrialLogicTest.domain.TestData.TEST_1;
import static com.industrialLogicTest.domain.TestData.TEST_2;
import static com.industrialLogicTest.domain.TestData.TEST_3;
import static com.industrialLogicTest.domain.TestData.TEST_4;
import static com.industrialLogicTest.domain.TestData.TODAY;
import static com.industrialLogicTest.domain.TestData.YESTERDAY;
import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Test;

import com.industrialLogicTest.domain.Basket;
import com.industrialLogicTest.domain.Promotion;

public class PriceCalculatorTest {
    //a "standard" set of
    public static final PriceCalculator NO_PROMS_CALCULATOR = new PriceCalculator();
    public static final PriceCalculator STANDARD_CALCULATOR = new PriceCalculator(
            new Promotion("2 tins -> bread 1/2 price", YESTERDAY, YESTERDAY.plusDays(7),
                    //if you have two or more tins of soup, get 0.5 * price of bread discount
                    "amountOf('soup') >= 2 ? priceFor('bread') * 0.5 : 0"),
            //price for all apples -10% (no apples - no discount)
            new Promotion("apples 10% off", FUTURE_3, endOfMonth(TODAY, 1),
                    "totalPriceFor('apples') * 0.1")
    );

    @Test
    public void testApplySinglePromotion_Dates() {
        //these two will reduce by constants if applicable
        final Promotion past = new Promotion("stale", PAST_3, YESTERDAY, "999.01");
        final Promotion future = new Promotion("in future", FUTURE_3, FUTURE_5, "55.02");

        //TEST_1 is for "today"
        assertZero(NO_PROMS_CALCULATOR.applyPromotion(TEST_1, past));
        assertZero(NO_PROMS_CALCULATOR.applyPromotion(TEST_1, future));
        //yesterday: PROMOTION_IN_PAST is applicable, PROMOTION_IN_FUTURE is not
        Basket yesterdayBasket = TEST_1.withDate(YESTERDAY);
        assertDouble(999.01, NO_PROMS_CALCULATOR.applyPromotion(yesterdayBasket, past));
        assertZero(NO_PROMS_CALCULATOR.applyPromotion(yesterdayBasket, future));

        //future: PROMOTION_IN_PAST is not applicable, PROMOTION_IN_FUTURE is
        Basket futureBasket = TEST_1.withDate(FUTURE_3);
        assertZero(NO_PROMS_CALCULATOR.applyPromotion(futureBasket, past));
        assertDouble(55.02, NO_PROMS_CALCULATOR.applyPromotion(futureBasket, future));
    }

    @Test
    public void testApplySinglePromotion_BrokenExpression() {
        //these two will reduce by constants if applicable
        final Promotion broken = new Promotion("stale", PAST_3, FUTURE_5, "oops");
        final Promotion ok = new Promotion("soup_minus_20%", PAST_3, FUTURE_5, "totalPriceFor('soup') * 0.2");

        //TEST_1 is for "today" so should go to the calculation
        assertZero(NO_PROMS_CALCULATOR.applyPromotion(TEST_1, broken));
        //3 tins of soup = 3 * 0.65 = £1.95; 20% of £1.95 is £0.39
        assertDouble(0.39, NO_PROMS_CALCULATOR.applyPromotion(TEST_1, ok));
    }

    @Test
    public void testStandardValues() {
        //as in spec
        assertDouble(3.15, STANDARD_CALCULATOR.calculatePrice(TEST_1));
        assertDouble(1.90, STANDARD_CALCULATOR.calculatePrice(TEST_2));
        assertDouble(1.84, STANDARD_CALCULATOR.calculatePrice(TEST_3));
        assertDouble(1.97, STANDARD_CALCULATOR.calculatePrice(TEST_4));
        //empty basket OK
        assertZero(STANDARD_CALCULATOR.calculatePrice(new Basket(TODAY)));
    }

    @Test
    public void testNoPromsCalculator() {
        //no proms - no problems, just prices higher
        assertDouble(3.55, NO_PROMS_CALCULATOR.calculatePrice(TEST_1));
        assertDouble(1.90, NO_PROMS_CALCULATOR.calculatePrice(TEST_2));
        assertDouble(1.90, NO_PROMS_CALCULATOR.calculatePrice(TEST_3));
        assertDouble(2.40, NO_PROMS_CALCULATOR.calculatePrice(TEST_4));
        //empty basket OK
        assertZero(STANDARD_CALCULATOR.calculatePrice(new Basket(TODAY)));
    }

    @Test
    public void testEndOfMonthCalculation() {
        //mid month, non-leap year last days of jan, feb, mar, apr
        assertDays(LocalDate.of(2003, 1, 10), 31, 28, 31, 30);
        //mid month, leap year last days of jan, feb, mar, apr
        assertDays(LocalDate.of(2004, 1, 10), 31, 29, 31, 30);
        //beginning of month, non-leap year last days of jan, feb, mar, apr
        assertDays(LocalDate.of(2003, 1, 1), 31, 28, 31, 30);
        //beginning of month, leap year last days of jan, feb, mar, apr
        assertDays(LocalDate.of(2004, 1, 1), 31, 29, 31, 30);
        //end of month, non-leap year last days of jan, feb, mar, apr
        assertDays(LocalDate.of(2003, 1, 31), 31, 28, 31, 30);
        //end of month, leap year last days of jan, feb, mar, apr
        assertDays(LocalDate.of(2004, 1, 31), 31, 29, 31, 30);
    }

    private void assertDays(LocalDate today, int... days) {
        int year = today.getYear();
        for (int i = 0; i < days.length; i++) {
            assertEquals("month from now: " + i,
                    LocalDate.of(year, i + 1, days[i]), endOfMonth(today, i));
        }
    }

}