package com.bartodelini.pixel.math;

/**
 * A utility class providing functions useful mathematical functions.
 *
 * @author Bartolini
 * @version 1.0
 */
public final class MathUtils {

    private MathUtils() {
    }

    /**
     * Returns the modulus of the division of {@code a} by {@code m}.
     *
     * @param a the dividend.
     * @param m the divisor.
     * @return the modulus of the division of {@code a} by {@code m}.
     * @see <a href=https://stackoverflow.com/questions/5385024/mod-in-java-produces-negative-numbers>Reference</a>
     */
    public static float mod(float a, float m) {
        return ((a % m) + m) % m;
    }

    /**
     * Returns the modulus of the division of {@code a} by {@code m}.
     *
     * @param a the dividend.
     * @param m the divisor.
     * @return the modulus of the division of {@code a} by {@code m}.
     * @see <a href=https://stackoverflow.com/questions/5385024/mod-in-java-produces-negative-numbers>Reference</a>
     */
    public static int mod(int a, int m) {
        return ((a % m) + m) % m;
    }

    /**
     * Clamps the specified value between the specified minimum and maximum values.
     *
     * @param val the value to clamp.
     * @param min the minimum value.
     * @param max the maximum value.
     * @return the clamped value.
     */
    public static float clamp(float val, float min, float max) {
        return Math.min(max, Math.max(min, val));
    }

    /**
     * Clamps the specified value between the specified minimum and maximum values.
     *
     * @param val the value to clamp.
     * @param min the minimum value.
     * @param max the maximum value.
     * @return the clamped value.
     */
    public static int clamp(int val, int min, int max) {
        return Math.min(max, Math.max(min, val));
    }

    /**
     * Linearly interpolates between the specified start and target using the specified alpha.
     *
     * @param start  the start value for the interpolation.
     * @param target the target value for the interpolation.
     * @param alpha  the interpolation factor.
     * @return the result of linear interpolation between {@code start} and {@code target} by {@code alpha}.
     */
    public static float lerpFloat(float start, float target, float alpha) {
        return start * (1 - alpha) + target * alpha;
    }
}