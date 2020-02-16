package com.industrialLogicTest.repl.commands;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

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
            Basket existing = session.getBasket();
            session.updateBasket(new Basket(existing.getDate()));
            return OK;
        }
    };

    public static final ReplCommand ADD_ITEMS = new AddItemsCommand();
    public static final ReplCommand REMOVE_ITEMS = new RemoveItemsCommand();
    public static final ReplCommand NEW_DATE = new UpdateDateCommand();

    private static class AddItemsCommand extends ItemUpdateCommand {
        public AddItemsCommand() {
            super("add", "Add item(s) to basket. Items should be described as name:amount[[,name:amount],...]. Example: add apples:3, soup:2, bread: 4.");
        }

        @Override
        protected Basket applyPerItem(Basket basket, Map.Entry<Product, Integer> entry) {
            return basket.addItems(entry.getKey(), entry.getValue());
        }
    }

    private static class RemoveItemsCommand extends ItemUpdateCommand {
        public RemoveItemsCommand() {
            super("remove", "Remove item(s) from basket. Items should be described as name:amount[[,name:amount],...]. Example: remove apples:1, soup:3");
        }

        @Override
        protected Basket applyPerItem(Basket basket, Map.Entry<Product, Integer> entry) {
            return basket.removeItems(entry.getKey(), entry.getValue());
        }
    }

    private static abstract class ItemUpdateCommand extends ReplCommand {

        public ItemUpdateCommand(String name, String description) {
            super(name, description);
        }

        protected abstract Basket applyPerItem(Basket basket, Map.Entry<Product, Integer> entry);

        @Override
        public String apply(String arguments, ReplSessionState session) throws ParsingException {
            Map<Product, Integer> productPairs = extractAmounts(session, arguments);
            Basket currentBasket = session.getBasket();
            for (Map.Entry<Product, Integer> entry : productPairs.entrySet()) {
                currentBasket = applyPerItem(currentBasket, entry);
            }
            session.updateBasket(currentBasket);
            return OK;
        }

    }

    /**
     * Update Basket's date (do not touch content)
     */
    private static class UpdateDateCommand extends ReplCommand {
        public UpdateDateCommand() {
            super("date", "Update basket's purchase date. The new date must be in YYYY-MM-DD formar. Example: date 2020-02-18");
        }

        @Override
        public String apply(String arguments, ReplSessionState session) throws ParsingException {
            try {
                LocalDate newDate = LocalDate.parse(arguments.trim());
                Basket currentBasket = session.getBasket();
                session.updateBasket(currentBasket.withDate(newDate));
                return OK;
            } catch (Exception e) {
                throw new ParsingException("unable to parse new purchase date from '" + arguments + "'");
            }
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
