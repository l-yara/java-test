package com.industrialLogicTest;

import java.io.PrintStream;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.base.Strings;
import com.industrialLogicTest.repl.ReplSessionFormatter;
import com.industrialLogicTest.repl.ReplSessionController;
import com.industrialLogicTest.repl.ReplSessionState;

/**
 * A poor man's REPL (Read-Evaluate-Print-Loop) implementation
 * <p>
 * In a perfect world I'd use JLine (https://github.com/jline/jline3) for a cool CLI / PERL
 * but it is not too stable under Windows. Pity!
 */
public class REPL {

    private static void disableJdkLoggers() {
    }

    public static void main(String[] args) {
        //disable JDK loggers
        Logger.getLogger("").setLevel(Level.OFF);

        ReplSessionController controller = new ReplSessionController();
        ReplSessionFormatter formatter  = new ReplSessionFormatter();

        Scanner scanner = new Scanner(System.in);
        PrintStream sink = System.out;

        sink.println("Greetings!");
        sink.println(formatter.formatAvailableCommands(controller.getCommands()));
        while (true) {
            ReplSessionState state = controller.getState();
            sink.println(formatter.formatBasket(state.getBasket(), state.getTotalBasketCost()));
            String commandOutcome = controller.execute(scanner.nextLine());
            if (!Strings.isNullOrEmpty(commandOutcome)) {
                sink.println(commandOutcome);
            }
        }
    }
}
