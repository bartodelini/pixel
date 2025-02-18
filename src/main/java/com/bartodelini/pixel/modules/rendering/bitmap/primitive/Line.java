package com.bartodelini.pixel.modules.rendering.bitmap.primitive;

import java.util.Objects;

/**
 * A <i>Line</i> is a {@linkplain Primitive} which consists of two {@linkplain Vertex vertices}.
 *
 * @author Bartolini
 * @version 1.0
 */
public final class Line extends Primitive {

    private final Vertex v1;
    private final Vertex v2;

    /**
     * Allocates a new {@code Line} by passing in its two {@linkplain Vertex vertices}.
     *
     * @param v1 the first {@code Vertex}.
     * @param v2 the second {@code Vertex}.
     * @throws NullPointerException if any of the specified {@code Vertices} is {@code null}.
     */
    public Line(Vertex v1, Vertex v2) {
        this.v1 = Objects.requireNonNull(v1, "v1 must not be null");
        this.v2 = Objects.requireNonNull(v2, "v2 must not be null");
    }

    /**
     * Returns the first {@linkplain Vertex} of this {@code Line}.
     *
     * @return the first {@code Vertex} of this {@code Line}.
     */
    public Vertex getV1() {
        return v1;
    }

    /**
     * Returns the second {@linkplain Vertex} of this {@code Line}.
     *
     * @return the second {@code Vertex} of this {@code Line}.
     */
    public Vertex getV2() {
        return v2;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Line) obj;
        return Objects.equals(this.v1, that.v1) &&
                Objects.equals(this.v2, that.v2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(v1, v2);
    }

    @Override
    public String toString() {
        return "Line[" +
                "v1=" + v1 + ", " +
                "v2=" + v2 + ']';
    }
}