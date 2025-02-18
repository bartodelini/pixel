package com.bartodelini.pixel.modules.rendering.bitmap;

import com.bartodelini.pixel.math.vector.Vector3f;

import java.util.Objects;

/**
 * A utility class providing useful functions for working of colors.
 *
 * @author Bartolini
 * @version 1.0
 */
public class Colors {

    private Colors() {
    }

    /**
     * Returns an array with the RGB components of the specified color.
     *
     * @param color the color to split into components.
     * @return an array with the RGB components of the specified color.
     */
    public static int[] getRGB(int color) {
        int[] result = new int[3];
        result[0] = color >> 16 & 0xff;
        result[1] = color >> 8 & 0xff;
        result[2] = color & 0xff;
        return result;
    }

    /**
     * Returns an array with the ARGB components of the specified color.
     *
     * @param color the color to split into components.
     * @return an array with the ARGB components of the specified color.
     */
    public static int[] getARGB(int color) {
        int[] result = new int[4];
        result[0] = color >> 24 & 0xff;
        result[1] = color >> 16 & 0xff;
        result[2] = color >> 8 & 0xff;
        result[3] = color & 0xff;
        return result;
    }

    /**
     * Returns an array with the RGB components of the specified color {@linkplain Vector3f}.
     *
     * @param color the color to split into components.
     * @return an array with the RGB components of the specified color {@code Vector3f}.
     * @throws NullPointerException if the specified color {@code Vector3f} is {@code null}.
     */
    public static int[] getRGB(Vector3f color) {
        Objects.requireNonNull(color, "color must not be null");
        int[] result = new int[3];
        result[0] = Math.round(color.getX() * 255);
        result[1] = Math.round(color.getY() * 255);
        result[2] = Math.round(color.getZ() * 255);
        return result;
    }

    /**
     * Returns a color from the specified components.
     *
     * @param r the red component.
     * @param g the green component.
     * @param b the blue component.
     * @return a color from the specified components.
     */
    public static int getColor(int r, int g, int b) {
        return 0xff << 24 | r << 16 | g << 8 | b;
    }

    /**
     * Returns a color from the specified components.
     *
     * @param a the alpha component.
     * @param r the red component.
     * @param g the green component.
     * @param b the blue component.
     * @return a color from the specified components.
     */
    public static int getColor(int a, int r, int g, int b) {
        return a << 24 | r << 16 | g << 8 | b;
    }

    /**
     * Returns a color from the specified {@code integer} array. The array can be of length 3 if the alpha component is
     * to be omitted and of length 4 if the alpha component is to be considered.
     *
     * @param colors the {@code integer} array containing the color components.
     * @return a color from the specified {@code integer} array.
     * @throws IllegalArgumentException if the length of the specified array is less than 3.
     */
    public static int getColor(int[] colors) {
        Objects.requireNonNull(colors, "colors must not be null");
        if (colors.length < 3) {
            throw new IllegalArgumentException("colors length must be at least 3");
        }
        if (colors.length == 3) {
            return getColor(colors[0], colors[1], colors[2]);
        } else {
            return getColor(colors[0], colors[1], colors[2], colors[3]);
        }
    }

    /**
     * Returns a color from the specified color {@linkplain Vector3f}.
     *
     * @param vec the color {@code Vector3f}.
     * @return a color from the specified color {@code Vector3f}.
     * @throws NullPointerException if the specified color {@code Vector3f} is {@code null}.
     */
    public static int getColor(Vector3f vec) {
        Objects.requireNonNull(vec, "vec must not be null");
        return getColor(Math.round(vec.getX() * 255), Math.round(vec.getY() * 255), Math.round(vec.getZ() * 255));
    }

    /**
     * Returns a color {@linkplain Vector3f} from the specified color.
     *
     * @param color the color {@code integer}.
     * @return a color {@code Vector3f} from the specified color.
     */
    public static Vector3f getVector(int color) {
        int[] colors = getRGB(color);
        return new Vector3f(colors[0] / 255.0f, colors[1] / 255.0f, colors[2] / 255.0f);
    }

    /**
     * Returns a color with the RGB components set to the specified brightness.
     *
     * @param brightness the value to set the RGB components to.
     * @return a color with the RGB components set to the specified brightness.
     */
    public static int getGray(int brightness) {
        return getColor(brightness, brightness, brightness);
    }

