package com.industrialLogic.domain;

import org.javamoney.moneta.Money;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
public class Product implements Comparable<Product>{
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
    public String toString() {
        return "Product{" +
                "'" + name + '\'' +
                " at " + price.getNumber() +
                " per " + unit + '}';
    }

    @Override
    public int compareTo(Product o) {
        return this.getName().compareTo(o.getName());
    }
}
