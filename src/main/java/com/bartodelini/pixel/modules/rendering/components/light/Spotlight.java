package com.bartodelini.pixel.modules.rendering.components.light;

import com.bartodelini.pixel.math.vector.Vector3f;

import java.util.Objects;

/**
 * A <i>Spotlight</i> is a {@linkplain Light} which illuminates the scene in a flashlight-like manner.
 *
 * @author Bartolini
 * @version 1.0
 */
public class Spotlight extends Light {

    private Vector3f direction;
    private float exponent;
    private float cutoff;
    private float cutoffCos;

    /**
     * Allocates a new {@code Spotlight} by passing in a {@linkplain Vector3f} representing its color, as well as
     * the direction of the light, the exponent and the cutoff of the light cone.
     *
     * @param color     the {@code Vector3f} representing the color of this {@code Spotlight}.
     * @param direction the direction of the light.
     * @param exponent  the exponent of the light cone.
     * @param cutoff    the cutoff of the light cone.
     * @throws NullPointerException if the specified color or direction are {@code null}.
     */
    public Spotlight(Vector3f color, Vector3f direction, float exponent, float cutoff) {
        super(color);
        this.direction = Objects.requireNonNull(direction, "direction must not be null");
        this.exponent = exponent;
        this.cutoff = cutoff;
        this.cutoffCos = (float) Math.cos(cutoff);
    }

    /**
     * Returns the direction of this {@code Spotlight}.
     *
     * @return the direction of this {@code Spotlight}.
     */
    public Vector3f getDirection() {
        return direction;
    }

    /**
     * Sets the direction of this {@code Spotlight}.
     *
     * @param direction the new direction of this {@code Spotlight}.
     * @throws NullPointerException if any of the specified parameters is {@code null}.
     */
    public void setDirection(Vector3f direction) {
        this.direction = Objects.requireNonNull(direction, "direction must not be null");
    }

    /**
     * Returns the exponent of this {@code Spotlight}.
     *
     * @return the exponent of this {@code Spotlight}.
     */
    public float getExponent() {
        return exponent;
    }

    /**
     * Sets the exponent of this {@code Spotlight}.
     *
     * @param exponent the exponent of this {@code Spotlight}.
     */
    public void setExponent(float exponent) {
        this.exponent = exponent;
    }

    /**
     * Returns the cutoff of this {@code Spotlight}.
     *
     * @return the cutoff of this {@code Spotlight}.
     */
    public float getCutoff() {
        return cutoff;
    }

    /**
     * Sets the cutoff of this {@code Spotlight}.
     *
     * @param cutoff the cutoff of this {@code Spotlight}.
     */
    public void setCutoff(float cutoff) {
        this.cutoff = cutoff;
        this.cutoffCos = (float) Math.cos(cutoff);
    }

    /**
     * Returns the cosine of the cutoff of this {@code Spotlight}.
     *
     * @return the cosine of the cutoff of this {@code Spotlight}.
     */
    public float getCutoffCos() {
        return cutoffCos;
    }
}