package com.bartodelini.pixel.math.vector;

import java.util.Objects;

/**
 * A <i>Vector4f</i> represents a four-dimensional vector with components of type {@code float}.
 *
 * @author Bartolini
 * @version 1.0
 */
public class Vector4f {

    /**
     * A vector with all components set to 0.
     */
    public static final Vector4f ZERO = new Vector4f(0);

    /**
     * A vector with all components set to 1.
     */
    public static final Vector4f ONE = new Vector4f(1);

    /**
     * A unit vector with the third component set to 1.
     */
    public static final Vector4f FRONT = new Vector4f(0, 0, 1, 0);

    /**
     * A unit vector with the third component set to -1.
     */
    public static final Vector4f BACK = new Vector4f(0, 0, -1, 0);

    /**
     * A unit vector with the second component set to 1.
     */
    public static final Vector4f UP = new Vector4f(0, 1, 0, 0);

    /**
     * A unit vector with the second component set to -1.
     */
    public static final Vector4f DOWN = new Vector4f(0, -1, 0, 0);

    /**
     * A unit vector with the first component set to -1.
     */
    public static final Vector4f LEFT = new Vector4f(-1, 0, 0, 0);

    /**
     * A unit vector with the first component set to 1.
     */
    public static final Vector4f RIGHT = new Vector4f(1, 0, 0, 0);

    private final float x;
    private final float y;
    private final float z;
    private final float w;

