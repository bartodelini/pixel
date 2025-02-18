package com.bartodelini.pixel.modules.rendering.bitmap;

import com.bartodelini.pixel.math.vector.Vector2f;
import com.bartodelini.pixel.modules.rendering.bitmap.font.BitmapFont;
import com.bartodelini.pixel.modules.rendering.bitmap.font.BitmapFontMetrics;
import com.bartodelini.pixel.modules.rendering.bitmap.font.StringIndexedBitmapFont;

import java.util.Arrays;
import java.util.Objects;

/**
 * A <i>BitmapGraphics</i> is used to draw to an underlying {@linkplain Bitmap}.
 *
 * @author Bartolini
 * @version 1.0
 */
public class BitmapGraphics {

    protected final Bitmap bitmap;

    protected BitmapFont font;
    private int clearValue = 0;

    /**
     * Allocates a new {@code BitmapGraphics} by passing in its {@linkplain Bitmap} and sets the {@linkplain BitmapFont}
     * of this {@code BitmapGraphics} to {@linkplain StringIndexedBitmapFont#DEFAULT_FONT}.
     *
     * @param bitmap the {@code Bitmap} to be altered by this {@code BitmapGraphics}.
     * @throws NullPointerException if the specified {@code Bitmap} is {@code null}.
     */
    public BitmapGraphics(Bitmap bitmap) {
        this.bitmap = Objects.requireNonNull(bitmap, "bitmap must not be null");
        this.font = StringIndexedBitmapFont.DEFAULT_FONT;
    }

    /**
     * Sets the {@linkplain BitmapFont} used by this {@code BitmapGraphics} to draw text.
     *
     * @param font the new {@code BitmapFont} to be used by this {@code BitmapGraphics}.
     * @throws NullPointerException if the specified {@code BitmapFont} is {@code null}.
     */
    public void setFont(BitmapFont font) {
        this.font = Objects.requireNonNull(font, "font must not be null");
    }

    /**
     * Returns the {@linkplain BitmapFont} used by this {@code BitmapGraphics}.
     *
     * @return the {@code BitmapFont} used by this {@code BitmapGraphics}.
     */
    public BitmapFont getFont() {
        return font;
    }

    /**
     * Returns the {@linkplain BitmapFontMetrics} of the {@linkplain BitmapFont} used by this {@code BitmapGraphics}.
     *
     * @return the {@code BitmapFontMetrics} of the {@code BitmapFont} used by this {@code BitmapGraphics}.
     */
    public BitmapFontMetrics getFontMetrics() {
        return font.getBitmapFontMetrics();
    }

    /**
     * Returns the width of the {@linkplain Bitmap} used by this {@code BitmapGraphics}.
     *
     * @return the width of the {@code Bitmap} used by this {@code BitmapGraphics}.
     */
    public int getWidth() {
        return bitmap.getWidth();
    }

    /**
     * Returns the height of the {@linkplain Bitmap} used by this {@code BitmapGraphics}.
     *
     * @return the height of the {@code Bitmap} used by this {@code BitmapGraphics}.
     */
    public int getHeight() {
        return bitmap.getHeight();
    }

    /**
     * Returns the clear value used by this {@code BitmapGraphics} when using {@linkplain #clear()}.
     *
     * @return the clear value used by this {@code BitmapGraphics} when using {@code #clear()}.
     */
    public int getClearValue() {
        return clearValue;
    }

    /**
     * Sets the clear value used by this {@code BitmapGraphics} when using {@linkplain #clear()}.
     *
     * @param clearValue the new clear value.
     */
    public void setClearValue(int clearValue) {
        this.clearValue = clearValue;
    }

