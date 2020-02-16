package com.industrialLogicTest.repl.commands;

/**
 * Exception thrown if command was malformed
 */
public class ParsingException extends Exception {
    public ParsingException(String message) {
        super(message);
    }
}
