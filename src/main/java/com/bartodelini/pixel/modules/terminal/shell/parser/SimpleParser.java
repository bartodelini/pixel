package com.bartodelini.pixel.modules.terminal.shell.parser;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * A <i>SimpleParser</i> is used to parse an input string into a {@linkplain Collection} of
 * {@linkplain ParseResult ParseResults}.
 *
 * @author Bartolini
 * @version 1.1
 */
public class SimpleParser implements Parser {

    private final char quoteChar;
    private final String quoteCharString;
    private final char commandChar;
    private final char spaceChar;

    /**
     * Allocates a new {@code SimpleParser} object by passing in {@code chars} used to indicate a quote, command
     * delimiter and space respectively.
     *
     * @param quoteChar   the {@code char} used to indicate a quote.
     * @param commandChar the {@code char} used to indicate a command delimiter.
     * @param spaceChar   the {@code char} used to indicate a space.
     */
    public SimpleParser(char quoteChar, char commandChar, char spaceChar) {
        this.quoteChar = quoteChar;
        this.commandChar = commandChar;
        this.spaceChar = spaceChar;
        this.quoteCharString = String.valueOf(quoteChar);
    }

    @Override
    public Collection<ParseResult> parseInput(String input) {
        List<ParseResult> resultList = new LinkedList<>();
        splitOnTokenNotInQuotes(input, commandChar).forEach(c ->
                resultList.add(parseCommand(c, splitOnTokenNotInQuotes(c, spaceChar))));
        return resultList;
    }

    /**
     * Helper method used to split an unquoted token.
     *
     * @param input      the input string to split.
     * @param splitToken the token to split on.
     * @return the {@linkplain List} of split strings.
     */
    private List<String> splitOnTokenNotInQuotes(String input, char splitToken) {
        List<String> words = new LinkedList<>();

        boolean inQuote = false;
        int startPos = 0;

        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);

            if (currentChar == quoteChar) {
                inQuote = !inQuote;
            }

            if (!inQuote && currentChar == splitToken) {
                if (startPos != i) {
                    words.add(input.substring(startPos, i));
                }
                startPos = i + 1;
            }
        }

        if (startPos <= input.length() - 1) {
            words.add(input.substring(startPos));
        }

        return words;
    }

    /**
     * Helper method used to parse an input string and a {@linkplain List} of words to a {@linkplain ParseResult}.
     *
     * @param inputString the input string to parse.
     * @param words       the {@code List} of words.
     * @return the resulting {@code ParseResult}.
     */
    private ParseResult parseCommand(String inputString, List<String> words) {
        if (words.isEmpty()) {
            return null;
        }

        LinkedList<String> trimmedWords = new LinkedList<>();

        for (String word : words) {
            if (word.startsWith(quoteCharString)) {
                word = word.substring(1);
            }
            if (word.endsWith(quoteCharString)) {
                word = word.substring(0, word.length() - 1);
            }
            trimmedWords.add(word);
        }

        String[] splitOperableName = trimmedWords.removeFirst().split("\\.");
        if (splitOperableName.length > 1) {
            return new ParseResult(
                    inputString,
                    splitOperableName[0],
                    String.join(".", Arrays.copyOfRange(splitOperableName, 1, splitOperableName.length)),
                    trimmedWords);
        }
        return new ParseResult(inputString, splitOperableName[0], trimmedWords);
    }
}