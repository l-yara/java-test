package com.industrialLogicTest.repl;

import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;
import java.util.stream.Collectors;

import com.google.common.base.Strings;
import com.industrialLogicTest.domain.Basket;
import com.industrialLogicTest.domain.Product;
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
                    String.format("-- %d %s(s) of %s at %.2f", amount, p.getUnit(), p.getName(), p.getPrice().getNumber().doubleValue()))
                    .append(LINE_SEPARATOR));
        }
        ret.append(String.format("Total Cost: %.2f", totalCost));
        return ret.toString();
    }


}
