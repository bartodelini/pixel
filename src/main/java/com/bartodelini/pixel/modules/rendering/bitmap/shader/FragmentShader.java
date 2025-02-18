package com.bartodelini.pixel.modules.rendering.bitmap.shader;

import com.bartodelini.pixel.modules.rendering.bitmap.primitive.Vertex;

/**
 * A <i>FragmentShader</i> is used to determine a fragment color based on the {@linkplain ShaderUniforms} of the current
 * shader invocation, as well as the {@linkplain Vertex} associated with the shaded fragment.
 *
 * @author Bartolini
 * @version 1.0
 */
@FunctionalInterface
public interface FragmentShader {

    /**
     * Returns the color for a fragment based on the specified {@linkplain ShaderUniforms} as well as the
     * {@linkplain Vertex} associated with the fragment.
     *
     * @param shaderUniforms the {@code ShaderUniforms} of the current shader invocation.
     * @param vertex         the {@code Vertex} associated with the shaded fragment.
     * @return the color of the currently shaded fragment.
     */
    int shade(ShaderUniforms shaderUniforms, Vertex vertex);
}