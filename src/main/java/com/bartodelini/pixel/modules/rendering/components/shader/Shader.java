package com.bartodelini.pixel.modules.rendering.components.shader;

import com.bartodelini.pixel.math.matrix.Matrix4f;
import com.bartodelini.pixel.modules.rendering.bitmap.blending.BlendFunction;
import com.bartodelini.pixel.modules.rendering.bitmap.shader.FragmentShader;
import com.bartodelini.pixel.modules.rendering.bitmap.shader.GeometryShader;
import com.bartodelini.pixel.modules.rendering.bitmap.shader.ShaderUniforms;
import com.bartodelini.pixel.modules.rendering.bitmap.shader.VertexShader;
import com.bartodelini.pixel.modules.rendering.components.camera.Camera;
import com.bartodelini.pixel.modules.rendering.components.model.Mesh;
import com.bartodelini.pixel.modules.rendering.components.model.Transform;
import com.bartodelini.pixel.modules.scripting.ScriptableComponent;

/**
 * A <i>Shader</i> is a {@linkplain ScriptableComponent} used to control how a {@linkplain Mesh} is rendered to the
 * screen.
 *
 * @author Bartolini
 * @version 1.0
 */
public abstract class Shader extends ScriptableComponent {

    /**
     * Returns whether this {@code Shader} is opaque.
     *
     * @return {@code true} if this {@code Shader} is opaque; {@code false} otherwise.
     */
    public boolean isOpaque() {
        return true;
    }

    /**
     * Returns whether this {@code Shader} should cull back-faces.
     *
     * @return {@code true} if this {@code Shader} has back-face culling enabled; {@code false} otherwise.
     */
    public boolean isBackFaceCullingEnabled() {
        return true;
    }

    /**
     * Returns the {@linkplain BlendFunction} used by this {@code Shader}.
     *
     * @return the {@code BlendFunction} used by this {@code Shader}.
     */
    public BlendFunction getBlendFunction() {
        return BlendFunction.DEFAULT;
    }

    /**
     * Returns the {@linkplain ShaderUniforms} based on the specified {@linkplain Camera}, projection, view and
     * projection-view {@linkplain Matrix4f Matrices}, as well as the {@linkplain Transform}.
     *
     * @param camera         the {@code Camera} used to render the scene.
     * @param projection     the projection matrix of the {@code Camera} used to render the scene.
     * @param view           the view matrix of the {@code Camera} used to render the scene.
     * @param projectionView the premultiplied projection-view matrix of the {@code Camera} used to render the scene.
     * @param transform      the {@code Transform} of the rendered object.
     * @return the {@code ShaderUniforms} based on the specified parameters.
     */
    public abstract ShaderUniforms getShaderUniforms(Camera camera, Matrix4f projection, Matrix4f view, Matrix4f projectionView, Transform transform);

    /**
     * Returns the {@linkplain VertexAssembly} of this {@code Shader}.
     *
     * @return the {@code VertexAssembly} of this {@code Shader}.
     */
    public abstract VertexAssembly getVertexAssembly();

    /**
     * Returns the {@linkplain VertexShader} of this {@code Shader}.
     *
     * @return the {@code VertexShader} of this {@code Shader}.
     */
    public abstract VertexShader getVertexShader();

    /**
     * Returns the {@linkplain PrimitiveAssembly} of this {@code Shader}. The default is
     * {@linkplain PrimitiveAssembly#CONSECUTIVE}.
     *
     * @return the {@code PrimitiveAssembly} of this {@code Shader}.
     */
    public PrimitiveAssembly getPrimitiveAssembly() {
        return PrimitiveAssembly.CONSECUTIVE;
    }

    /**
     * Returns the {@linkplain GeometryShader} of this {@code Shader}. The default is {@code null}.
     *
     * @return the {@code GeometryShader} of this {@code Shader}.
     */
    public GeometryShader getGeometryShader() {
        return null;
    }

    /**
     * Returns the {@linkplain FragmentShader} of this {@code Shader}.
     *
     * @return the {@code FragmentShader} of this {@code Shader}.
     */
    public abstract FragmentShader getFragmentShader();
}