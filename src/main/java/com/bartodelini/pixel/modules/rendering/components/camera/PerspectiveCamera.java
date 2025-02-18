package com.bartodelini.pixel.modules.rendering.components.camera;

import com.bartodelini.pixel.math.matrix.Matrices;
import com.bartodelini.pixel.math.matrix.Matrix4f;
import com.bartodelini.pixel.modules.rendering.bitmap.filter.PostProcessingFilter;

/**
 * An <i>OrthographicCamera</i> is a {@linkplain Camera} with a perspective-view frustum.
 *
 * @author Bartolini
 * @version 1.0
 */
public class PerspectiveCamera extends Camera {

    /**
     * Allocates a new {@code PerspectiveCamera} by passing in the {@code fov}, {@code aspectRatio}, values for the
     * near and far clipping planes, as well as the {@code backgroundColor} used when clearing the screen and a varargs
     * of {@linkplain PostProcessingFilter PostProcessingFilters}.
     *
     * @param fov             the fov used for this {@code Camera}.
     * @param aspectRatio     the aspectRatio used for this {@code Camera}.
     * @param near            the distance from the camera center to the near clipping plane of this {@code Camera}.
     * @param far             the distance from the camera center to the far clipping plane of this {@code Camera}.
     * @param backgroundColor the background color used when rendering the view from this {@code Camera}.
     * @param filters         a varargs of {@code PostProcessingFilters} to be applied to the view of this
     *                        {@code Camera}.
     * @throws NullPointerException if any of the specified {@code PostProcessingFilters} is {@code null}.
     */
    public PerspectiveCamera(float fov, float aspectRatio, float near, float far, int backgroundColor, PostProcessingFilter... filters) {
        super(fov, aspectRatio, near, far, backgroundColor, filters);
    }

    @Override
    public Matrix4f calculateProjectionMatrix() {
        return Matrices.getPerspectiveProjection(getFov(), getAspectRatio(), getNear(), getFov());
    }
}