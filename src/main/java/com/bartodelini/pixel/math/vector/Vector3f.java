package com.bartodelini.pixel.math.vector;

import java.util.Objects;

/**
 * A <i>Vector3f</i> is a three-dimensional vector with components of type {@code float}.
 *
 * @author Bartolini
 * @version 1.0
 */
public class Vector3f {

    /**
     * A vector with all components set to 0.
     */
    public static final Vector3f ZERO = new Vector3f(0);

    /**
     * A vector with all components set to 1.
     */
    public static final Vector3f ONE = new Vector3f(1);

    /**
     * A unit vector with the third component set to 1.
     */
    public static final Vector3f FRONT = new Vector3f(0, 0, 1);

    /**
     * A unit vector with the third component set to -1.
     */
    public static final Vector3f BACK = new Vector3f(0, 0, -1);

    /**
     * A unit vector with the second component set to 1.
     */
    public static final Vector3f UP = new Vector3f(0, 1, 0);

    /**
     * A unit vector with the second component set to -1.
     */
    public static final Vector3f DOWN = new Vector3f(0, -1, 0);

    /**
     * A unit vector with the first component set to -1.
     */
    public static final Vector3f LEFT = new Vector3f(-1, 0, 0);

    /**
     * A unit vector with the first component set to 1.
     */
    public static final Vector3f RIGHT = new Vector3f(1, 0, 0);

    private final float x;
    private final float y;
    private final float z;

    /**
     * Allocates a new {@code Vector3f} object by passing in all its components.
     *
     * @param x the first component.
     * @param y the second component.
     * @param z the third component.
     */
    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Allocates a new {@code Vector3f} object by passing in an array of its components.
     *
     * @param components the array containing the components.
     * @throws IllegalArgumentException if {@code components} is not of length 3.
     */
    public Vector3f(float[] components) {
        if (components.length != 3) {
            throw new IllegalArgumentException("a must be of length 3");
        }
        this.x = components[0];
        this.y = components[1];
        this.z = components[2];
    }


    /**
     * Allocates a new {@code Vector3f} object by passing in a single component,
     * which will be used for all its components. This constructor has
     * the same effect as {@linkplain #Vector3f(float, float, float) Vector3f}
     * {@code (f, f, f)}.
     *
     * @param f the value used for all components.
     */
    public Vector3f(float f) {
        this(f, f, f);
    }

    /**
     * Allocates a new {@code Vector3f} object by passing a {@code Vector4f} instance
     * and only using its x and y components.
     *
     * @param vec the {@code Vector4f} object used to instantiate this {@code Vector3f}.
     * @throws NullPointerException if {@code vec} is {@code null}.
     */
    public Vector3f(Vector4f vec) {
        Objects.requireNonNull(vec, "vec must not be null");
        this.x = vec.getX();
        this.y = vec.getY();
        this.z = vec.getZ();
    }

    /**
     * Allocates a new {@code Vector3f} object by passing in a {@code Vector3f}
     * instance.
     *
     * @param vec the {@code Vector3f} object used to instantiate this {@code Vector3f}.
     * @throws NullPointerException if {@code vec} is {@code null}.
     */
    public Vector3f(Vector3f vec) {
        Objects.requireNonNull(vec, "vec must not be null");
        this.x = vec.getX();
        this.y = vec.getY();
        this.z = vec.getZ();
    }

    /**
     * Allocates a new {@code Vector3f} object by passing in a {@code Vector2f}
     * instance and a {@code float} value for the z-component.
     *
     * @param vec the {@code Vector2f} object used to instantiate this {@code Vector3f}.
     * @param z   the value for the z-component.
     * @throws NullPointerException if {@code vec} is {@code null}.
     */
    public Vector3f(Vector2f vec, float z) {
        Objects.requireNonNull(vec, "vec must not be null");
        this.x = vec.getX();
        this.y = vec.getY();
        this.z = z;
    }

