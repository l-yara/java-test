package com.industrialLogicTest.domain;

import org.javamoney.moneta.Money;

import lombok.Value;

@Value
public class Product implements Comparable<Product> {
    //a "standard" set. We do NOT use Enum here as we want this set to be expandable at runtime
    public static final Product SOUP = new Product("soup", "tin", 0.65);
    public static final Product BREAD = new Product("bread", "loaf", 0.80);
    public static final Product MILK = new Product("milk", "bottle", 1.30);
    public static final Product APPLES = new Product("apples", "single", 0.1);

    public static final String DEFAULT_CURRENCY = "GBP";

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
