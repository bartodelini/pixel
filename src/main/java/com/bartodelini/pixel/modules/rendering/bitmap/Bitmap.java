package com.bartodelini.pixel.modules.rendering.bitmap;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * A <i>Bitmap</i> is used to store an integer array of values representing the pixels of an image in the ARGB scheme.
 *
 * @author Bartolini
 * @version 1.0
 */
public class Bitmap {

    private final int width;
    private final int height;
    private final int[] pixels;

    private BitmapGraphics3D graphics;


    /**
     * Allocates a new {@code Bitmap} by passing in its dimensions, as well as the arrays containing the pixel values.
     *
     * @param width  the width of the {@code Bitmap}.
     * @param height the height of the {@code Bitmap}.
     * @throws IllegalArgumentException if the specified dimensions are less than 1 or do not match the length of the
     *                                  pixel array.
     */
    public Bitmap(int width, int height, int[] pixels) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("both the width and the height have to be at least 1");
        }
        if (pixels.length != width * height) {
            throw new IllegalArgumentException("the width and height do not match the length of the pixel array");
        }
        this.width = width;
        this.height = height;
        this.pixels = pixels;
    }

    /**
     * Allocates a new {@code Bitmap} by passing in its dimensions. This constructor has the same effect as
     * {@linkplain #Bitmap(int, int, int[] pixels) Bitmap} {@code (width, height, new int[width * height])}.
     *
     * @param width  the width of the {@code Bitmap}.
     * @param height the height of the {@code Bitmap}.
     * @throws IllegalArgumentException if the specified dimensions are less than 1.
     */
    public Bitmap(int width, int height) {
        this(width, height, new int[width * height]);
    }

    /**
     * Returns the width of this {@code Bitmap}.
     *
     * @return the width of this {@code Bitmap}.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of this {@code Bitmap}.
     *
     * @return the height of this {@code Bitmap}.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the integer array storing the pixel values of this {@code Bitmap} in the ARGB scheme.
     *
     * @return the integer array storing the pixels of this {@code Bitmap}.
     */
    public int[] getPixels() {
        return pixels;
    }

    /**
     * Returns the pixel value at the specified position.
     *
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     * @return the pixel value at the specified position.
     * @throws IllegalArgumentException if the specified x and y coordinates lie outside this {@code Bitmap}.
     */
    public int getPixel(int x, int y) {
        if (x < 0 || x > getWidth() - 1 || y < 0 || y > getHeight() - 1) {
            throw new IllegalArgumentException(
                    "the specified x and y coordinates (" + x + ", " + y + ") " +
                            "lie outside of this Bitmap (" + getWidth() + "x" + getHeight() + ")");
        }
        return pixels[x + y * width];
    }

    /**
     * Sets the pixel value at the specified position.
     *
     * @param x     the x-coordinate.
     * @param y     the y-coordinate.
     * @param color the new pixel value.
     * @throws IllegalArgumentException if the specified x and y coordinates lie outside this {@code Bitmap}.
     */
    public void setPixel(int x, int y, int color) {
        if (x < 0 || x > getWidth() - 1 || y < 0 || y > getHeight() - 1) {
            throw new IllegalArgumentException(
                    "the specified x and y coordinates (" + x + ", " + y + ") " +
                            "lie outside of this Bitmap (" + getWidth() + "x" + getHeight() + ")");
        }
        pixels[x + y * width] = color;
    }

    /**
     * Writes the specified portion of this {@code Bitmaps} pixels into the specified array.
     *
     * @param x         the start x position to read from.
     * @param y         the start y position to read from.
     * @param width     the width of the read portion.
     * @param height    the height of the read portion.
     * @param data      the data array to write to.
     * @param offset    the offset of the data array at which to start writing.
     * @param scanWidth the scan width of the data array.
     * @throws IllegalArgumentException if the specified x or y coordinates lie outside of this {@code Bitmap}, the
     *                                  width and height are too large for this {@code Bitmap}, or if the specified
     *                                  parameters lead outside the data array.
     */
    public void getPixels(int x, int y, int width, int height, int[] data, int offset, int scanWidth) {
        if (x < 0 || y < 0 || x >= getWidth() || y >= getHeight()) {
            throw new IllegalArgumentException(
                    "the specified x and y coordinates (" + x + ", " + y + ") " +
                            "lie outside of this Bitmap (" + getWidth() + "x" + getHeight() + ")");
        }
        if (x + width > getWidth() || y + height > getHeight()) {
            throw new IllegalArgumentException(
                    "the specified width and height (" + x + " + " + width + ", " + y + " + " + height + ") " +
                            "lead outside this Bitmap (" + getWidth() + "x" + getHeight() + ")");
        }
        if (offset + (width - 1) + (height - 1) * scanWidth >= data.length) {
            throw new IllegalArgumentException("the specified parameters lead outside the data array");
        }
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                data[offset + j + i * scanWidth] = pixels[(x + j) + (y + i) * this.width];
            }
        }
    }

    /**
     * Writes the specified portion of the specified array into the pixels of this {@code Bitmap}.
     *
     * @param x         the start x position to write to.
     * @param y         the start y position to write to.
     * @param width     the width of the written portion.
     * @param height    the height of the written portion.
     * @param data      the data array to read from.
     * @param offset    the offset of the data array at which to start reading.
     * @param scanWidth the scan width of the data array.
     * @throws IllegalArgumentException if the specified x or y coordinates lie outside of this {@code Bitmap}, the
     *                                  width and height are too large for this {@code Bitmap}, or if the specified
     *                                  parameters lead outside the data array.
     */
    public void setPixels(int x, int y, int width, int height, int[] data, int offset, int scanWidth) {
        if (x < 0 || y < 0 || x >= getWidth() || y >= getHeight()) {
            throw new IllegalArgumentException(
                    "the specified x and y coordinates (" + x + ", " + y + ") " +
                            "lie outside of this Bitmap (" + getWidth() + "x" + getHeight() + ")");
        }
        if (x + width > getWidth() || y + height > getHeight()) {
            throw new IllegalArgumentException(
                    "the specified width and height (" + x + " + " + width + ", " + y + " + " + height + ") " +
                            "lead outside this Bitmap (" + getWidth() + "x" + getHeight() + ")");
        }
        if (offset + (width - 1) + (height - 1) * scanWidth >= data.length) {
            throw new IllegalArgumentException("the specified parameters lead outside the data array");
        }
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                pixels[(x + j) + (y + i) * this.width] = data[offset + j + i * scanWidth];
            }
        }
    }

    /**
     * Sets all pixels of this {@code Bitmap} to the specified color.
     *
     * @param color the color to set all pixels to.
     */
    public void setAllPixels(int color) {
        Arrays.fill(pixels, color);
    }

    /**
     * Sets all pixels of this {@code Bitmap} according to the specified {@linkplain Function}.
     *
     * @param function the {@code Function} which will be used to set the pixels.
     * @throws NullPointerException if the specified {@code Function} is {@code null}.
     */
    public void setAllPixels(Function<Integer, Integer> function) {
        Objects.requireNonNull(function, "function must not be null");
        IntStream.range(0, pixels.length).parallel().forEach(i -> pixels[i] = function.apply(pixels[i]));
    }

    /**
     * Returns a cutout {@code Bitmap} from this {@code Bitmap}.
     *
     * @param x      the starting x position of the cutout {@code Bitmap}.
     * @param y      the starting y position of the cutout {@code Bitmap}.
     * @param width  the width of the cutout {@code Bitmap}.
     * @param height the height of the cutout {@code Bitmap}.
     * @return a cutout {@code Bitmap} of this {@code Bitmap}.
     * @throws IllegalArgumentException if the specified x or y coordinates lie outside of this {@code Bitmap}, or the
     *                                  width and height are too large for this {@code Bitmap}.
     */
    public Bitmap getSubBitmap(int x, int y, int width, int height) {
        if (x < 0 || y < 0 || x >= getWidth() || y >= getHeight()) {
            throw new IllegalArgumentException(
                    "the specified x and y coordinates (" + x + ", " + y + ") " +
                            "lie outside of this Bitmap (" + getWidth() + "x" + getHeight() + ")");
        }
        if (x + width > getWidth() || y + height > getHeight()) {
            throw new IllegalArgumentException(
                    "the specified width and height (" + x + " + " + width + ", " + y + " + " + height + ") " +
                            "lead outside this Bitmap (" + getWidth() + "x" + getHeight() + ")");
        }
        Bitmap bitmap = new Bitmap(width, height);
        bitmap.setPixels(0, 0, width, height, pixels, x + y * this.width, this.width);
        return bitmap;
    }

    /**
     * Returns a two-dimensional array of {@code Bitmaps} cut from this {@code Bitmap}.
     *
     * @param bitmapWidth  the width of a single cutout {@code Bitmap}.
     * @param bitmapHeight the height of a single cutout {@code Bitmap}.
     * @return a two-dimensional array of {@code Bitmaps} cut from this {@code Bitmap}.
     * @throws IllegalArgumentException if the specified bitmapWidth or bitmapHeight are less than 1 or are not the
     *                                  respective divisors of the width and height of this {@code Bitmap}.
     */
    public Bitmap[][] getSubBitmaps(int bitmapWidth, int bitmapHeight) {
        if (bitmapWidth <= 0) {
            throw new IllegalArgumentException("bitmapWidth must be positive");
        }
        if (bitmapHeight <= 0) {
            throw new IllegalArgumentException("bitmapHeight must be positive");
        }
        if (getWidth() % bitmapWidth != 0) {
            throw new IllegalArgumentException("bitmapWidth must be a divisor of width");
        }
        if (getHeight() % bitmapHeight != 0) {
            throw new IllegalArgumentException("bitmapHeight must be a divisor of height");
        }

        int bitmapCountX = getWidth() / bitmapWidth;
        int bitmapCountY = getHeight() / bitmapHeight;

        Bitmap[][] bitmaps = new Bitmap[bitmapCountX][bitmapCountY];

        for (int y = 0; y < bitmapCountY; y++) {
            for (int x = 0; x < bitmapCountX; x++) {
                bitmaps[x][y] = getSubBitmap(x * bitmapWidth, y * bitmapHeight, bitmapWidth, bitmapHeight);
            }
        }

        return bitmaps;
    }

    /**
     * Returns the {@linkplain BitmapGraphics} object used to draw to this {@code Bitmap}. The returned
     * {@code BitmapGraphics} instance can be cast to {@linkplain BitmapGraphics3D}.
     *
     * @return the {@code BitmapGraphics} object used to draw to this {@code Bitmap}.
     */
    public BitmapGraphics getGraphics() {
        if (graphics == null) {
            graphics = new BitmapGraphics3D(this);
        }
        return graphics;
    }
}