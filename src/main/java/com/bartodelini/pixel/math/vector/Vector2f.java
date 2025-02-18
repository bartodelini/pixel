package com.bartodelini.pixel.math.vector;

import java.util.Objects;

/**
 * A <i>Vector2f</i> is a two-dimensional vector with components of type {@code float}.
 *
 * @author Bartolini
 * @version 1.0
 */
public class Vector2f {

    /**
     * A vector with all components set to 0.
     */
    public static final Vector2f ZERO = new Vector2f(0);

    /**
     * A vector with all components set to 1.
     */
    public static final Vector2f ONE = new Vector2f(1);

    /**
     * A unit vector with the second component set to 1.
     */
    public static final Vector2f UP = new Vector2f(0, 1);

    /**
     * A unit vector with the second component set to -1.
     */
    public static final Vector2f DOWN = new Vector2f(0, -1);

    /**
     * A unit vector with the first component set to -1.
     */
    public static final Vector2f LEFT = new Vector2f(-1, 0);

    /**
     * A unit vector with the first component set to 1.
     */
    public static final Vector2f RIGHT = new Vector2f(1, 0);

    private final float x;
    private final float y;

    /**
     * Allocates a new {@code Vector2f} object by passing in all its components.
     *
     * @param x the first component.
     * @param y the second component.
     */
    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Allocates a new {@code Vector2f} object by passing in an array of its components.
     *
     * @param components the array containing the components.
     * @throws IllegalArgumentException if {@code components} is not of length 2.
     */
    public Vector2f(float[] components) {
        if (components.length != 2) {
            throw new IllegalArgumentException("components must be of length 2");
        }
        this.x = components[0];
        this.y = components[1];
    }

    /**
     * Allocates a new {@code Vector2f} object by passing in a single component,
     * which will be used for all its components. This constructor has
     * the same effect as {@linkplain #Vector2f(float, float) Vector2f}
     * {@code (f, f)}.
     *
     * @param f the value used for all components.
     */
    public Vector2f(float f) {
        this(f, f);
    }

    /**
     * Allocates a new {@code Vector2f} object by passing in a {@code Vector4f} instance
     * and only using its x and y components.
     *
     * @param vec the {@code Vector4f} object used to instantiate this {@code Vector2f}.
     * @throws NullPointerException if {@code vec} is {@code null}.
     */
    public Vector2f(Vector4f vec) {
        Objects.requireNonNull(vec, "vec must not be null");
        this.x = vec.getX();
        this.y = vec.getY();
    }

    /**
     * Allocates a new {@code Vector2f} object by passing in a {@code Vector3f} instance
     * and only using its x and y components.
     *
     * @param vec the {@code Vector3f} object used to instantiate this {@code Vector2f}.
     * @throws NullPointerException if {@code vec} is {@code null}.
     */
    public Vector2f(Vector3f vec) {
        Objects.requireNonNull(vec, "vec must not be null");
        this.x = vec.getX();
        this.y = vec.getY();
    }

    /**
     * Allocates a new {@code Vector2f} object by passing in a {@code Vector2f}
     * instance.
     *
     * @param vec the {@code Vector2f} object used to instantiate this {@code Vector2f}.
     * @throws NullPointerException if {@code vec} is {@code null}.
     */
    public Vector2f(Vector2f vec) {
        Objects.requireNonNull(vec, "vec must not be null");
        this.x = vec.getX();
        this.y = vec.getY();
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
        Objects.requireNonNull(start, "start must not be null");
        Objects.requireNonNull(target, "target must not be null");
        return start.add(target.subtract(start).scale(alpha));
    }

    /**
     * Linearly interpolates between this {@code Vector2f} and {@code target} by {@code alpha} and returns the result.
     *
     * @param target The target {@code Vector2f}.
     * @param alpha  The interpolation coefficient.
     * @return The result of linear interpolation between this {@code Vector2f} and {@code target} by {@code alpha}.
     */
    public Vector2f lerp(Vector2f target, float alpha) {
        Objects.requireNonNull(target, "target must not be null");
        return add(target.subtract(this).scale(alpha));
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
        Objects.requireNonNull(incident, "incident must not be null");
        Objects.requireNonNull(normal, "normal must not be null");
        return incident.subtract(normal.scale(2 * incident.dot(normal)));
    }

    /**
     * Reflects this {@code Vector2f} off the plane defined by a normal.
     * <p>
     * Note: The specified normal should be normalized in order to achieve the desired result.
     *
     * @param normal the normal {@code Vector2f} of the plane.
     * @return the reflected {@code Vector2f}.
     * @throws NullPointerException if the specified normal {@code Vector2f} is {@code null}.
     */
    public Vector2f reflect(Vector2f normal) {
        Objects.requireNonNull(normal, "normal must not be null");
        return subtract(normal.scale(2 * dot(normal)));
    }

    /**
     * Returns the first component of this {@code Vector2f}.
     *
     * @return the first component of this {@code Vector2f}.
     */
    public float getX() {
        return x;
    }

