package com.industrialLogicTest.domain;

import java.time.LocalDate;

//utility set of the test data
public class TestData {

    public static final LocalDate TODAY = LocalDate.of(2010, 2, 18);
    public static final LocalDate YESTERDAY = TODAY.minusDays(1);
    public static final LocalDate PAST_3 = TODAY.minusDays(3);
    public static final LocalDate FUTURE_3 = TODAY.plusDays(3);
    public static final LocalDate FUTURE_5 = TODAY.plusDays(5);

    public static final Product SOUP = new Product("soup", "tin", 0.65);
    public static final Product BREAD = new Product("bread", "loaf", 0.80);
    public static final Product MILK = new Product("milk", "bottle", 1.30);
    public static final Product APPLES = new Product("apples", "single", 0.1);

    public static final Basket TEST_1 = new Basket(TODAY).addItems(SOUP, 3).addItems(BREAD, 2);
    public static final Basket TEST_2 = new Basket(TODAY).addItems(APPLES, 6).addItem(MILK);
    public static final Basket TEST_3 = TEST_2.withDate(FUTURE_5);
    public static final Basket TEST_4 = new Basket(FUTURE_5).addItems(APPLES, 3).addItems(SOUP, 2).addItem(BREAD);
}
