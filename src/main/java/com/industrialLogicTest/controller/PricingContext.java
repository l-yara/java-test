package com.industrialLogicTest.controller;


import java.time.LocalDate;
import java.util.Map;

import org.javamoney.moneta.Money;

import com.industrialLogicTest.domain.Basket;
import com.industrialLogicTest.domain.Product;

import lombok.Value;


/**
 * A convenience class for {@linkplain PriceCalculator}. Should be an inner class of the latter but I want a bit of convenience for testing
 */
@Value
public class PricingContext {
    private final Basket basket;

    /**
     * give amount of productName in the basket
     *
     * @param productName a name of the product
     * @return count number of items of given product (defined by name) in the basket
     */
    public int amountOf(String productName) {
        return basket.getContent().entrySet().stream()
                .filter(e -> e.getKey().getName().equalsIgnoreCase(productName))
                .findFirst().map(Map.Entry::getValue).orElse(0);
    }

    /**
     * give a "raw" (i.e. with no promotions applied) price of productName in the basket (or 0 if basket does not contain that product)
     *
     * @param productName a name of the product
     * @return price of a single item of given product (defined by name) in the basket.
     */
    public double priceFor(String productName) {
        return basket.getContent().keySet().stream()
                .filter(e -> e.getName().equalsIgnoreCase(productName))
                .findFirst().map(Product::getPrice)
                .map(m -> m.getNumber().doubleValue()).orElse(0.0);
    }

    /**
     * give total price of all items of productName in the basket (or 0 if basket does not contain that product). No promotions applied.
     * <p>
     * for a given product, {@code this.totalPriceFor('product') == this.priceFor('product') * this.amountOf('product')}
     *
     * @param productName a name of the product
     * @return total price of all single items of given product (defined by name) in the basket.
     */
    public double totalPriceFor(String productName) {
        return basket.getContent().entrySet().stream()
                .filter(e -> e.getKey().getName().equalsIgnoreCase(productName))
                .findFirst().map(e -> e.getKey().getPrice().multiply(e.getValue()))
                .map(m -> m.getNumber().doubleValue()).orElse(0.0);
    }

    /**
     * give total price of all items in the basket (or 0 if basket is empty). No promotions applied.
     * <p>
     * Useful for promotions like "5 quid off if total exceeds £50"
     *
     * @return total price of all single items of given product (defined by name) in the basket.
     */
    public double totalPrice() {
        return basket.getContent().entrySet().stream()
                .map(e -> e.getKey().getPrice().multiply(e.getValue()))
                .reduce(Money::add)
                .map(m -> m.getNumber().doubleValue()).orElse(0.0);
    }

    /**
     * A shortcut for the basket's date
     *
     * @return a date of purchase
     */
    public LocalDate getDate() {
        return basket.getDate();
    }

}