    /**
     * Draws the passed in {@linkplain Bitmap} onto the {@code Bitmap} used by this {@code BitmapGraphics}.
     *
     * @param bitmap the source {@code Bitmap}.
     * @param x      the starting x coordinate in the destination {@code Bitmap}.
     * @param y      the starting y coordinate in the destination {@code Bitmap}.
     * @param x0     the starting x coordinate in the source {@code Bitmap}.
     * @param y0     the starting y coordinate in the source {@code Bitmap}.
     * @param width  the width of the read area in the source {@code Bitmap}.
     * @param height the height of the read area in the source {@code Bitmap}.
     * @throws NullPointerException if the specified {@code Bitmap} is {@code null}.
     */
    public void drawBitmap(Bitmap bitmap, int x, int y, int x0, int y0, int width, int height) {
        Objects.requireNonNull(bitmap, "bitmap must not be null");

        // PARALLEL VERSION
//        IntStream.range(0, height).parallel().forEach(i -> {
//            if (i + y < 0 || i + y >= this.bitmap.getHeight() || i + y0 >= bitmap.getHeight()) return;
//
//            IntStream.range(0, width).parallel().forEach(j -> {
//                if (j + x < 0 || j + x >= this.bitmap.getWidth() || j + x0 >= bitmap.getWidth()) return;
//
//                int value = bitmap.getPixel(j + x0, i + y0);
//                this.bitmap.setPixel(j + x, i + y, value);
//            });
//        });

        for (int i = 0; i < height; i++) {
            if (i + y < 0 || i + y >= this.bitmap.getHeight() || i + y0 >= bitmap.getHeight()) continue;

            for (int j = 0; j < width; j++) {
                if (j + x < 0 || j + x >= this.bitmap.getWidth() || j + x0 >= bitmap.getWidth()) continue;

                int value = bitmap.getPixel(j + x0, i + y0);
                // TODO: Add Bitmap blending
                if ((value & 0xff000000) != 0) {
                    this.bitmap.setPixel(j + x, i + y, value);
                }
            }
        }
    }

    /**
     * Draws the passed in {@linkplain Bitmap} onto the {@code Bitmap} used by this {@code BitmapGraphics}.
     *
     * @param bitmap the source {@code Bitmap}.
     * @param x      the starting x coordinate in the destination {@code Bitmap}.
     * @param y      the starting y coordinate in the destination {@code Bitmap}.
     * @throws NullPointerException if the specified {@code Bitmap} is {@code null}.
     */
    public void drawBitmap(Bitmap bitmap, int x, int y) {
        drawBitmap(bitmap, x, y, 0, 0, bitmap.getWidth(), bitmap.getHeight());
    }

    /**
     * Draws the passed in {@linkplain Bitmap} onto the {@code Bitmap} used by this {@code BitmapGraphics}. The color
     * read from the source {@code Bitmap} is premultiplied with the specified color before being drawn.
     *
     * @param bitmap the source {@code Bitmap}.
     * @param x      the starting x coordinate in the destination {@code Bitmap}.
     * @param y      the starting y coordinate in the destination {@code Bitmap}.
     * @param x0     the starting x coordinate in the source {@code Bitmap}.
     * @param y0     the starting y coordinate in the source {@code Bitmap}.
     * @param width  the width of the read area in the source {@code Bitmap}.
     * @param height the height of the read area in the source {@code Bitmap}.
     * @param color  the color to premultiply the source {@code Bitmap} with before drawing.
     * @throws NullPointerException if the specified {@code Bitmap} is {@code null}.
     */
    public void drawBitmap(Bitmap bitmap, int x, int y, int x0, int y0, int width, int height, int color) {
        Objects.requireNonNull(bitmap, "bitmap must not be null");

        // PARALLEL VERSION
//        IntStream.range(0, height).parallel().forEach(i -> {
//            if (i + y < 0 || i + y >= this.bitmap.getHeight() || i + y0 >= bitmap.getHeight()) return;
//
//            IntStream.range(0, width).parallel().forEach(j -> {
//                if (j + x < 0 || j + x >= this.bitmap.getWidth() || j + x0 >= bitmap.getWidth()) return;
//
//                int value = bitmap.getPixel(j + x0, i + y0);
//                this.bitmap.setPixel(j + x, i + y, value);
//            });
//        });

        for (int i = 0; i < height; i++) {
            if (i + y < 0 || i + y >= this.bitmap.getHeight() || i + y0 >= bitmap.getHeight()) continue;

            for (int j = 0; j < width; j++) {
                if (j + x < 0 || j + x >= this.bitmap.getWidth() || j + x0 >= bitmap.getWidth()) continue;

                int value = bitmap.getPixel(j + x0, i + y0);
                // TODO: Add Bitmap blending
                if ((value & 0xff000000) != 0) {
                    this.bitmap.setPixel(j + x, i + y, Colors.multiply(value, color));
                }
            }
        }
    }

