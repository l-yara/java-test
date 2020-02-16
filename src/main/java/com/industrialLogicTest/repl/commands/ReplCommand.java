package com.industrialLogicTest.repl.commands;

import com.industrialLogicTest.repl.ReplSessionState;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public abstract class ReplCommand {
    public static final String OK = "";

    private final String name;
    private final String description;

    /**
     * Accepts unparsed argument string and session. Updates session, returns user's outcome (like OK or "wrong parameters message")
     *
     * @param arguments the arguments line (unparsed)
     * @param session a current session
     * @return user's outcome (might be null or empty)
     */
    public abstract String apply(String arguments, ReplSessionState session) throws ParsingException;

    public static final ReplCommand EXIT = new ReplCommand("quit", "Quit the program") {
        @Override
        public String apply(String arguments, ReplSessionState session) {
            System.exit(0);
            return OK;
        }
    };
}
