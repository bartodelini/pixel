package com.bartodelini.pixel.modules.rendering.components.light;

import com.bartodelini.pixel.math.vector.Vector3f;

/**
 * A <i>PointLight</i> is a {@linkplain Light} which illuminates the scene from one particular point.
 *
 * @author Bartolini
 * @version 1.0
 */
public class PointLight extends Light {

    /**
     * Allocates a new {@code PointLight} by passing in a {@linkplain Vector3f} used for its color.
     *
     * @param color the {@code Vector3f} representing the color of this {@code PointLight}.
     * @throws NullPointerException if the specified {@code Vector3f} is {@code null}.
     */
    public PointLight(Vector3f color) {
        super(color);
    }
}