    /**
     * Returns the second component of this {@code Vector2f}.
     *
     * @return the second component of this {@code Vector2f}.
     */
    public float getY() {
        return y;
    }

    /**
     * Returns the component at the specified index.
     *
     * @param componentIndex the index of the component to be returned.
     * @return the component at the specified index.
     * @throws IndexOutOfBoundsException if the specified index was not between 0 and 1.
     */
    public float get(int componentIndex) {
        return switch (componentIndex) {
            case 0 -> getX();
            case 1 -> getY();
            default -> throw new IndexOutOfBoundsException("index must be between 0 and 1");
        };
    }

    /**
     * Returns the components of this {@code Vector2f} as an array.
     *
     * @return the components of this {@code Vector2f} as an array.
     */
    public float[] toArray() {
        return new float[]{getX(), getY()};
    }

    /**
     * Returns the sum of this {@code Vector2f} and the passed in {@code Vector2f}.
     * <p>
     * Note: This method leaves the current {@code Vector2f} instance unchanged.
     *
     * @param vec the {@code Vector2f} to add to this {@code Vector2f} instance.
     * @return the sum of this {@code Vector2f} and the passed in {@code Vector2f}.
     * @throws NullPointerException if {@code vec} is {@code null}.
     */
    public Vector2f add(Vector2f vec) {
        Objects.requireNonNull(vec, "vec must not be null");
        return new Vector2f(getX() + vec.getX(), getY() + vec.getY());
    }

    /**
     * Returns the difference of this {@code Vector2f} and the passed in {@code Vector2f}.
     * <p>
     * Note: This method leaves the current {@code Vector2f} instance unchanged.
     *
     * @param vec the {@code Vector2f} to subtract from this {@code Vector2f} instance.
     * @return the difference of this {@code Vector2f} and the passed in {@code Vector2f}.
     * @throws NullPointerException if {@code vec} is {@code null}.
     */
    public Vector2f subtract(Vector2f vec) {
        Objects.requireNonNull(vec, "vec must not be null");
        return new Vector2f(getX() - vec.getX(), getY() - vec.getY());
    }

    /**
     * Returns a copy of the current {@code Vector2f} scaled by the specified factor.
     * <p>
     * Note: This method leaves the current {@code Vector2f} instance unchanged.
     *
     * @param factor the factor to scale the {@code Vector2f} by.
     * @return a copy of the current {@code Vector2f} scaled by the specified factor.
     */
    public Vector2f scale(float factor) {
        return new Vector2f(getX() * factor, getY() * factor);
    }

    /**
     * Returns a negated copy of the current {@code Vector2f}.
     * This method has the same effect as
     * {@linkplain #scale(float) scale} {@code (-1)}.
     * <p>
     * Note: This method leaves the current {@code Vector2f} instance unchanged.
     *
     * @return a negated copy of the current {@code Vector2f}.
     */
    public Vector2f negate() {
        return scale(-1);
    }

    /**
     * Returns the dot product of the current {@code Vector2f} instance and the given
     * {@code Vector2f}.
     *
     * @param vec the {@code Vector2f} to calculate the dot product with.
     * @return the dot product of the current {@code Vector2f} instance and {@code vec}.
     * @throws NullPointerException if {@code vec} is {@code null}.
     */
    public float dot(Vector2f vec) {
        Objects.requireNonNull(vec, "vec must not be null");
        return getX() * vec.getX() + getY() * vec.getY();
    }

    /**
     * Returns the length (euclidean norm) of this {@code Vector2f}.
     *
     * @return the length (euclidean norm) of this {@code Vector2f}.
     */
    public float length() {
        return (float) Math.sqrt(getX() * getX() + getY() * getY());
    }

    /**
     * Returns a normalized {@code Vector2f} instance of the current vector, by
     * scaling the vector with the reciprocal of its length.
     * In case the length is equal to 0, returns this vector instead.
     *
     * @return a normalized copy of this {@code Vector2f} instance, or this {@code Vector2f} if its length is 0.
     */
    public Vector2f normalize() {
        final float length = length();
        if (length == 0) {
            return this;
        }
        return scale(1 / length());
    }

    /**
     * Returns the distance from the current {@code Vector2f} instance to the given
     * {@code Vector2f}.
     *
     * @param vec the {@code Vector2f} to calculate the distance to.
     * @return the distance from this {@code Vector2f} to {@code vec}.
     * @throws NullPointerException if {@code vec} is {@code null}.
     */
    public float distance(Vector2f vec) {
        Objects.requireNonNull(vec, "vec must not be null");
        float dx = vec.getX() - getX();
        float dy = vec.getY() - getY();
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vector2f vector2f)) return false;
        return Float.compare(vector2f.getX(), getX()) == 0
                && Float.compare(vector2f.getY(), getY()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }

    @Override
    public String toString() {
        return "(" + getX() + ", " + getY() + ")";
    }
}
