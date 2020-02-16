package com.industrialLogicTest.domain;

import java.time.LocalDate;
import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;

import lombok.Value;

@Value
public class Basket {

    private final LocalDate date;

    //content sorted by Product
    private final SortedMap<Product, Integer> content;

    public Basket() {
        this(LocalDate.now());
    }

    public Basket(LocalDate date) {
        this(date, new TreeMap<>());
    }

    private Basket(LocalDate date, SortedMap<Product, Integer> content) {
        this.date = date;
        this.content = Collections.unmodifiableSortedMap(content);
    }

    /**
     * copy existing Basket on the new Date
     *
     * @param newDate a move-to date
     * @return this
     */
    public Basket withDate(LocalDate newDate) {
        return new Basket(newDate, this.content);
    }

    /**
     * Add one item of given product to the basket
     *
     * @param product a product to add
     * @return this
     */
    public Basket addItem(Product product) {
        return addItems(product, 1);
    }

    /**
     * Add a given amount of items of given product to the basket
     *
     * @param product a product to add
     * @param amount  the amount to add
     * @return this
     */
    public Basket addItems(Product product, int amount) {
        TreeMap<Product, Integer> newContent = new TreeMap<>(content);
        newContent.compute(product, (ignore, oldAmount) -> amount + (oldAmount == null ? 0 : oldAmount));
        return new Basket(getDate(), newContent);
    }

    /**
     * Remove one item of given product, if exists in the basket.
     *
     * @param product a product to add
     * @return this
     */
    public Basket removeItem(Product product) {
        return removeItems(product, 1);
    }

    /**
     * Remove given amount of items of given product, if exists in the basket. If not enough items exists - removes all items of the given product.
     *
     * @param product a product to remove
     * @param amount  to remove
     * @return this
     */
    public Basket removeItems(Product product, int amount) {
        TreeMap<Product, Integer> newContent = new TreeMap<>(content);
        newContent.compute(product, (ignore, oldAmount) ->
                //returning null means "remove entry"
                (oldAmount == null || oldAmount < amount ? null : oldAmount - amount));
        return new Basket(getDate(), newContent);
    }

}
