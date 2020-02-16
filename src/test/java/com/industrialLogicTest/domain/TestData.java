package com.industrialLogicTest.domain;

import static com.industrialLogicTest.domain.Product.APPLES;
import static com.industrialLogicTest.domain.Product.BREAD;
import static com.industrialLogicTest.domain.Product.MILK;
import static com.industrialLogicTest.domain.Product.SOUP;

import java.time.LocalDate;

//utility set of the test data
public class TestData {

    public static final LocalDate TODAY = LocalDate.of(2010, 2, 18);
    public static final LocalDate YESTERDAY = TODAY.minusDays(1);
    public static final LocalDate PAST_3 = TODAY.minusDays(3);
    public static final LocalDate FUTURE_3 = TODAY.plusDays(3);
    public static final LocalDate FUTURE_5 = TODAY.plusDays(5);

    public static final Basket TEST_1 = new Basket(TODAY).addItems(SOUP, 3).addItems(BREAD, 2);
    public static final Basket TEST_2 = new Basket(TODAY).addItems(APPLES, 6).addItems(MILK, 1);
    public static final Basket TEST_3 = TEST_2.withDate(FUTURE_5);
    public static final Basket TEST_4 = new Basket(FUTURE_5).addItems(APPLES, 3).addItems(SOUP, 2).addItems(BREAD, 1);
}
