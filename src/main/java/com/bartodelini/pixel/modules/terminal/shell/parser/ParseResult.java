package com.bartodelini.pixel.modules.terminal.shell.parser;

import java.util.List;

/**
 * A <i>ParseResult</i> is a container for holding the result of parsing a command.
 *
 * @author Bartolini
 * @version 1.0
 */
public class ParseResult {

    private final String inputString;
    private final String prefix;
    private final String operableName;
    private final boolean hasPrefix;
    private final List<String> args;

    /**
     * Allocates a new {@code ParseResult} by passing in the inputString, the name of the operable as well as the
     * {@linkplain List} of arguments.
     *
     * @param inputString  the input string.
     * @param operableName the name of the operable.
     * @param args         the {@code List} of arguments.
     */
    public ParseResult(String inputString, String operableName, List<String> args) {
        this.inputString = inputString;
        this.prefix = "";
        this.operableName = operableName;
        this.hasPrefix = false;
        this.args = args;
    }

    /**
     * Allocates a new {@code ParseResult} by passing in the inputString, the prefix, the name of the operable as well
     * as the {@linkplain List} of arguments.
     *
     * @param inputString  the input string.
     * @param prefix       the prefix.
     * @param operableName the name of the operable.
     * @param args         the {@code List} of arguments.
     */
    public ParseResult(String inputString, String prefix, String operableName, List<String> args) {
        this.inputString = inputString;
        this.prefix = prefix;
        this.operableName = operableName;
        this.hasPrefix = true;
        this.args = args;
    }

    /**
     * Returns the input string.
     *
     * @return the input string.
     */
    public String getInputString() {
        return inputString;
    }

    /**
     * Returns the name of the operable.
     *
     * @return the name of the operable.
     */
    public String getOperableName() {
        return operableName;
    }

    /**
     * Returns the {@linkplain List} of arguments.
     *
     * @return the {@code List} of arguments.
     */
    public List<String> getArgs() {
        return args;
    }

    /**
     * Returns the prefix.
     *
     * @return the prefix.
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Returns whether a prefix was specified for this command.
     *
     * @return {@code true} if a prefix is specified; {@code false} otherwise.
     */
    public boolean hasPrefix() {
        return hasPrefix;
    }
}