    /**
     * Returns the specified component of the specified color.
     *
     * @param color the color.
     * @param index the component index.
     * @return the specified component of the specified color.
     */
    public static int getComponent(int color, int index) {
        int[] colors = getARGB(color);
        return colors[index];
    }

    /**
     * Returns the alpha component of the specified color.
     *
     * @param color the color.
     * @return the alpha component of the specified color.
     */
    public static int getAlpha(int color) {
        return getComponent(color, 0);
    }

    /**
     * Returns the red component of the specified color.
     *
     * @param color the color.
     * @return the red component of the specified color.
     */
    public static int getRed(int color) {
        return getComponent(color, 1);
    }

    /**
     * Returns the green component of the specified color.
     *
     * @param color the color.
     * @return the green component of the specified color.
     */
    public static int getGreen(int color) {
        return getComponent(color, 2);
    }

    /**
     * Returns the blue component of the specified color.
     *
     * @param color the color.
     * @return the blue component of the specified color.
     */
    public static int getBlue(int color) {
        return getComponent(color, 3);
    }

    /**
     * Sets the specified component of the specified color.
     *
     * @param color the color.
     * @param index the component to set.
     * @param value the new component value.
     * @return the specified color with the specified component altered.
     */
    public static int setComponent(int color, int index, int value) {
        int[] colors = getARGB(color);
        colors[index] = value;
        return getColor(colors);
    }

    /**
     * Sets the alpha component of the specified color.
     *
     * @param color the color.
     * @param value the new alpha component.
     * @return the specified component with the alpha component altered.
     */
    public static int setAlpha(int color, int value) {
        return setComponent(color, 0, value);
    }

    /**
     * Sets the red component of the specified color.
     *
     * @param color the color.
     * @param value the new red component.
     * @return the specified component with the red component altered.
     */
    public static int setRed(int color, int value) {
        return setComponent(color, 1, value);
    }

    /**
     * Sets the green component of the specified color.
     *
     * @param color the color.
     * @param value the new green component.
     * @return the specified component with the green component altered.
     */
    public static int setGreen(int color, int value) {
        return setComponent(color, 2, value);
    }

    /**
     * Sets the blue component of the specified color.
     *
     * @param color the color.
     * @param value the new blue component.
     * @return the specified component with the blue component altered.
     */
    public static int setBlue(int color, int value) {
        return setComponent(color, 3, value);
    }

    /**
     * Returns a darkened version of the specified color by the specified amount.
     * <a href="https://stackoverflow.com/questions
     * /6615002/given-an-rgb-value-how-do-i-create-a-tint-or-shade">Reference</a>
     *
     * @param color  the color to darken.
     * @param factor the amount to darken the color by.
     * @return a darkened version of the specified color by the sepcified amount.
     */
    public static int darken(int color, float factor) {
        int[] colors = getARGB(color);
        final float oneMinusFactor = 1 - factor;
        colors[1] = Math.round(colors[1] * oneMinusFactor);
        colors[2] = Math.round(colors[2] * oneMinusFactor);
        colors[3] = Math.round(colors[3] * oneMinusFactor);
        return getColor(colors);
    }

    /**
     * Returns a brightened version of the specified color by the specified amount.
     * <a href="https://stackoverflow.com/questions
     * /6615002/given-an-rgb-value-how-do-i-create-a-tint-or-shade">Reference</a>
     *
     * @param color  the color to brighten.
     * @param factor the amount to brighten the color by.
     * @return a brightened version of the specified color by the specified amount.
     */
    public static int brighten(int color, float factor) {
        int[] colors = getARGB(color);
        colors[1] = colors[1] + Math.round((255 - colors[1]) * factor);
        colors[2] = colors[2] + Math.round((255 - colors[2]) * factor);
        colors[3] = colors[3] + Math.round((255 - colors[3]) * factor);
        return getColor(colors);
    }