    /**
     * Allocates a new {@code Vector4f} object by passing in all its components.
     *
     * @param x the first component of the {@code Vector4f}.
     * @param y the second component of the {@code Vector4f}.
     * @param z the third component of the {@code Vector4f}.
     * @param w the fourth component of the {@code Vector4f}.
     */
    public Vector4f(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    /**
     * Allocates a new {@code Vector4f} object by passing in an array of its components.
     *
     * @param components the array containing the components.
     * @throws IllegalArgumentException if {@code components} is not of length 4.
     */
    public Vector4f(float[] components) {
        if (components.length != 4) {
            throw new IllegalArgumentException("a must be of length 4");
        }
        this.x = components[0];
        this.y = components[1];
        this.z = components[2];
        this.w = components[3];
    }

    /**
     * Allocates a new {@code Vector4f} object by passing in a single component,
     * which will be used for all its components. This constructor has
     * the same effect as {@linkplain #Vector4f(float, float, float, float)
     * Vector4f} {@code (f, f, f, f)}.
     *
     * @param f the value used for all components.
     */
    public Vector4f(float f) {
        this(f, f, f, f);
    }

    /**
     * Allocates a new {@code Vector4f} object by passing in a {@code Vector4f}
     * instance.
     *
     * @param vec the {@code Vector4f} object used to instantiate this {@code Vector4f}.
     * @throws NullPointerException if {@code vec} is {@code null}.
     */
    public Vector4f(Vector4f vec) {
        Objects.requireNonNull(vec, "vec must not be null");
        this.x = vec.getX();
        this.y = vec.getY();
        this.z = vec.getZ();
        this.w = vec.getW();
    }

    /**
     * Allocates a new {@code Vector4f} object by passing in a {@code Vector3f}
     * instance and a {@code float} value for the w-component.
     *
     * @param vec the {@code Vector3f} object used to instantiate this {@code Vector4f}.
     * @param w   the value for the w-component.
     * @throws NullPointerException if {@code vec} is {@code null}.
     */
    public Vector4f(Vector3f vec, float w) {
        Objects.requireNonNull(vec, "vec must not be null");
        this.x = vec.getX();
        this.y = vec.getY();
        this.z = vec.getZ();
        this.w = w;
    }

    /**
     * Allocates a new {@code Vector4f} object by passing in a {@code Vector3f}
     * instance. The w-component of the resulting {@code Vector4f}
     * be set to 0. This constructor has the same effect as
     * {@linkplain #Vector4f(Vector3f, float) Vector4f}
     * {@code (vec, 0)}.
     *
     * @param vec the {@code Vector3f} object used to instantiate this {@code Vector4f}.
     * @throws NullPointerException if {@code vec} is {@code null}.
     */
    public Vector4f(Vector3f vec) {
        this(vec, 0);
    }

    /**
     * Allocates a new {@code Vector4f} object by passing in a {@code Vector2f}
     * instance and the {@code float} values for the z and w components.
     *
     * @param vec the {@code Vector2f} object used to instantiate this {@code Vector4f}.
     * @param z   the value for the z-component.
     * @param w   the value for the w-component.
     * @throws NullPointerException if {@code vec} is {@code null}.
     */
    public Vector4f(Vector2f vec, float z, float w) {
        Objects.requireNonNull(vec, "vec must not be null");
        this.x = vec.getX();
        this.y = vec.getY();
        this.z = z;
        this.w = w;
    }

    /**
     * Allocates a new {@code Vector4f} object by passing in a {@code Vector2f}
     * instance. The z and w components of the resulting {@code Vector4f}
     * be set to 0. This constructor has the same effect as
     * {@linkplain #Vector4f(Vector2f, float, float) Vector4f}
     * {@code (vec, 0, 0)}.
     *
     * @param vec the {@code Vector2f} object used to instantiate this {@code Vector4f}.
     * @throws NullPointerException if {@code vec} is {@code null}.
     */
    public Vector4f(Vector2f vec) {
        this(vec, 0, 0);
    }

    /**
     * Linearly interpolates between {@code start} and {@code target} by {@code alpha} and returns the result.
     *
     * @param start  The start {@code Vector4f}.
     * @param target The target {@code Vector4f}.
     * @param alpha  The interpolation coefficient.
     * @return the result of linear interpolation between {@code start} and {@code target} by {@code alpha}.
     */
    public static Vector4f lerp(Vector4f start, Vector4f target, float alpha) {
        Objects.requireNonNull(start, "start must not be null");
        Objects.requireNonNull(target, "target must not be null");
        return start.add(target.subtract(start).scale(alpha));
    }

    /**
     * Linearly interpolates between this {@code Vector4f} and {@code target} by {@code alpha} and returns the result.
     *
     * @param target The target {@code Vector4f}.
     * @param alpha  The interpolation coefficient.
     * @return the result of linear interpolation between this {@code Vector4f} and {@code target} by {@code alpha}.
     */
    public Vector4f lerp(Vector4f target, float alpha) {
        Objects.requireNonNull(target, "target must not be null");
        return add(target.subtract(this).scale(alpha));
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
        Objects.requireNonNull(incident, "incident must not be null");
        Objects.requireNonNull(normal, "normal must not be null");
        return incident.subtract(normal.scale(2 * incident.dot(normal)));
    }

    /**
     * Reflects this {@code Vector4f} off the plane defined by a normal.
     * <p>
     * Note: The specified normal should be normalized in order to achieve the desired result.
     *
     * @param normal the normal {@code Vector4f} of the plane.
     * @return the reflected {@code Vector4f}.
     * @throws NullPointerException if the specified normal {@code Vector4f} is {@code null}.
     */
    public Vector4f reflect(Vector4f normal) {
        Objects.requireNonNull(normal, "normal must not be null");
        return subtract(normal.scale(2 * dot(normal)));
    }

    /**
     * Returns the first component of this {@code Vector4f}.
     *
     * @return the first component of this {@code Vector4f}.
     */
    public float getX() {
        return x;
    }

    /**
     * Returns the second component of this {@code Vector4f}.
     *
     * @return the second component of this {@code Vector4f}.
     */
    public float getY() {
        return y;
    }

    /**
     * Returns the third component of this {@code Vector4f}.
     *
     * @return the third component of this {@code Vector4f}.
     */
    public float getZ() {
        return z;
    }

    /**
     * Returns the fourth component of this {@code Vector4f}.
     *
     * @return the fourth component of this {@code Vector4f}.
     */
    public float getW() {
        return w;
    }

    /**
     * Returns the component at the specified index.
     *
     * @param componentIndex the index of the component to be returned.
     * @return the component at the specified index.
     * @throws IndexOutOfBoundsException if the specified index was not between 0 and 3.
     */
    public float get(int componentIndex) {
        return switch (componentIndex) {
            case 0 -> getX();
            case 1 -> getY();
            case 2 -> getZ();
            case 3 -> getW();
            default -> throw new IndexOutOfBoundsException("index must be between 0 and 3");
        };
    }

    /**
     * Returns the components of this {@code Vector4f} as an array.
     *
     * @return the components of this {@code Vector4f} as an array.
     */
    public float[] toArray() {
        return new float[]{getX(), getY(), getZ(), getW()};
    }

    /**
     * Returns the sum of this {@code Vector4f} and the passed in {@code Vector4f}.
     * <p>
     * Note: This method leaves the current {@code Vector4f} instance unchanged.
     *
     * @param vec the {@code Vector4f} to add to this {@code Vector4f} instance.
     * @return the sum of this {@code Vector4f} and the passed in {@code Vector4f}.
     * @throws NullPointerException if {@code vec} is {@code null}.
     */
    public Vector4f add(Vector4f vec) {
        Objects.requireNonNull(vec, "vec must not be null");
        return new Vector4f(getX() + vec.getX(), getY() + vec.getY(), getZ() + vec.getZ(),
                getW() + vec.getW());
    }

    /**
     * Returns the difference of this {@code Vector4f} and the passed in {@code Vector4f}.
     * <p>
     * Note: This method leaves the current {@code Vector4f} instance unchanged.
     *
     * @param vec the {@code Vector4f} to subtract from this {@code Vector4f} instance.
     * @return the difference of this {@code Vector4f} and the passed in {@code Vector4f}.
     * @throws NullPointerException if {@code vec} is {@code null}.
     */
    public Vector4f subtract(Vector4f vec) {
        Objects.requireNonNull(vec, "vec must not be null");
        return new Vector4f(getX() - vec.getX(), getY() - vec.getY(), getZ() - vec.getZ(),
                getW() - vec.getW());
    }

    /**
     * Returns a copy of the current {@code Vector4f} scaled by the specified factor.
     * <p>
     * Note: This method leaves the current {@code Vector4f} instance unchanged.
     *
     * @param factor the factor to scale the {@code Vector4f} by.
     * @return a copy of the current {@code Vector4f} scaled by the specified factor.
     */
    public Vector4f scale(float factor) {
        return new Vector4f(getX() * factor, getY() * factor, getZ() * factor, getW() * factor);
    }

    /**
     * Returns a negated copy of the current {@code Vector4f}.
     * This method has the same effect as
     * {@linkplain #scale(float) scale} {@code (-1)}.
     * <p>
     * Note: This method leaves the current {@code Vector4f} instance unchanged.
     *
     * @return a negated copy of the current {@code Vector4f}.
     */
    public Vector4f negate() {
        return scale(-1);
    }

    /**
     * Returns the dot product of the current {@code Vector4f} instance and the given
     * {@code Vector3f}.
     *
     * @param vec the {@code Vector4f} to calculate the dot product with.
     * @return the dot product of the current {@code Vector4f} instance and {@code vec}.
     * @throws NullPointerException if {@code vec} is {@code null}.
     */
    public float dot(Vector4f vec) {
        Objects.requireNonNull(vec, "vec must not be null");
        return getX() * vec.getX() + getY() * vec.getY() + getZ() * vec.getZ() + getW() * vec.getW();
    }

    /**
     * Returns the length (euclidean norm) of this {@code Vector4f}.
     *
     * @return the length (euclidean norm) of this {@code Vector4f}.
     */
    public float length() {
        return (float) Math.sqrt(getX() * getX() + getY() * getY() + getZ() * getZ() + getW() * getW());
    }

    /**
     * Returns a normalized {@code Vector4f} instance of the current vector, by
     * scaling the vector with the reciprocal of its length.
     * In case the norm is equal to 0, returns a copy of this vector instead.
     *
     * @return a normalized copy of this {@code Vector4f} instance, or this {@code Vector4f} if its length is 0.
     */
    public Vector4f normalize() {
        float length = length();
        if (length == 0) {
            return this;
        }
        return scale(1 / length());
    }

    /**
     * Returns the distance from the current {@code Vector4f} instance to the given
     * {@code Vector4f}.
     *
     * @param vec the {@code Vector4f} to calculate the distance to.
     * @return the distance from this {@code Vector4d} to {@code vec}.
     * @throws NullPointerException if {@code vec} is {@code null}.
     */
    public float distance(Vector4f vec) {
        Objects.requireNonNull(vec, "vec must not be null");
        float dx = vec.getX() - getX();
        float dy = vec.getY() - getY();
        float dz = vec.getZ() - getZ();
        float dw = vec.getW() - getW();
        return (float) Math.sqrt(dx * dx + dy * dy + dz * dz + dw * dw);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vector4f vector4f)) return false;
        return Float.compare(vector4f.getX(), getX()) == 0
                && Float.compare(vector4f.getY(), getY()) == 0
                && Float.compare(vector4f.getZ(), getZ()) == 0
                && Float.compare(vector4f.getW(), getW()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY(), getZ(), getW());
    }

    @Override
    public String toString() {
        return "(" + getX() + ", " + getY() + ", " + getZ() + ", " + getW() + ")";
    }
}
