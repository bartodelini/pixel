package com.bartodelini.pixel.modules.rendering.bitmap.shader;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A <i>ShaderUniforms</i> instance holds name-value pairs of uniforms, which can be passed in to shaders during their
 * invocation.
 *
 * @author Bartolini
 * @version 1.0
 */
public class ShaderUniforms {

    private final Map<String, Object> uniformMap = new HashMap<>();

    /**
     * Returns a uniform associated with the specified {@code name}, or {@code null} if no uniform with such name is
     * present.
     *
     * @param name the name of the uniform to be returned.
     * @return the uniform associated with the specified {@code name}, or {@code null} if no uniform with such name is present.
     * @throws NullPointerException if the specified {@code name} is {@code null}.
     */
    public Object getUniform(String name) {
        Objects.requireNonNull(name, "name must not be null");
        return uniformMap.get(name);
    }

    /**
     * Sets a uniform with the specified {@code name} and {@code value}.
     *
     * @param name    the name of the uniform.
     * @param uniform the uniform to set.
     */
    public void setUniform(String name, Object uniform) {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(uniform, "uniform must not be null");
        uniformMap.put(name, uniform);
    }
}