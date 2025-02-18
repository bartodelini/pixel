package com.bartodelini.pixel.modules.rendering.bitmap;

/**
 * A utility class providing useful functions for {@linkplain Bitmap} creation.
 *
 * @author Bartolini
 * @version 1.0
 */
public final class Bitmaps {

    /**
     * Returns a {@linkplain Bitmap} of the specified width and height, with all its pixels set to the specified color.
     *
     * @param width  the width of the {@code Bitmap}.
     * @param height the height of the {@code Bitmap}.
     * @param color  the color of the pixels.
     * @return a {@code Bitmap} of the specified width and height, with all its pixels set to the specified color.
     */
    public static Bitmap getUnicolor(int width, int height, int color) {
        Bitmap bitmap = new Bitmap(width, height);
        bitmap.setAllPixels(color);
        return bitmap;
    }

    /**
     * Returns a {@linkplain Bitmap} of the specified width and height, with random pixels.
     *
     * @param width  the width of the {@code Bitmap}.
     * @param height the height {@code Bitmap}.
     * @return a {@code Bitmap} of the specified width and height, with random pixels.
     */
    public static Bitmap getRandom(int width, int height) {
        Bitmap bitmap = new Bitmap(width, height);
        bitmap.setAllPixels(color -> Colors.randomRGB());
        return bitmap;
    }
}