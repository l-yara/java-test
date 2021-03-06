package com.industrialLogicTest.domain;


import static com.industrialLogicTest.domain.Product.BREAD;
import static com.industrialLogicTest.domain.Product.MILK;
import static com.industrialLogicTest.domain.Product.SOUP;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

public class BasketTest {

    @Test
    public void testAddRemove() {
        Basket basket1 = new Basket().addItems(SOUP, 5);
        assertEquals(ImmutableMap.of(SOUP, 5), basket1.getContent());

        Basket basket2 = basket1.addItems(SOUP, 1).addItems(BREAD, 2).removeItems(MILK, 1);
        assertEquals(ImmutableMap.of(SOUP, 6, BREAD, 2), basket2.getContent());

        Basket basket3 = basket2.removeItems(SOUP, 3).removeItems(BREAD, 4);
        assertEquals(ImmutableMap.of(SOUP, 3), basket3.getContent());
    }
}