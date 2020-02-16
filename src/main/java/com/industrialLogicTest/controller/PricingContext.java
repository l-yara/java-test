package com.industrialLogicTest.controller;


import static com.industrialLogicTest.domain.Basket.ZERO;

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
class PricingContext {
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
    public Money priceFor(String productName) {
        return basket.getContent().keySet().stream()
                .filter(e -> e.getName().equalsIgnoreCase(productName))
                .findFirst().map(Product::getPrice).orElse(ZERO);
    }

    /**
     * give total price of all items of productName in the basket (or 0 if basket does not contain that product). No promotions applied.
     * <p>
     * for a given product, {@code this.totalPriceFor('product') == this.priceFor('product') * this.amountOf('product')}
     *
     * @param productName a name of the product
     * @return total price of all single items of given product (defined by name) in the basket.
     */
    public Money totalPriceFor(String productName) {
        return basket.getContent().entrySet().stream()
                .filter(e -> e.getKey().getName().equalsIgnoreCase(productName))
                .findFirst().map(e -> e.getKey().getPrice().multiply(e.getValue())).orElse(ZERO);
    }

    /**
     * give total price of all items in the basket (or 0 if basket is empty). No promotions applied.
     * <p>
     * Useful for promotions like "5 quid off if total exceeds 50"
     *
     * @return total price of all single items of given product (defined by name) in the basket.
     */
    public Money totalPrice() {
        return basket.getContent().entrySet().stream()
                .map(e -> e.getKey().getPrice().multiply(e.getValue()))
                .reduce(Money::add).orElse(ZERO);
    }

    /**
     * A shortcut for the basket's date
     *
     * @return a date of purchase
     */
    public LocalDate getDate() {
        return basket.getDate();
    }

    /**
     * A shortcut to create a constant {@linkplain org.javamoney.moneta.Money} amount.
     * <p>
     * Useful for the case "buy something stupid and get 5 quid back"
     *
     * @param amount a sum to "wrap" in the Money object
     * @return Money with exact amount value
     */
    public Money exact(double amount) {
        return Basket.amount(amount);
    }

}
