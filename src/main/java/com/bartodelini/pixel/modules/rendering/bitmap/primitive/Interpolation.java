package com.bartodelini.pixel.modules.rendering.bitmap.primitive;

/**
 * An <i>Interpolation</i> is used when specifying the interpolation mode for {@linkplain Vertex} attributes.
 *
 * @author Bartolini
 * @version 1.0
 */
public enum Interpolation {
    
    /**
     * The value will not be interpolated.
     */
    FLAT,

    /**
     * The value will be linearly interpolated in screen-space.
     */
    NO_PERSPECTIVE,

    /**
     * The value will be interpolated in a perspective-correct fashion. This is the default if no qualifier is
     * present.
     */
    SMOOTH
}