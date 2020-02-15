package com.industrialLogic.domain;


import static com.industrialLogic.domain.TestData.PRODUCT_1;
import static com.industrialLogic.domain.TestData.PRODUCT_2;
import static com.industrialLogic.domain.TestData.PRODUCT_3;
import static java.util.Collections.singletonMap;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class BasketTest {

    @Test
    public void testAddRemove() {
        Basket basket1 = new Basket().addItems(PRODUCT_1, 5);
        assertEquals(ImmutableMap.of(PRODUCT_1, 5), basket1.getContent());
        assertEquals(5, basket1.amountOf("p1"));
        assertEquals(0, basket1.amountOf("p2"));
        assertEquals(0, basket1.amountOf("oops, wrong product name!"));

        Basket basket2 = basket1.addItem(PRODUCT_1).addItems(PRODUCT_2, 2).removeItem(PRODUCT_3);
        assertEquals(ImmutableMap.of(PRODUCT_1, 6, PRODUCT_2, 2), basket2.getContent());
        assertEquals(6, basket2.amountOf("p1"));
        assertEquals(2, basket2.amountOf("p2"));
        assertEquals(0, basket2.amountOf("p3"));

        Basket basket3 = basket2.removeItems(PRODUCT_1, 3).removeItems(PRODUCT_2, 4);
        assertEquals(ImmutableMap.of(PRODUCT_1, 3), basket3.getContent());

        log.error("{}", basket1);
        log.error("{}", basket2);
        log.error("{}", basket3);
    }
}