package com.industrialLogicTest.domain;

import org.javamoney.moneta.Money;

import lombok.Value;

@Value
public class Product implements Comparable<Product> {
    public static final String DEFAULT_CURRENCY = "GBP";
    public static final Money ZERO = Money.of(0, DEFAULT_CURRENCY);

    private final String name;
    private final String unit;
    private final Money price;

    public Product(String name, String unit, double price) {
        this.name = name;
        this.unit = unit;
        this.price = Money.of(price, DEFAULT_CURRENCY);
    }

    @Override
    public int compareTo(Product o) {
        return this.getName().compareTo(o.getName());
    }
}
