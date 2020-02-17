package com.industrialLogicTest.repl;

import static com.industrialLogicTest.repl.ReplSessionFormatter.HELP_COMMANDS;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.google.common.base.Strings;
import com.industrialLogicTest.repl.commands.BasketCommands;
import com.industrialLogicTest.repl.commands.ParsingException;
import com.industrialLogicTest.repl.commands.ProductCommands;
import com.industrialLogicTest.repl.commands.PromotionCommands;
import com.industrialLogicTest.repl.commands.ReplCommand;

import lombok.Getter;

/**
 * A "session", keeping track of available commands, session's state and formatting
 */
public class ReplSessionController {
    @Getter
    private final List<ReplCommand> commands = Arrays.asList(
            BasketCommands.CLEAR_BASKET, BasketCommands.ADD_ITEMS, BasketCommands.REMOVE_ITEMS, BasketCommands.NEW_DATE,
            ReplSessionFormatter.LIST_PRODUCTS, ProductCommands.ADD_PRODUCT,
            ReplSessionFormatter.LIST_PROMOTIONS, PromotionCommands.ADD_PROMOTION, PromotionCommands.REMOVE_PROMOTION,
            ReplCommand.EXIT
    );

    @Getter
    private final ReplSessionState state = new ReplSessionState();

    private final ReplSessionFormatter formatter = new ReplSessionFormatter();

    public String execute(String nextLine) {
        //first element(until space) is a command, others are arguments
        String trimmed = Strings.nullToEmpty(nextLine).trim();
        if (trimmed.isEmpty()) {
            return "empty command!";
        } else {
            String[] parsed = trimmed.split(" ");
            String command = parsed[0].toLowerCase();
            if (HELP_COMMANDS.contains(command)) {
                return formatter.formatAvailableCommands(getCommands());
            }
            //remainder is argument(s)
            String arguments = trimmed.length() > command.length() ? trimmed.substring(command.length() + 1) : "";
            Optional<ReplCommand> replCommand = commands.stream().filter(c -> c.getName().equalsIgnoreCase(command)).findFirst();
            if (replCommand.isPresent()) {
                try {
                    return replCommand.get().apply(arguments, state);
                } catch (ParsingException e) {
                    return e.getMessage();
                }
            } else {
                return "Unable to interpret command: got '" + command + "' out of '" + trimmed + "'";
            }
        }
    }
}
