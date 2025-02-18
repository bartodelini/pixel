package com.bartodelini.pixel.modules.rendering.bitmap.primitive;

import java.util.Objects;

/**
 * A <i>Triangle</i> is a {@linkplain Primitive} which consists of three {@linkplain Vertex vertices}.
 *
 * @author Bartolini
 * @version 1.0
 */
public final class Triangle extends Primitive {

    private final Vertex v1;
    private final Vertex v2;
    private final Vertex v3;

    /**
     * Allocates a new {@code Triangle} by passing in its three {@linkplain Vertex vertices}.
     *
     * @param v1 the first {@code Vertex}.
     * @param v2 the second {@code Vertex}.
     * @param v3 the third {@code Vertex}.
     * @throws NullPointerException if any of the specified {@code Vertices} is {@code null}.
     */
    public Triangle(Vertex v1, Vertex v2, Vertex v3) {
        this.v1 = Objects.requireNonNull(v1, "v1 must not be null");
        this.v2 = Objects.requireNonNull(v2, "v2 must not be null");
        this.v3 = Objects.requireNonNull(v3, "v3 must not be null");
    }

    /**
     * Returns whether this {@code Triangle} is front-facing. A {@code Triangle} is front-facing if its
     * {@linkplain Vertex vertices} are wound up in a counter-clockwise fashion on the screen.
     *
     * @return {@code true} if this {@code Triangle} is front-facing; {@code false} otherwise.
     */
    public boolean isFrontFacing() {
        float ax = getV3().getScreenSpacePosition().getX() - getV1().getScreenSpacePosition().getX();
        float ay = getV3().getScreenSpacePosition().getY() - getV1().getScreenSpacePosition().getY();
        float bx = getV2().getScreenSpacePosition().getX() - getV1().getScreenSpacePosition().getX();
        float by = getV2().getScreenSpacePosition().getY() - getV1().getScreenSpacePosition().getY();

        return ax * by - bx * ay > 0;
    }

    /**
     * Returns the first {@linkplain Vertex} of this {@code Triangle}.
     *
     * @return the first {@code Vertex} of this {@code Triangle}.
     */
    public Vertex getV1() {
        return v1;
    }

    /**
     * Returns the second {@linkplain Vertex} of this {@code Triangle}.
     *
     * @return the second {@code Vertex} of this {@code Triangle}.
     */
    public Vertex getV2() {
        return v2;
    }

    /**
     * Returns the third {@linkplain Vertex} of this {@code Triangle}.
     *
     * @return the third {@code Vertex} of this {@code Triangle}.
     */
    public Vertex getV3() {
        return v3;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Triangle) obj;
        return Objects.equals(this.v1, that.v1) &&
                Objects.equals(this.v2, that.v2) &&
                Objects.equals(this.v3, that.v3);
    }

    @Override
    public int hashCode() {
        return Objects.hash(v1, v2, v3);
    }

    @Override
    public String toString() {
        return "Triangle[" +
                "v1=" + v1 + ", " +
                "v2=" + v2 + ", " +
                "v3=" + v3 + ']';
    }
}