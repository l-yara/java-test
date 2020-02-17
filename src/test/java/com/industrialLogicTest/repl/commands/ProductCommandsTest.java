package com.industrialLogicTest.repl.commands;

import static com.industrialLogicTest.domain.Product.APPLES;
import static com.industrialLogicTest.domain.Product.BREAD;
import static com.industrialLogicTest.domain.Product.MILK;
import static com.industrialLogicTest.domain.Product.SOUP;
import static com.industrialLogicTest.domain.TestData.TEST_1;
import static com.industrialLogicTest.repl.commands.ProductCommands.ADD_PRODUCT;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.industrialLogicTest.domain.Basket;
import com.industrialLogicTest.domain.Product;
import com.industrialLogicTest.repl.ReplSessionState;

public class ProductCommandsTest {

    @Mock
    private ReplSessionState session;
    private ArgumentCaptor<Basket> basketArgumentCaptor;

    private List<Product> products;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        products = new ArrayList<>(Arrays.asList(SOUP, BREAD, MILK, APPLES));

        when(session.getBasket()).thenReturn(TEST_1);
        when(session.getProducts()).thenReturn(products);
        when(session.findProduct("soup")).thenReturn(SOUP);
        when(session.findProduct("apples")).thenReturn(APPLES);
        when(session.findProduct("bread")).thenReturn(BREAD);
        when(session.findProduct("milk")).thenReturn(MILK);

        basketArgumentCaptor = ArgumentCaptor.forClass(Basket.class);
    }

    @Test
    public void testAddNew_HappyPath() throws ParsingException {
        int sizeBefore = products.size();
        String result = ADD_PRODUCT.apply("eggs: 2.20 : dozen", session);
        assertTrue(result, result.contains("added"));
        assertTrue(products.size() == sizeBefore + 1);
        Product added = products.get(sizeBefore);
        assertEquals(new Product("eggs","dozen", 2.20), added);
    }

    @Test
    public void testReplace_HappyPath() throws ParsingException {
        int sizeBefore = products.size();
//        also test rounding
        String result = ADD_PRODUCT.apply("bread: 9.5890 : item", session);
        assertTrue(result, result.contains("replaced"));
        assertTrue(products.size() == sizeBefore);
        Product replaced = products.get(sizeBefore - 1);
        assertEquals(new Product("bread","item", 9.59), replaced);
    }
}