    /**
     * Draws the passed in {@linkplain Bitmap} onto the {@code Bitmap} used by this {@code BitmapGraphics}. The color
     * read from the source {@code Bitmap} is premultiplied with the specified color before being drawn.
     *
     * @param bitmap the source {@code Bitmap}.
     * @param x      the starting x coordinate in the destination {@code Bitmap}.
     * @param y      the starting y coordinate in the destination {@code Bitmap}.
     * @param color  the color to premultiply the source {@code Bitmap} with before drawing.
     * @throws NullPointerException if the specified {@code Bitmap} is {@code null}.
     */
    public void drawBitmap(Bitmap bitmap, int x, int y, int color) {
        drawBitmap(bitmap, x, y, 0, 0, bitmap.getWidth(), bitmap.getHeight(), color);
    }

    /**
     * Draws a line from start to the end point of the specified color.
     *
     * @param x1    the x coordinate of the starting point.
     * @param y1    the y coordinate of the starting point.
     * @param x2    the x coordinate of the end point.
     * @param y2    the y coordinate of the end point.
     * @param color the color of the line.
     * @see <a href="https://www.javatpoint.com/computer-graphics-dda-algorithm">Reference</a>
     */
    public void drawLine(int x1, int y1, int x2, int y2, int color) {
        // Calculate the differences along the x and y axes
        float dx = x2 - x1;
        float dy = y2 - y1;

        // Don't draw points
        if (dx == 0 && dy == 0) return;

        // Get the number of steps (the larger delta)
        float step = Math.max(Math.abs(dx), Math.abs(dy));
        dx /= step;
        dy /= step;

        float x = x1;
        float y = y1;

        // Draw the line
        for (int i = 0; i <= step; i++) {
            bitmap.setPixel(Math.round(x), Math.round(y), color);
            x += dx;
            y += dy;
        }
    }