    /**
     * Allocates a new {@code Vector3f} object by passing in a {@code Vector2f}
     * instance. The z-component will of the resulting {@code Vector3f}
     * be set to 0. This constructor has the same effect as
     * {@linkplain #Vector3f(Vector2f, float) Vector3f}
     * {@code (vec, 0)}.
     *
     * @param vec the {@code Vector2f} object used to instantiate this {@code Vector3f}.
     * @throws NullPointerException if {@code vec} is {@code null}.
     */
    public Vector3f(Vector2f vec) {
        this(vec, 0);
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
        Objects.requireNonNull(start, "start must not be null");
        Objects.requireNonNull(target, "target must not be null");
        return start.add(target.subtract(start).scale(alpha));
    }

    /**
     * Linearly interpolates between this {@code Vector3f} and {@code target} by {@code alpha} and returns the result.
     *
     * @param target The target {@code Vector3f}.
     * @param alpha  The interpolation coefficient.
     * @return The result of linear interpolation between this {@code Vector3f} and {@code target} by {@code alpha}.
     */
    public Vector3f lerp(Vector3f target, float alpha) {
        Objects.requireNonNull(target, "target must not be null");
        return add(target.subtract(this).scale(alpha));
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
        Objects.requireNonNull(incident, "incident must not be null");
        Objects.requireNonNull(normal, "normal must not be null");
        return incident.subtract(normal.scale(2 * incident.dot(normal)));
    }

    /**
     * Reflects this {@code Vector3f} off the plane defined by a normal.
     * <p>
     * Note: The specified normal should be normalized in order to achieve the desired result.
     *
     * @param normal the normal {@code Vector3f} of the plane.
     * @return the reflected {@code Vector3f}.
     * @throws NullPointerException if the specified normal {@code Vector3f} is {@code null}.
     */
    public Vector3f reflect(Vector3f normal) {
        Objects.requireNonNull(normal, "normal must not be null");
        return subtract(normal.scale(2 * dot(normal)));
    }

    /**
     * Returns the first component of this {@code Vector3f}.
     *
     * @return the first component of this {@code Vector3f}.
     */
    public float getX() {
        return x;
    }

    /**
     * Returns the second component of this {@code Vector3f}.
     *
     * @return the second component of this {@code Vector3f}.
     */
    public float getY() {
        return y;
    }

    /**
     * Returns the third component of this {@code Vector3f}.
     *
     * @return the third component of this {@code Vector3f}.
     */
    public float getZ() {
        return z;
    }

    /**
     * Returns the component at the specified index.
     *
     * @param componentIndex the index of the component to be returned.
     * @return the component at the specified index.
     * @throws IndexOutOfBoundsException if the specified index was not between 0 and 2.
     */
    public float get(int componentIndex) {
        return switch (componentIndex) {
            case 0 -> getX();
            case 1 -> getY();
            case 2 -> getZ();
            default -> throw new IndexOutOfBoundsException("index must be between 0 and 2");
        };
    }

    /**
     * Returns the components of this {@code Vector3f} as an array.
     *
     * @return the components of this {@code Vector3f} as an array.
     */
    public float[] toArray() {
        return new float[]{getX(), getY(), getZ()};
    }

    /**
     * Returns the sum of this {@code Vector3f} and the passed in {@code Vector3f}.
     * <p>
     * Note: This method leaves the current {@code Vector3f} instance unchanged.
     *
     * @param vec the {@code Vector3f} to add to this {@code Vector3f} instance.
     * @return the sum of this {@code Vector3f} and the passed in {@code Vector3f}.
     * @throws NullPointerException if {@code vec} is {@code null}.
     */
    public Vector3f add(Vector3f vec) {
        Objects.requireNonNull(vec, "vec must not be null");
        return new Vector3f(getX() + vec.getX(), getY() + vec.getY(), getZ() + vec.getZ());
    }

    /**
     * Returns the difference of this {@code Vector3f} and the passed in {@code Vector3f}.
     * <p>
     * Note: This method leaves the current {@code Vector3f} instance unchanged.
     *
     * @param vec the {@code Vector3f} to subtract from this {@code Vector3f} instance.
     * @return the difference of this {@code Vector3f} and the passed in {@code Vector3f}.
     * @throws NullPointerException if {@code vec} is {@code null}.
     */
    public Vector3f subtract(Vector3f vec) {
        Objects.requireNonNull(vec, "vec must not be null");
        return new Vector3f(getX() - vec.getX(), getY() - vec.getY(), getZ() - vec.getZ());
    }

