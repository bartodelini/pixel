package com.bartodelini.pixel.modules.terminal.bbtext;

import java.util.List;

/**
 * A <i>BBParseResult</i> is used to store the result of the parsing of a bb text.
 *
 * @param tokens    the {@linkplain List} of {@linkplain BBChunk BBChunks}.
 * @param clearText the text without the tokens.
 * @param rawText   the text without any parsing.
 * @author Bartolini
 * @version 1.1
 */
public record BBParseResult(List<BBChunk> tokens, String clearText, String rawText) {
}