    /**
     * Returns the multiplication of two specified colors.
     *
     * @param color1 the first color.
     * @param color2 the second color.
     * @return the multiplication of two specified colors.
     */
    public static int multiply(int color1, int color2) {
        int[] colors1 = getARGB(color1);
        int[] colors2 = getRGB(color2);
        colors1[1] = colors1[1] * colors2[0] / 0xff;
        colors1[2] = colors1[2] * colors2[1] / 0xff;
        colors1[3] = colors1[3] * colors2[2] / 0xff;
        return getColor(colors1);
    }

    /**
     * Returns the sum of two specified colors.
     *
     * @param color1 the first color.
     * @param color2 the second color.
     * @return the sum of two specified colors.
     */
    public static int add(int color1, int color2) {
        int[] colors1 = getARGB(color1);
        int[] colors2 = getRGB(color2);
        colors1[1] = Math.min(255, colors1[1] + colors2[0]);
        colors1[2] = Math.min(255, colors1[2] + colors2[1]);
        colors1[3] = Math.min(255, colors1[3] + colors2[2]);
        return getColor(colors1);
    }

    /**
     * Returns the sum of a varargs of colors.
     *
     * @param colors the varargs of colors.
     * @return the sum of a varargs of colors.
     */
    public static int add(int... colors) {
        int result = 0;
        for (int color : colors) {
            result = add(result, color);
        }
        return result;
    }

    /**
     * Returns a difference of two specified colors.
     *
     * @param color1 the first color.
     * @param color2 the second color.
     * @return the difference between the first and the second color.
     */
    public static int subtract(int color1, int color2) {
        int[] colors1 = getARGB(color1);
        int[] colors2 = getRGB(color2);
        colors1[1] = Math.max(0, colors1[1] - colors2[0]);
        colors1[2] = Math.max(0, colors1[2] - colors2[1]);
        colors1[3] = Math.max(0, colors1[3] - colors2[2]);
        return getColor(colors1);
    }

    /**
     * Returns the difference of a varargs of colors.
     *
     * @param colors the varargs of colors.
     * @return the difference of a varargs of colors.
     */
    public static int subtract(int... colors) {
        int result = 0;
        for (int color : colors) {
            result = subtract(result, color);
        }
        return result;
    }

    /**
     * Returns a color with random RGB components.
     *
     * @return a color with random RGB components.
     */
    public static int randomRGB() {
        return getColor(
                (int) Math.round(Math.random() * 255),
                (int) Math.round(Math.random() * 255),
                (int) Math.round(Math.random() * 255));
    }

    /**
     * Returns a color with random ARGB components.
     *
     * @return a color with random ARGB components.
     */
    public static int randomARGB() {
        return getColor(
                (int) Math.round(Math.random() * 255),
                (int) Math.round(Math.random() * 255),
                (int) Math.round(Math.random() * 255),
                (int) Math.round(Math.random() * 255));
    }

    /**
     * Returns the RGB component interpolation of two specified colors by the specified factor alpha.
     *
     * @param start  the start color.
     * @param target the target color.
     * @param alpha  the interpolation factor.
     * @return the interpolated color.
     */
    public static int interpolateRGB(int start, int target, float alpha) {
        int[] colors1 = getARGB(start);
        int[] colors2 = getARGB(target);
        colors1[1] = Math.round(colors1[1] * (1 - alpha) + colors2[1] * alpha);
        colors1[2] = Math.round(colors1[2] * (1 - alpha) + colors2[2] * alpha);
        colors1[3] = Math.round(colors1[3] * (1 - alpha) + colors2[3] * alpha);
        return getColor(colors1);
    }

    /**
     * Returns the ARGB component interpolation of two specified colors by the specified factor alpha.
     *
     * @param start  the start color.
     * @param target the target color.
     * @param alpha  the interpolation factor.
     * @return the interpolated color.
     */
    public static int interpolateARGB(int start, int target, float alpha) {
        int[] colors1 = getARGB(start);
        int[] colors2 = getARGB(target);
        colors1[0] = Math.round(colors1[0] * (1 - alpha) + colors2[0] * alpha);
        colors1[1] = Math.round(colors1[1] * (1 - alpha) + colors2[1] * alpha);
        colors1[2] = Math.round(colors1[2] * (1 - alpha) + colors2[2] * alpha);
        colors1[3] = Math.round(colors1[3] * (1 - alpha) + colors2[3] * alpha);
        return getColor(colors1);
    }
}