package com.bartodelini.pixel.modules.rendering.components.light;

import com.bartodelini.pixel.ecs.Component;
import com.bartodelini.pixel.math.vector.Vector3f;

import java.util.Objects;

/**
 * A <i>Light</i> is a {@linkplain Component} which defines variables used for modelling light sources.
 *
 * @author Bartolini
 * @version 1.0
 */
public abstract class Light extends Component {

    private Vector3f color;

    /**
     * Allocates a new {@code Light} by passing in a {@linkplain Vector3f} used for its color.
     *
     * @param color the {@code Vector3f} representing the color of this {@code Light}.
     * @throws NullPointerException if the specified {@code Vector3f} is {@code null}.
     */
    public Light(Vector3f color) {
        this.color = Objects.requireNonNull(color, "color must not be null");
    }

    /**
     * Returns the {@linkplain Vector3f} representing the color of this {@code Light}.
     *
     * @return the {@code Vector3f} representing the color of this {@code Light}.
     */
    public Vector3f getColor() {
        return color;
    }

    /**
     * Sets the color of this {@code Light} by specifying it with a {@linkplain Vector3f}.
     *
     * @param color the {@code Vector3f} representing the new color of this {@code Light}.
     * @throws NullPointerException if the specified {@code Vector3f} is {@code null}.
     */
    public void setColor(Vector3f color) {
        this.color = Objects.requireNonNull(color, "color must not be null");
    }
}