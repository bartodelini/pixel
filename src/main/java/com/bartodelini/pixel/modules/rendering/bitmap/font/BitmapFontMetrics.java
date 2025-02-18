package com.bartodelini.pixel.modules.rendering.bitmap.font;

import com.bartodelini.pixel.modules.rendering.bitmap.Bitmap;

/**
 * A <i>BitmapFontMetrics</i> provides methods for determining the length of a text in a concrete
 * {@linkplain BitmapFont}, as well as the width and height of character {@linkplain Bitmap Bitmaps}.
 *
 * @author Bartolini
 * @version 1.0
 */
public interface BitmapFontMetrics {

    /**
     * Returns the width of a character {@linkplain Bitmap} in pixels.
     *
     * @return the width of a character {@code Bitmap} in pixels.
     */
    int getCharacterWidth();

    /**
     * Returns the height of a character {@linkplain Bitmap} in pixels.
     *
     * @return the height of a character {@code Bitmap} in pixels.
     */
    int getCharacterHeight();

    /**
     * Returns the width of a string in pixels, rendered using the {@linkplain BitmapFont} of this
     * {@code BitmapFontMetrics}.
     *
     * @param str the string to get the width of.
     * @return the width of the specified string in pixels.
     */
    int stringWidth(String str);

    /**
     * Returns the height of a string in pixels, rendered using the {@linkplain BitmapFont} of this
     * {@code BitmapFontMetrics}.
     *
     * @param str the string to get the height of.
     * @return the height of the specified string in pixels.
     */
    int stringHeight(String str);
}