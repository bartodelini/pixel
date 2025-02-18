package com.bartodelini.pixel.math.vector;

/**
 * A utility class providing functions useful for common operations on vectors.
 *
 * @author Bartolini
 * @version 1.0
 */
public final class Vectors {

    private Vectors() {
    }

    /**
     * Linearly interpolates between {@code start} and {@code target} by {@code alpha} and returns the result.
     *
     * @param start  The start {@code Vector2f}.
     * @param target The target {@code Vector2f}.
     * @param alpha  The interpolation coefficient.
     * @return The result of linear interpolation between {@code start} and {@code target} by {@code alpha}.
     */
    public static Vector2f lerp(Vector2f start, Vector2f target, float alpha) {
        return Vector2f.lerp(start, target, alpha);
    }

    /**
     * Linearly interpolates between {@code start} and {@code target} by {@code alpha} and returns the result.
     *
     * @param start  The start {@code Vector3f}.
     * @param target The target {@code Vector3f}.
     * @param alpha  The interpolation coefficient.
     * @return The result of linear interpolation between {@code start} and {@code target} by {@code alpha}.
     */
    public static Vector3f lerp(Vector3f start, Vector3f target, float alpha) {
        return Vector3f.lerp(start, target, alpha);
    }

    /**
     * Linearly interpolates between {@code start} and {@code target} by {@code alpha} and returns the result.
     *
     * @param start  The start {@code Vector4f}.
     * @param target The target {@code Vector4f}.
     * @param alpha  The interpolation coefficient.
     * @return The result of linear interpolation between {@code start} and {@code target} by {@code alpha}.
     */
    public static Vector4f lerp(Vector4f start, Vector4f target, float alpha) {
        return Vector4f.lerp(start, target, alpha);
    }

    /**
     * Reflects an incident {@code Vector2f} off the plane defined by a normal.
     * <p>
     * Note: The specified normal should be normalized in order to achieve the desired result.
     *
     * @param incident the incident {@code Vector2f}.
     * @param normal   the normal {@code Vector2f} of the plane.
     * @return the reflected {@code Vector2f}.
     * @throws NullPointerException if the specified incident or normal {@code Vector2f} are {@code null}.
     */
    public static Vector2f reflect(Vector2f incident, Vector2f normal) {
        return Vector2f.reflect(incident, normal);
    }

    /**
     * Reflects an incident {@code Vector3f} off the plane defined by a normal.
     * <p>
     * Note: The specified normal should be normalized in order to achieve the desired result.
     *
     * @param incident the incident {@code Vector3f}.
     * @param normal   the normal {@code Vector3f} of the plane.
     * @return the reflected {@code Vector3f}.
     * @throws NullPointerException if the specified incident or normal {@code Vector3f} are {@code null}.
     */
    public static Vector3f reflect(Vector3f incident, Vector3f normal) {
        return Vector3f.reflect(incident, normal);
    }

    /**
     * Reflects an incident {@code Vector4f} off the plane defined by a normal.
     * <p>
     * Note: The specified normal should be normalized in order to achieve the desired result.
     *
     * @param incident the incident {@code Vector4f}.
     * @param normal   the normal {@code Vector4f} of the plane.
     * @return the reflected {@code Vector4f}.
     * @throws NullPointerException if the specified incident or normal {@code Vector4f} are {@code null}.
     */
    public static Vector4f reflect(Vector4f incident, Vector4f normal) {
        return Vector4f.reflect(incident, normal);
    }
}