    /**
     * Draws a line of the specified color between two specified {@linkplain Vector2f points}.
     *
     * @param p1    the first point.
     * @param p2    the second point.
     * @param color the color of the line.
     * @throws NullPointerException if any of the specified points is {@code null}.
     */
    public void drawLine(Vector2f p1, Vector2f p2, int color) {
        Objects.requireNonNull(p1, "p1 must not be null");
        Objects.requireNonNull(p2, "p2 must not be null");
        drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY(), color);
    }

    /**
     * Draws a triangle by drawing a line of the specified color between each pair of the three specified
     * {@linkplain Vector2f points}.
     *
     * @param v1    the first point.
     * @param v2    the seconds point.
     * @param v3    the third point.
     * @param color the color of the triangle.
     * @throws NullPointerException if any of the specified points is {@code null}.
     */
    public void drawTriangle(Vector2f v1, Vector2f v2, Vector2f v3, int color) {
        drawLine(v1, v2, color);
        drawLine(v2, v3, color);
        drawLine(v1, v3, color);
    }

    /**
     * Draws the specified string at the specified coordinate with the set line spacing and
     * {@linkplain BitmapFont.TextAlignment}.
     *
     * @param str         the string to draw.
     * @param x           the x coordinate.
     * @param y           the y coordinate.
     * @param lineSpacing the space between the lines of text in pixels.
     * @param alignment   the {@code TextAlignment} of the text.
     * @throws NullPointerException if the specified string or {@code TextAlignment} are {@code null}.
     */
    public void drawString(String str, int x, int y, int lineSpacing, BitmapFont.TextAlignment alignment) {
        Objects.requireNonNull(str, "str must not be null");
        Objects.requireNonNull(alignment, "alignment must not be null");

        // Initialize the variables
        int xOffset = 0;
        int newLine = 0;
        int xPos = 0;

        // Set the offset according to the alignment
        switch (alignment) {
            case CENTER -> xOffset = -getFontMetrics().stringWidth(str) / 2;
            case RIGHT -> xOffset = -getFontMetrics().stringWidth(str);
        }

        // Iterate through each character in the specified string
        for (int i = 0; i < str.length(); i++) {
            char currentChar = str.charAt(i);
            if (currentChar == '\n') {
                newLine++;
                xPos = 0;
                continue;
            }

            // Retrieve the Bitmap for the current char
            Bitmap charBitmap = font.getBitmapForChar(currentChar);
            // Skip drawing if no Bitmap was found for this char
            if (charBitmap == null) {
                xPos++;
                continue;
            }

            // Draw the char
            drawBitmap(charBitmap, xOffset + x + xPos * getFontMetrics().getCharacterWidth(),
                    y + newLine * (getFontMetrics().getCharacterHeight() + lineSpacing));
            xPos++;
        }
    }

    /**
     * Draws the specified string at the specified coordinate and
     * {@linkplain BitmapFont.TextAlignment} with the line spacing
     * set to 1.
     *
     * @param str       the string to draw.
     * @param x         the x coordinate.
     * @param y         the y coordinate.
     * @param alignment the {@code TextAlignment} of the text.
     * @throws NullPointerException if the specified string or {@code TextAlignment} are {@code null}.
     */
    public void drawString(String str, int x, int y, BitmapFont.TextAlignment alignment) {
        drawString(str, x, y, 1, alignment);
    }

    /**
     * Draws the specified string at the specified coordinate with the line spacing set to 1 and the
     * {@linkplain BitmapFont.TextAlignment} set to
     * {@linkplain BitmapFont.TextAlignment#LEFT}.
     *
     * @param str the string to draw.
     * @param x   the x coordinate.
     * @param y   the y coordinate.
     * @throws NullPointerException if the specified string is {@code null}.
     */
    public void drawString(String str, int x, int y) {
        drawString(str, x, y, 1, BitmapFont.TextAlignment.LEFT);
    }

    /**
     * Draws the specified string at the specified coordinate with the set line spacing,
     * {@linkplain BitmapFont.TextAlignment} and color.
     *
     * @param str         the string to draw.
     * @param x           the x coordinate.
     * @param y           the y coordinate.
     * @param lineSpacing the space between the lines of text in pixels.
     * @param alignment   the {@code TextAlignment} of the text.
     * @param color       the color of the text.
     * @throws NullPointerException if the specified string or {@code TextAlignment} are {@code null}.
     */
    public void drawString(String str, int x, int y, int lineSpacing, BitmapFont.TextAlignment alignment, int color) {
        Objects.requireNonNull(str, "str must not be null");
        Objects.requireNonNull(alignment, "alignment must not be null");

        // Initialize the variables
        int xOffset = 0;
        int newLine = 0;
        int xPos = 0;

        // Set the offset according to the alignment
        switch (alignment) {
            case CENTER -> xOffset = -getFontMetrics().stringWidth(str) / 2;
            case RIGHT -> xOffset = -getFontMetrics().stringWidth(str);
        }

        // Iterate through each character in the specified string
        for (int i = 0; i < str.length(); i++) {
            char currentChar = str.charAt(i);
            if (currentChar == '\n') {
                newLine++;
                xPos = 0;
                continue;
            }

            // Retrieve the Bitmap for the current char
            Bitmap charBitmap = font.getBitmapForChar(currentChar);
            // Skip drawing if no Bitmap was found for this char
            if (charBitmap == null) {
                xPos++;
                continue;
            }

            // Draw the char
            drawBitmap(charBitmap, xOffset + x + xPos * getFontMetrics().getCharacterWidth(),
                    y + newLine * (getFontMetrics().getCharacterHeight() + lineSpacing), color);
            xPos++;
        }
    }

    /**
     * Draws the specified string at the specified coordinate,
     * {@linkplain BitmapFont.TextAlignment} and color, with the
     * line spacing set to 1.
     *
     * @param str       the string to draw.
     * @param x         the x coordinate.
     * @param y         the y coordinate.
     * @param alignment the {@code TextAlignment} of the text.
     * @param color     the color of the text.
     * @throws NullPointerException if the specified string or {@code TextAlignment} are {@code null}.
     */
    public void drawString(String str, int x, int y, BitmapFont.TextAlignment alignment, int color) {
        drawString(str, x, y, 1, alignment, color);
    }

    /**
     * Draws the specified string at the specified coordinate and color, with the line spacing set to 1 and the
     * {@linkplain BitmapFont.TextAlignment} set to
     * {@linkplain BitmapFont.TextAlignment#LEFT}.
     *
     * @param str   the string to draw.
     * @param x     the x coordinate.
     * @param y     the y coordinate.
     * @param color the color of the text.
     * @throws NullPointerException if the specified string is {@code null}.
     */
    public void drawString(String str, int x, int y, int color) {
        drawString(str, x, y, 1, BitmapFont.TextAlignment.LEFT, color);
    }

    /**
     * Draws the specified string at the specified coordinate with the set line spacing,
     * {@linkplain BitmapFont.TextAlignment}, color, and shadow
     * color.
     *
     * @param str         the string to draw.
     * @param x           the x coordinate.
     * @param y           the y coordinate.
     * @param lineSpacing the space between the lines of text in pixels.
     * @param alignment   the {@code TextAlignment} of the text.
     * @param color       the color of the text.
     * @param shadowColor the color of the shadow.
     * @throws NullPointerException if the specified string or {@code TextAlignment} are {@code null}.
     */
    public void drawString(String str, int x, int y, int lineSpacing, BitmapFont.TextAlignment alignment, int color, int shadowColor) {
        drawString(str, x + 1, y + 1, lineSpacing, alignment, shadowColor);
        drawString(str, x, y, lineSpacing, alignment, color);
    }

    /**
     * Draws the specified string at the specified coordinate,
     * {@linkplain BitmapFont.TextAlignment}, color, and shadow
     * color, with the line spacing set to 1.
     *
     * @param str         the string to draw.
     * @param x           the x coordinate.
     * @param y           the y coordinate.
     * @param alignment   the {@code TextAlignment} of the text.
     * @param color       the color of the text.
     * @param shadowColor the color of the shadow.
     * @throws NullPointerException if the specified string or {@code TextAlignment} are {@code null}.
     */
    public void drawString(String str, int x, int y, BitmapFont.TextAlignment alignment, int color, int shadowColor) {
        drawString(str, x, y, 1, alignment, color, shadowColor);
    }

    /**
     * Draws the specified string at the specified coordinate, color, and shadow color, with the line spacing set to 1
     * and the {@linkplain BitmapFont.TextAlignment} set to
     * {@linkplain BitmapFont.TextAlignment#LEFT}.
     *
     * @param str         the string to draw.
     * @param x           the x coordinate.
     * @param y           the y coordinate.
     * @param color       the color of the text.
     * @param shadowColor the color of the shadow.
     * @throws NullPointerException if the specified string is {@code null}.
     */
    public void drawString(String str, int x, int y, int color, int shadowColor) {
        drawString(str, x, y, 1, BitmapFont.TextAlignment.LEFT, color, shadowColor);
    }

    /**
     * Clears the {@linkplain Bitmap} of this {@code BitmapGraphics} by setting all pixels to the specified color.
     *
     * @param color the color to set all pixels to.
     */
    public void clear(int color) {
        Arrays.fill(bitmap.getPixels(), color);
    }

    /**
     * Clears the {@linkplain Bitmap} of this {@code BitmapGraphics} by setting all pixels to the clear color of this
     * {@code BitmapGraphics}.
     */
    public void clear() {
        clear(clearValue);
    }
}