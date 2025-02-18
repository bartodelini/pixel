package com.bartodelini.pixel.modules.rendering.bitmap.shader;

import com.bartodelini.pixel.modules.rendering.bitmap.primitive.Vertex;

/**
 * A <i>VertexShader</i> is used to transform {@linkplain Vertex vertices}.
 *
 * @author Bartolini
 * @version 1.0
 */
@FunctionalInterface
public interface VertexShader {

    /**
     * Transforms the specified {@linkplain Vertex} using the specified {@linkplain ShaderUniforms}.
     *
     * @param shaderUniforms the {@code ShaderUniforms} of the current shader invocation.
     * @param vertex         the {@code Vertex} to be transformed.
     */
    void shade(ShaderUniforms shaderUniforms, Vertex vertex);
}