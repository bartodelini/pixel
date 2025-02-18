package com.bartodelini.pixel.modules.terminal.bbtext;

/**
 * A <i>BBToken</i> is the token part of a bb text.
 *
 * @param type       the {@linkplain BBTokenType} describing the type of this {@code BBToken}.
 * @param content    the content of this {@code BBToken}.
 * @param rawContent the raw content of this {@code BBToken}.
 * @author Bartolini
 * @version 1.1
 */
public record BBToken(BBTokenType type, String content, String rawContent) {
}