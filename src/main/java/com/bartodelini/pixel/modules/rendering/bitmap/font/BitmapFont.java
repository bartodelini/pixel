package com.bartodelini.pixel.modules.rendering.bitmap.font;

import com.bartodelini.pixel.modules.rendering.bitmap.Bitmap;

/**
 * A <i>BitmapFont</i> can be used to facilitate the rendering of fonts onto a {@linkplain Bitmap}.
 *
 * @author Bartolini
 * @version 1.0
 */
public interface BitmapFont {

    /**
     * A <i>TextAlignment</i> is used to indicate the alignment of rendered text.
     */
    enum TextAlignment {
        /**
         * The default text alignment. Indicates a rendering of text such that its beginning is at specified position.
         */
        LEFT,
        /**
         * Indicates a rendering of text such that its center is at the specified position.
         */
        CENTER,
        /**
         * Indicates a rendering of text such that its end is at specified position.
         */
        RIGHT
    }

    /**
     * Returns a {@linkplain Bitmap} for the specified char.
     *
     * @param c the char to return the {@code Bitmap} for.
     * @return the {@code Bitmap} for the specified char.
     */
    Bitmap getBitmapForChar(char c);

    /**
     * Returns the {@linkplain BitmapFontMetrics} for this {@code BitmapFont}.
     *
     * @return the {@code BitmapFontMetrics} for this {@code BitmapFont}.
     */
    BitmapFontMetrics getBitmapFontMetrics();
}