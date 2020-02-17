package com.industrialLogicTest.repl.commands;

import static com.industrialLogicTest.domain.Product.APPLES;
import static com.industrialLogicTest.domain.Product.BREAD;
import static com.industrialLogicTest.domain.Product.MILK;
import static com.industrialLogicTest.domain.Product.SOUP;
import static com.industrialLogicTest.domain.TestData.TEST_1;
import static com.industrialLogicTest.repl.commands.ReplCommand.OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.ImmutableMap;
import com.industrialLogicTest.domain.Basket;
import com.industrialLogicTest.domain.Product;
import com.industrialLogicTest.repl.ReplSessionState;

public class BasketCommandsTest {
    private final static List<Product> PRODUCTS = Arrays.asList(SOUP, BREAD, MILK, APPLES);

    @Mock
    private ReplSessionState session;
    private ArgumentCaptor<Basket> basketArgumentCaptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(session.getBasket()).thenReturn(TEST_1);
        when(session.getProducts()).thenReturn(PRODUCTS);
        when(session.findProduct("soup")).thenReturn(SOUP);
        when(session.findProduct("apples")).thenReturn(APPLES);
        when(session.findProduct("bread")).thenReturn(BREAD);
        when(session.findProduct("milk")).thenReturn(MILK);

        basketArgumentCaptor = ArgumentCaptor.forClass(Basket.class);
    }

    @Test
    public void testClearCommand() throws ParsingException {
        assertEquals(OK, BasketCommands.CLEAR_BASKET.apply("ignored", session));
        verify(session).updateBasket(basketArgumentCaptor.capture());

        Basket newBasket = basketArgumentCaptor.getValue();
        assertTrue(newBasket.getContent().isEmpty());
        assertEquals(TEST_1.getDate(), newBasket.getDate());
    }

    @Test
    public void testAddCommand_HappyRun() throws ParsingException {
        //a bit fizzy formatting should be OK still
        assertEquals(OK, BasketCommands.ADD_ITEMS.apply("soup:2 , milk : 4", session));
        verify(session).updateBasket(basketArgumentCaptor.capture());

        Basket newBasket = basketArgumentCaptor.getValue();
        assertEquals(TEST_1.getDate(), newBasket.getDate());
        assertEquals(ImmutableMap.of(SOUP, 5, BREAD, 2, MILK, 4), newBasket.getContent());
    }

    @Test
    public void testAddCommand_BrokenProductName() {
        try {
            BasketCommands.ADD_ITEMS.apply("soup:2,oops: 4", session);
            fail("Expecting ParsingException");
        } catch (ParsingException e) {
            assertTrue(e.getMessage().contains("oops"));
            verify(session, never()).updateBasket(any(Basket.class));
        }
    }

    @Test
    public void testAddCommand_BrokenAmount() {
        try {
            BasketCommands.ADD_ITEMS.apply("soup:2,milk:2.5", session);
            fail("Expecting ParsingException");
        } catch (ParsingException e) {
            assertTrue(e.getMessage().contains("2.5"));
            verify(session, never()).updateBasket(any(Basket.class));
        }
    }

    @Test
    public void testRemoveCommand_HappyRun() throws ParsingException {
        //a bit fizzy formatting should be OK still
        assertEquals(OK, BasketCommands.REMOVE_ITEMS.apply("soup:2 , bread:10", session));
        verify(session).updateBasket(basketArgumentCaptor.capture());

        Basket newBasket = basketArgumentCaptor.getValue();
        assertEquals(TEST_1.getDate(), newBasket.getDate());
        assertEquals(ImmutableMap.of(SOUP, 1), newBasket.getContent());
    }

    @Test
    public void testNewDate_HappyRun() throws ParsingException {
        //before - check not equal to what we'll set up
        assertEquals(LocalDate.of(2020, 2, 18), session.getBasket().getDate());
        assertEquals(OK, BasketCommands.NEW_DATE.apply("2017-09-22", session));
        verify(session).updateBasket(basketArgumentCaptor.capture());

        Basket newBasket = basketArgumentCaptor.getValue();
        assertEquals(LocalDate.of(2017, 9, 22), newBasket.getDate());
        //content did not change
        assertEquals(TEST_1.getContent(), newBasket.getContent());
    }

    @Test
    public void testNewDate_BrokenAmount() {
        try {
            BasketCommands.NEW_DATE.apply("oops", session);
            fail("Expecting ParsingException");
        } catch (ParsingException e) {
            assertTrue(e.getMessage().contains("oops"));
            verify(session, never()).updateBasket(any(Basket.class));
        }
    }


}