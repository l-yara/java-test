package com.industrialLogicTest.repl;

import static com.industrialLogicTest.controller.PriceCalculator.endOfMonth;
import static com.industrialLogicTest.domain.Product.APPLES;
import static com.industrialLogicTest.domain.Product.BREAD;
import static com.industrialLogicTest.domain.Product.MILK;
import static com.industrialLogicTest.domain.Product.SOUP;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import com.industrialLogicTest.controller.PriceCalculator;
import com.industrialLogicTest.domain.Basket;
import com.industrialLogicTest.domain.Product;
import com.industrialLogicTest.domain.Promotion;

import lombok.Getter;


public class ReplSessionState {
    private final LocalDate today = LocalDate.now();

    @Getter
    private final List<Product> products = Arrays.asList(SOUP, BREAD, MILK, APPLES);

    @Getter
    //as set in spec
    private final List<Promotion> promotions = Arrays.asList(
            new Promotion("2 tins -> bread 1/2 price", today.minusDays(1), today.plusDays(6),
                    //if you have two or more tins of soup, get 0.5 * price of bread discount
                    "amountOf('soup') >= 2 ? priceFor('bread') * 0.5 : 0"),
            //price for all apples -10% (no apples - no discount)
            new Promotion("apples 10% off", today.plusDays(3), endOfMonth(today, 1),
                    "totalPriceFor('apples') * 0.1")

    );

    @Getter
    private Basket basket = new Basket();
    @Getter
    private double totalBasketCost = 0.0;

    public Basket updateBasket(Basket newBasket) {
        this.basket = newBasket;
        totalBasketCost = new PriceCalculator(getPromotions()).calculatePrice(basket);
        return basket;
    }

    //find product by name. Formally, storing Products in Map should provide me with faster access
    //but difference in speed is negligible while List is more convenient in maintaining order
    //(I do know about LinkedHashMap)
    public Product findProduct(String name) {
        return getProducts().stream().filter(p -> p.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }
}
