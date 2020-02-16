package com.industrialLogicTest.domain;


import static com.industrialLogicTest.domain.TestData.SOUP;
import static com.industrialLogicTest.domain.TestData.BREAD;
import static com.industrialLogicTest.domain.TestData.MILK;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class BasketTest {

    @Test
    public void testAddRemove() {
        Basket basket1 = new Basket().addItems(SOUP, 5);
        assertEquals(ImmutableMap.of(SOUP, 5), basket1.getContent());

        Basket basket2 = basket1.addItem(SOUP).addItems(BREAD, 2).removeItem(MILK);
        assertEquals(ImmutableMap.of(SOUP, 6, BREAD, 2), basket2.getContent());

        Basket basket3 = basket2.removeItems(SOUP, 3).removeItems(BREAD, 4);
        assertEquals(ImmutableMap.of(SOUP, 3), basket3.getContent());

        log.error("{}", basket1);
        log.error("{}", basket2);
        log.error("{}", basket3);
    }
}