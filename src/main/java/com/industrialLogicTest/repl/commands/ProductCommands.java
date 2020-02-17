package com.industrialLogicTest.repl.commands;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import com.google.common.base.Splitter;
import com.industrialLogicTest.domain.Product;
import com.industrialLogicTest.repl.ReplSessionState;

/**
 * A set of commands (extensions of {@linkplain com.industrialLogicTest.repl.commands.ReplCommand} working with the {@linkplain com.industrialLogicTest.domain.Product}: add/modify items (no "remove" operation as this does not make much sense).
 */
public class ProductCommands {
    private static final Splitter ARGUMENT_SPLITTER = Splitter.on(":").trimResults().omitEmptyStrings();
    public static final ReplCommand ADD_PRODUCT = new ReplCommand("product",
            "Add new / modify existing Product. Syntax: <name>:<price>:<unit>. Example: product eggs:2.20:dozen") {
        @Override
        public String apply(String arguments, ReplSessionState session) throws ParsingException {
            List<String> strings = ARGUMENT_SPLITTER.splitToList(arguments);
            if (strings.size() < 3) {
                return "Unable to parse Product data out of '" + arguments + "'. Expecting <name>:<price>:<unit>";
            }
            try {
                double price = new BigDecimal(strings.get(1)).setScale(2, RoundingMode.UP).doubleValue();
                String productName = strings.get(0).toLowerCase();
                Product existingProduct = session.findProduct(productName);
                Product newProduct = new Product(productName, strings.get(2), price);
                List<Product> products = session.getProducts();
                if (existingProduct != null) {
                    products.remove(existingProduct);
                    products.add(newProduct);
                    return "replaced " + existingProduct + " with " + newProduct;
                } else {
                    products.add(newProduct);
                    return "added " + newProduct;
                }
            } catch (NumberFormatException e) {
                return "Unable to parse price out of '" + strings.get(1) + "' - part of '" + arguments + "'";
            }
        }
    };


}
