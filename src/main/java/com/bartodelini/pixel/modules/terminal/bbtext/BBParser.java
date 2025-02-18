package com.bartodelini.pixel.modules.terminal.bbtext;

import java.util.List;

/**
 * A <i>BBParser</i> is used to parse a text into a {@linkplain List} of {@linkplain BBChunk BBChunks}.
 *
 * @author Bartolini
 * @version 1.0
 */
public interface BBParser {

    /**
     * Returns the {@linkplain BBParseResult} from the passed in text.
     *
     * @param text the text to parse.
     * @return the {@code BBParseResult} from the passed in text.
     */
    BBParseResult parse(String text);
}