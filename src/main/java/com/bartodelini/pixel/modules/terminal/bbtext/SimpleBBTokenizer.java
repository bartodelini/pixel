package com.bartodelini.pixel.modules.terminal.bbtext;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * A <i>SimpleBBTokenizer</i> is used to break down a text into a {@linkplain List} of {@linkplain BBToken BBTokens}.
 *
 * @author Bartolini
 * @version 1.1
 */
public class SimpleBBTokenizer implements BBTokenizer {

    private final char openingBracketChar;
    private final char closingBracketChar;
    private final String closingTokenString;

    /**
     * Allocates a new {@code SimpleBBTokenizer} by passing in the {@code chars} for the opening and closing brackets,
     * as well as the closing token.
     *
     * @param openingBracketChar the {@code char} for the opening bracket.
     * @param closingBracketChar the {@code char} for the closing bracket.
     * @param closingTokenChar   the {@code char} for the opening token.
     */
    public SimpleBBTokenizer(char openingBracketChar, char closingBracketChar, char closingTokenChar) {
        this.openingBracketChar = openingBracketChar;
        this.closingBracketChar = closingBracketChar;
        this.closingTokenString = String.valueOf(closingTokenChar);
    }

    /**
     * Returns a {@linkplain List} of {@linkplain BBToken BBTokens} extracted from the passed in text.
     *
     * @param input the text to tokenize.
     * @return a {@code List} of {@code BBTokens} extracted from the specified text.
     * @throws NullPointerException if the specified text is {@code null}.
     */
    @Override
    public List<BBToken> tokenize(String input) {
        Objects.requireNonNull(input, "input must not be null");

        List<BBToken> bbTokens = new LinkedList<>();
        StringBuilder textSB = new StringBuilder();
        StringBuilder tokenSB = new StringBuilder();

        boolean inToken = false;

        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);

            if (currentChar == openingBracketChar) {
                if (inToken) {
                    textSB.append(currentChar).append(tokenSB);
                    tokenSB = new StringBuilder();
                } else {
                    inToken = true;
                }
                continue;
            }

            if (inToken) {
                if (currentChar == closingBracketChar) {
                    if (textSB.length() != 0) {
                        String textString = textSB.toString();
                        textSB = new StringBuilder();
                        bbTokens.add(new BBToken(BBTokenType.TEXT, textString, textString));
                    }
                    String tokenString = tokenSB.toString();
                    tokenSB = new StringBuilder();
                    bbTokens.add(new BBToken(
                            tokenString.startsWith(closingTokenString) ? BBTokenType.RIGHT_DELIMITER : BBTokenType.LEFT_DELIMITER,
                            tokenString.startsWith(closingTokenString) ? tokenString.substring(1) : tokenString,
                            openingBracketChar + tokenString + closingBracketChar
                    ));
                    inToken = false;
                } else {
                    tokenSB.append(currentChar);
                }
            } else {
                textSB.append(currentChar);
            }
        }

        if (textSB.length() != 0 || tokenSB.length() != 0) {
            if (tokenSB.length() != 0) {
                textSB.append(openingBracketChar).append(tokenSB);
            }
            String restString = textSB.toString();
            bbTokens.add(new BBToken(BBTokenType.TEXT, restString, restString));
        }

        return bbTokens;
    }
}