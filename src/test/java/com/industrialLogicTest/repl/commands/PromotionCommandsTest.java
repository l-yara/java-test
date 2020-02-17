package com.industrialLogicTest.repl.commands;

import static com.industrialLogicTest.domain.Product.APPLES;
import static com.industrialLogicTest.domain.Product.BREAD;
import static com.industrialLogicTest.domain.Product.MILK;
import static com.industrialLogicTest.domain.Product.SOUP;
import static com.industrialLogicTest.repl.commands.PromotionCommands.ADD_PROMOTION;
import static com.industrialLogicTest.repl.commands.PromotionCommands.REMOVE_PROMOTION;
import static com.industrialLogicTest.repl.commands.ReplCommand.OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.industrialLogicTest.domain.Product;
import com.industrialLogicTest.domain.Promotion;
import com.industrialLogicTest.repl.ReplSessionState;

public class PromotionCommandsTest {
    @Mock
    private ReplSessionState session;
    @Mock
    private List<Promotion> promotions;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(session.getPromotions()).thenReturn(promotions);
        when(promotions.size()).thenReturn(3);
    }

    @Test
    public void testRemovePromotion_HappyPath() throws ParsingException {
        assertTrue(REMOVE_PROMOTION.apply("1", session).contains("Removed promotion"));
        verify(promotions).remove(anyInt());
    }

    @Test
    public void testRemovePromotion_malformat() throws ParsingException {
        String result = REMOVE_PROMOTION.apply("oops", session);
        //no changes in promotion list
        verifyNoInteractions(promotions);
        assertTrue(result.contains("oops"));
    }

    @Test
    public void testRemovePromotion_outOfRange() throws ParsingException {
        String result = REMOVE_PROMOTION.apply("99", session);
        //no changes in promotion list
        verify(promotions, never()).remove(anyInt());
        assertTrue(result.contains("range"));
    }

    @Test
    public void testAddPromotion_HappyPath() throws ParsingException {
        String result = ADD_PROMOTION.apply("5% discount on basket > £20.0|2020-02-01|2020-03-15|totalPrice() > 20.0 ? totalPrice() * 0.05 : 0", session);
        assertTrue(result.contains("added"));
        verify(promotions).add(any(Promotion.class));
    }

    @Test
    public void testAddPromotion_Malformed1() throws ParsingException {
        String result = ADD_PROMOTION.apply("5% discount on basket > £20.0|2020-02-01|20200315|totalPrice() > 20.0 ? totalPrice() * 0.05 : 0", session);
        assertTrue(result.contains("Unable to parse"));
        verify(promotions, never()).add(any(Promotion.class));
    }

}