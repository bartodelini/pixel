package com.bartodelini.pixel.modules.rendering.components.light;

import com.bartodelini.pixel.math.vector.Vector3f;

import java.util.Objects;

/**
 * A <i>DirectionalLight</i> is a {@linkplain Light} which illuminates the scene from one particular direction.
 *
 * @author Bartolini
 * @version 1.0
 */
public class DirectionalLight extends Light {

    private Vector3f direction;

    /**
     * Allocates a new {@code DirectionalLight} by passing in a {@linkplain Vector3f} representing its color, as well as
     * the direction of the light.
     *
     * @param color     the {@code Vector3f} representing the color of this {@code DirectionalLight}.
     * @param direction the direction of the light.
     * @throws NullPointerException if any of the specified parameters is {@code null}.
     */
    public DirectionalLight(Vector3f color, Vector3f direction) {
        super(color);
        this.direction = Objects.requireNonNull(direction, "direction must not be null");
    }

    /**
     * Returns the direction of this {@code DirectionalLight}.
     *
     * @return the direction of this {@code DirectionalLight}.
     */
    public Vector3f getDirection() {
        return direction;
    }

    /**
     * Sets the direction of this {@code DirectionalLight}.
     *
     * @param direction the new direction of this {@code DirectionalLight}.
     * @throws NullPointerException if any of the specified parameters is {@code null}.
     */
    public void setDirection(Vector3f direction) {
        this.direction = Objects.requireNonNull(direction, "direction must not be null");
    }
}