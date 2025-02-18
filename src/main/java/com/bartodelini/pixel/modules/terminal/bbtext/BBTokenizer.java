package com.bartodelini.pixel.modules.terminal.bbtext;

import java.util.List;

/**
 * A <i>BBTokenizer</i> is used to break down a text into a {@linkplain List} of {@linkplain BBToken BBTokens}.
 *
 * @author Bartolini
 * @version 1.0
 */
public interface BBTokenizer {

    /**
     * Returns a {@linkplain List} of {@linkplain BBToken BBTokens} extracted from the passed in text.
     *
     * @param input the text to tokenize.
     * @return a {@code List} of {@code BBTokens} extracted from the specified text.
     */
    List<BBToken> tokenize(String input);
}