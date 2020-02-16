package com.industrialLogicTest.domain;

import static com.industrialLogicTest.domain.Basket.amount;

import org.javamoney.moneta.Money;

import lombok.Value;

@Value
public class Product implements Comparable<Product> {
    private final String name;
    private final String unit;
    private final Money price;

    public Product(String name, String unit, double price) {
        this.name = name;
        this.unit = unit;
        this.price = amount(price);
    }

    @Override
    public int compareTo(Product o) {
        return this.getName().compareTo(o.getName());
    }
}
