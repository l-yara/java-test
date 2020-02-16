package com.industrialLogicTest.controller;

import static com.industrialLogicTest.domain.TestData.BREAD;
import static com.industrialLogicTest.domain.TestData.MILK;
import static com.industrialLogicTest.domain.TestData.SOUP;
import static com.industrialLogicTest.domain.TestData.TODAY;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.industrialLogicTest.domain.Basket;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class PricingContextTest {
    public static final double TOLERANCE = 0.00000001;
    public static final Basket BASKET_1 = new Basket(TODAY).addItems(SOUP, 5);
    Basket BASKET_2 = BASKET_1.addItem(SOUP).addItems(BREAD, 2).removeItem(MILK);

    @Test
    public void testUtils() {
        PricingContext ctx = new PricingContext(BASKET_1);
        assertEquals(TODAY, ctx.getDate());
    }

    @Test
    public void testBasket_1() {
        PricingContext ctx = new PricingContext(BASKET_1);
        assertEquals(5, ctx.amountOf("soup"));
        assertDouble(0.65, ctx.priceFor("sOUp"));
        assertDouble(3.25, ctx.totalPriceFor("souP"));

        assertEquals(0, ctx.amountOf("milk"));
        assertZero(ctx.priceFor("milk"));
        assertZero(ctx.totalPriceFor("milk"));
        //wrong product name:
        assertEquals(0, ctx.amountOf("oops"));
        assertZero(ctx.priceFor("oops"));
        assertZero(ctx.totalPriceFor("oops"));

        assertDouble(3.25, ctx.totalPrice());
    }

    @Test
    public void testBasket_2() {
        PricingContext ctx = new PricingContext(BASKET_2);
        log.error("{}", ctx);
        assertEquals(6, ctx.amountOf("SOUP"));
        assertDouble(0.65, ctx.priceFor("sOUp"));
        assertDouble(3.9, ctx.totalPriceFor("souP"));
        assertEquals(0, ctx.amountOf("milk"));
        //no milk in basket - no priced per item, sorry
        assertZero(ctx.priceFor("milk"));
        assertZero(ctx.totalPriceFor("milk"));

        assertEquals(2, ctx.amountOf("bread"));
        assertDouble(0.8, ctx.priceFor("bread"));
        assertDouble(1.6, ctx.totalPriceFor("bread"));

        assertDouble(5.5, ctx.totalPrice());
    }

    @Test
    public void testOnEmptyBasket() {
        PricingContext ctx = new PricingContext(new Basket());
        //no failures - only zeros
        assertEquals(0, ctx.amountOf("SOUP"));
        assertZero(ctx.priceFor("sOUp"));
        assertZero(ctx.totalPriceFor("souP"));
        assertEquals(0, ctx.amountOf("milk"));
        assertZero(ctx.totalPrice());
    }

    public static void assertDouble(double expected, double actual) {
        assertEquals(expected, actual, TOLERANCE);
    }

    public static void assertZero(double actual) {
        assertDouble(0.0, actual);
    }
}