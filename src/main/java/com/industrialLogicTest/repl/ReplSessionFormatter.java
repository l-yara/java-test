package com.industrialLogicTest.repl;

import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;
import java.util.stream.Collectors;

import com.google.common.base.Strings;
import com.industrialLogicTest.domain.Basket;
import com.industrialLogicTest.domain.Product;
import com.industrialLogicTest.domain.Promotion;
import com.industrialLogicTest.repl.commands.ReplCommand;

import lombok.Value;

@Value
public class ReplSessionFormatter {
    public static final String LINE_SEPARATOR = System.lineSeparator();
    public static final List<String> HELP_COMMANDS = Arrays.asList("?", "help");

    public String formatAvailableCommands(List<ReplCommand> commands) {
        StringBuilder ret = new StringBuilder("Available commands:" + LINE_SEPARATOR);
        String helpCommand = String.join(", ", HELP_COMMANDS);
        //max width of the command - for better formatting
        int width = Math.max(commands.stream().mapToInt(c -> c.getName().length()).max().orElse(0), helpCommand.length());
        ret.append(formatCommand(helpCommand, width, "Print command list") + LINE_SEPARATOR);

        ret.append(commands.stream().map(c -> formatCommand(c.getName(), width, c.getDescription())).collect(Collectors.joining(LINE_SEPARATOR)));
        return ret.toString();
    }

    private String formatCommand(String name, int width, String description) {
        return Strings.padEnd(name, width, ' ') + " : " + description;
    }

    public String formatBasket(Basket basket, double totalCost) {
        StringBuilder ret = new StringBuilder("Current basket: ");
        SortedMap<Product, Integer> content = basket.getContent();
        if (content.isEmpty()) {
            ret.append("(No Items)" + LINE_SEPARATOR);
        } else {
            ret.append("purchased at " + basket.getDate() + LINE_SEPARATOR);
            ret.append("- Items:" + LINE_SEPARATOR);
            content.forEach((p, amount) -> ret.append(
                    String.format("-- %d %s(s) of %s at %.2f each", amount, p.getUnit(), p.getName(), p.getPrice().getNumber().doubleValue()))
                    .append(LINE_SEPARATOR));
        }
        ret.append(String.format("Total Cost: %.2f", totalCost));
        return ret.toString();
    }


    public static final ReplCommand LIST_PROMOTIONS = new ReplCommand("promotions", "List all promotions") {
        @Override
        public String apply(String arguments, ReplSessionState session) {
            List<Promotion> promotions = session.getPromotions();
            StringBuilder ret = new StringBuilder("Current promotions: ");
            if (promotions.isEmpty()) {
                ret.append("(No Promotions)");
            } else {
                for (int i = 0; i < promotions.size(); i++) {
                    Promotion p = promotions.get(i);
                    ret.append(LINE_SEPARATOR)
                            .append(String.format("-- (%d): \"%s\", valid from %s to %s, expression: \"%s\"", i, p.getName(), p.getValidFrom(), p.getValidTo(), p.getDiscountExp().getExpressionString()));
                }
            }
            return ret.append(LINE_SEPARATOR).toString();
        }

    };

    public static final ReplCommand LIST_PRODUCTS = new ReplCommand("products", "List all products") {
        @Override
        public String apply(String arguments, ReplSessionState session) {
            List<Product> products = session.getProducts();
            StringBuilder ret = new StringBuilder("Current products: " + LINE_SEPARATOR);
            if (products.isEmpty()) {
                ret.append("(No Products)" + LINE_SEPARATOR);
            } else {
                products.forEach((p) -> ret.append(
                        String.format("-- %s : %.2f/%s", p.getName(), p.getPrice().getNumber().doubleValue(), p.getUnit()))
                        .append(LINE_SEPARATOR));
            }
            return ret.toString();
        }
    };

}
