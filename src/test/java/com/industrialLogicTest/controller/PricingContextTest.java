package com.industrialLogicTest.controller;

import static com.industrialLogicTest.domain.Product.DEFAULT_CURRENCY;
import static com.industrialLogicTest.domain.Product.ZERO;
import static com.industrialLogicTest.domain.TestData.SOUP;
import static com.industrialLogicTest.domain.TestData.BREAD;
import static com.industrialLogicTest.domain.TestData.MILK;
import static org.junit.Assert.*;

import org.javamoney.moneta.Money;
import org.junit.Test;

import com.industrialLogicTest.domain.Basket;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class PricingContextTest {
    public static final Basket BASKET_1 = new Basket().addItems(SOUP, 5);
    Basket BASKET_2 = BASKET_1.addItem(SOUP).addItems(BREAD, 2).removeItem(MILK);

    @Test
    public void testBasket_1() {
        PricingContext ctx = new PricingContext(BASKET_1);
        assertEquals(5, ctx.amountOf("soup"));
        assertEquals(SOUP.getPrice(), ctx.priceFor("sOUp"));
        assertEquals(Money.of(3.25, DEFAULT_CURRENCY), ctx.totalPriceFor("souP"));
        assertEquals(0, ctx.amountOf("milk"));
        assertEquals(ZERO, ctx.priceFor("milk"));
        assertEquals(ZERO, ctx.totalPriceFor("milk"));
        //wrong product name:
        assertEquals(0, ctx.amountOf("oops"));
        assertEquals(ZERO, ctx.priceFor("oops"));
        assertEquals(ZERO, ctx.totalPriceFor("oops"));
    }

    @Test
    public void testBasket_2() {
        PricingContext ctx = new PricingContext(BASKET_2);
        log.error("{}", ctx);
        assertEquals(6, ctx.amountOf("SOUP"));
        assertEquals(SOUP.getPrice(), ctx.priceFor("sOUp"));
        assertEquals(Money.of(3.9, DEFAULT_CURRENCY), ctx.totalPriceFor("souP"));
        assertEquals(0, ctx.amountOf("milk"));
        //no milk in basket - no priced per item, sorry
        assertEquals(ZERO, ctx.priceFor("milk"));
        assertEquals(ZERO, ctx.totalPriceFor("milk"));

        assertEquals(2, ctx.amountOf("bread"));
        assertEquals(BREAD.getPrice(), ctx.priceFor("bread"));
        assertEquals(Money.of(1.6, DEFAULT_CURRENCY), ctx.totalPriceFor("bread"));
    }

    @Test
    public void testOnEmptyBasket() {
        PricingContext ctx = new PricingContext(new Basket());
        //no failures - only zeros
        assertEquals(0, ctx.amountOf("SOUP"));
        assertEquals(ZERO, ctx.priceFor("sOUp"));
        assertEquals(ZERO, ctx.totalPriceFor("souP"));
        assertEquals(0, ctx.amountOf("milk"));
    }
}