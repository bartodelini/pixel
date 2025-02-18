package com.bartodelini.pixel.modules.rendering.bitmap.shader;

import com.bartodelini.pixel.modules.rendering.bitmap.primitive.Primitive;

import java.util.List;

/**
 * A <i>GeometryShader</i> is used to add, remove or transform {@linkplain Primitive Primitives} based on the
 * {@linkplain ShaderUniforms} of the current shader invocation, as well as a {@linkplain Primitive}.
 *
 * @author Bartolini
 * @version 1.0
 */
@FunctionalInterface
public interface GeometryShader {

    /**
     * Returns a {@linkplain List} of {@linkplain Primitive Primitives} based on the specified
     * {@linkplain ShaderUniforms}, as well as a {@code Primitive}.
     *
     * @param shaderUniforms the {@code ShaderUniforms} of the current shader invocation.
     * @param triangle       the {@code Primitive} to process.
     * @return a {@code List} of {@code Primitives}.
     */
    List<Primitive> shade(ShaderUniforms shaderUniforms, Primitive triangle);
}