    /**
     * Returns a copy of the current {@code Vector3f} scaled by the specified factor.
     * <p>
     * Note: This method leaves the current {@code Vector3f} instance unchanged.
     *
     * @param factor the factor to scale the {@code Vector3f} by.
     * @return a copy of the current {@code Vector3f} scaled by the specified factor.
     */
    public Vector3f scale(float factor) {
        return new Vector3f(getX() * factor, getY() * factor, getZ() * factor);
    }

    /**
     * Returns a negated copy of the current {@code Vector3f}.
     * This method has the same effect as
     * {@linkplain #scale(float) scale} {@code (-1)}.
     * <p>
     * Note: This method leaves the current {@code Vector3f} instance unchanged.
     *
     * @return a negated copy of the current {@code Vector3f}.
     */
    public Vector3f negate() {
        return scale(-1);
    }

    /**
     * Returns the dot product of the current {@code Vector3f} instance and the given
     * {@code Vector3f}.
     *
     * @param vec the {@code Vector3f} to calculate the dot product with.
     * @return the dot product of the current {@code Vector3f} instance and {@code vec}.
     * @throws NullPointerException if {@code vec} is {@code null}.
     */
    public float dot(Vector3f vec) {
        Objects.requireNonNull(vec, "vec must not be null");
        return getX() * vec.getX() + getY() * vec.getY() + getZ() * vec.getZ();
    }

    /**
     * Returns the cross product of the current {@code Vector3f} instance and the given
     * {@code Vector3f}.
     *
     * @param vec the {@code Vector3f} to calculate the cross product with.
     * @return the cross product of the current {@code Vector3f} instance and {@code vec}.
     * Objects.requireNonNull(vec, "vec must not be null");
     */
    public Vector3f cross(Vector3f vec) {
        Objects.requireNonNull(vec, "vec must not be null");
        return new Vector3f(getY() * vec.getZ() - getZ() * vec.getY(),
                getZ() * vec.getX() - getX() * vec.getZ(),
                getX() * vec.getY() - getY() * vec.getX());
    }

    /**
     * Returns the length (euclidean norm) of this {@code Vector3f}.
     *
     * @return the length (euclidean norm) of this {@code Vector3f}.
     */
    public float length() {
        return (float) Math.sqrt(getX() * getX() + getY() * getY() + getZ() * getZ());
    }

    /**
     * Returns a normalized {@code Vector3f} instance of the current vector, by
     * scaling the vector with the reciprocal of its length.
     * In case the norm is equal to 0, returns a copy of this vector instead.
     *
     * @return a normalized copy of this {@code Vector3f} instance, or this {@code Vector3f} if its length is 0.
     */
    public Vector3f normalize() {
        float length = length();
        if (length == 0) {
            return this;
        }
        return scale(1 / length());
    }

    /**
     * Returns the distance from the current {@code Vector3f} instance to the given
     * {@code Vector3f}.
     *
     * @param vec the {@code Vector3f} to calculate the distance to.
     * @return the distance from this {@code Vector3f} to {@code vec}.
     * @throws NullPointerException if {@code vec} is {@code null}.
     */
    public float distance(Vector3f vec) {
        Objects.requireNonNull(vec, "vec must not be null");
        float dx = vec.getX() - getX();
        float dy = vec.getY() - getY();
        float dz = vec.getZ() - getZ();
        return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vector3f vector3f)) return false;
        return Float.compare(vector3f.getX(), getX()) == 0
                && Float.compare(vector3f.getY(), getY()) == 0
                && Float.compare(vector3f.getZ(), getZ()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY(), getZ());
    }

    @Override
    public String toString() {
        return "(" + getX() + ", " + getY() + ", " + getZ() + ")";
    }
}