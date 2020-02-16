package com.industrialLogicTest.repl.commands;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.util.Strings;

import com.google.common.base.Splitter;
import com.industrialLogicTest.domain.Basket;
import com.industrialLogicTest.domain.Product;
import com.industrialLogicTest.repl.ReplSessionState;

public class BasketCommands {
    private static final Splitter ARGUMENT_SPLITTER = Splitter.on(',').trimResults().omitEmptyStrings();
    private static final Splitter.MapSplitter MAP_SPLITTER = Splitter.on(',').trimResults().omitEmptyStrings().withKeyValueSeparator(":");

    public static final ReplCommand CLEAR_BASKET = new ReplCommand("clear", "Reset basket to (today, no items) state") {
        @Override
        public String apply(String arguments, ReplSessionState session) {
            session.updateBasket(new Basket());
            return "";
        }
    };

    public static final ReplCommand ADD_ITEM = new AddItemCommand();


    private static class AddItemCommand extends ReplCommand {
        public AddItemCommand() {
            super("add", "Add item(s) to basket. Items should be described as name:amount[[,name:amount],...]. Example: add apples:3, soup:2, bread: 4.");
        }

        @Override
        public String apply(String arguments, ReplSessionState session) throws ParsingException {
            Map<Product, Integer> productPairs = extractAmounts(session, arguments);
            Basket currentBasket = session.getBasket();
            for (Map.Entry<Product, Integer> entry : productPairs.entrySet()) {
                currentBasket = currentBasket.addItems(entry.getKey(), entry.getValue());
            }
            session.updateBasket(currentBasket);
            //empty string means "nothing special, all OK"
            return Strings.EMPTY;
        }
    }

    /**
     * decodes (parses) a given argument line returning Product -> Amount pairs
     *
     * @param arguments a string of Product:Amount pairs, separated by comma
     * @return decoded Map
     * @throws com.industrialLogicTest.repl.commands.ParsingException informing about exact parsing problem
     */
    //visible for testing
    static Map<Product, Integer> extractAmounts(ReplSessionState session, String arguments) throws ParsingException {
        Map<String, String> parsed = MAP_SPLITTER.split(arguments);
        //stream-based or lambda-based (via parsed.forEach((productName, amountStr) -> {...}
        // processing will be a bit bulky, imperative is much cleaner:
        Map<Product, Integer> ret = new HashMap<>();
        for (Map.Entry<String, String> src : parsed.entrySet()) {
            String productName = src.getKey().trim();
            Product product = session.findProduct(productName);
            if (product == null) {
                throw new ParsingException("Unable to find Product by name '" + productName + "' - part of " + arguments);
            }
            String amountStr = src.getValue().trim();
            try {
                ret.put(product, Integer.parseInt(amountStr));
            } catch (Exception e) {
                throw new ParsingException("Unable to restore integer out of '" + amountStr + "' - part of " + arguments);
            }
        }
        return ret;
    }


}
