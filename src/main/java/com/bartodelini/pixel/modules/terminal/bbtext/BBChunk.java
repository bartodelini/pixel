package com.bartodelini.pixel.modules.terminal.bbtext;

import java.util.Collections;
import java.util.Map;

/**
 * A <i>BBChunk</i> represents a part of a bb text inside bb tokens.
 *
 * @param properties the properties of the text part.
 * @param content    the text part.
 * @author Bartolini
 * @version 1.1
 */
public record BBChunk(Map<String, String> properties, String content) {

    @Override
    public Map<String, String> properties() {
        return Collections.unmodifiableMap(properties);
    }
}