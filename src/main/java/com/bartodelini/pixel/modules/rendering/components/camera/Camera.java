package com.bartodelini.pixel.modules.rendering.components.camera;

import com.bartodelini.pixel.ecs.Component;
import com.bartodelini.pixel.math.matrix.Matrix4f;
import com.bartodelini.pixel.modules.rendering.bitmap.filter.PostProcessingFilter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A <i>Camera</i> is a {@linkplain Component}, which is used for transforming the view, as well as managing
 * {@linkplain PostProcessingFilter PostProcessingFilters}.
 *
 * @author Bartolini
 * @version 1.0
 */
public abstract class Camera extends Component {

    private final List<PostProcessingFilter> postProcessingFilters = new ArrayList<>();

    private float fov;
    private float aspectRatio;
    private float near;
    private float far;
    private int backgroundColor;
    private Matrix4f projectionMatrix;

    /**
     * Allocates a new {@code Camera} by passing in the {@code fov}, {@code aspectRatio}, values for the near and far
     * clipping planes, as well as the {@code backgroundColor} used when clearing the screen and a varargs of
     * {@linkplain PostProcessingFilter PostProcessingFilters}.
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
    public Camera(float fov, float aspectRatio, float near, float far, int backgroundColor, PostProcessingFilter... filters) {
        this.fov = fov;
        this.aspectRatio = aspectRatio;
        this.near = near;
        this.far = far;
        this.backgroundColor = backgroundColor;
        this.projectionMatrix = calculateProjectionMatrix();
        this.postProcessingFilters.addAll(List.of(filters));
    }

    /**
     * Adds the specified {@linkplain PostProcessingFilter} to this {@code Camera}.
     *
     * @param filter the {@code PostProcessingFilter} to be added to this {@code Camera}.
     * @throws NullPointerException if the specified {@code PostProcessingFilter} is {@code null}.
     */
    public void addPostProcessingFilter(PostProcessingFilter filter) {
        Objects.requireNonNull(filter, "filter must not be null");
        postProcessingFilters.add(filter);
    }

    /**
     * Removes the specified {@linkplain PostProcessingFilter} from this {@code Camera}.
     *
     * @param filter the {@code PostProcessingFilter} to be removed from this {@code Camera}.
     * @throws NullPointerException if the specified {@code PostProcessingFilter} is {@code null}.
     */
    public void removePostProcessingFilter(PostProcessingFilter filter) {
        Objects.requireNonNull(filter, "filter must not be null");
        postProcessingFilters.remove(filter);
    }

    /**
     * Returns an unmodifiable {@linkplain List} of the {@linkplain PostProcessingFilter PostProcessingFilters} added to
     * this {@code Camera}.
     *
     * @return an unmodifiable {@code List} of the {@code PostProcessingFilters} added to this {@code Camera}.
     */
    public List<PostProcessingFilter> getPostProcessingFilters() {
        return Collections.unmodifiableList(postProcessingFilters);
    }

    /**
     * Returns the fov of this {@code Camera}.
     *
     * @return the fov of this {@code Camera}.
     */
    public float getFov() {
        return fov;
    }

    /**
     * Sets the fov of this {@code Camera} and recalculates the projection matrix if needed.
     *
     * @param fov the new fov of this {@code Camera}.
     */
    public void setFov(float fov) {
        if (this.fov == fov) {
            return;
        }
        this.fov = fov;
        projectionMatrix = calculateProjectionMatrix();
    }

    /**
     * Returns the aspectRatio of this {@code Camera}.
     *
     * @return the aspectRatio of this {@code Camera}.
     */
    public float getAspectRatio() {
        return aspectRatio;
    }

    /**
     * Sets the aspect ratio of this {@code Camera} and recalculates the projection matrix if needed.
     *
     * @param aspectRatio the new fov of this {@code Camera}.
     */
    public void setAspectRatio(float aspectRatio) {
        if (this.aspectRatio == aspectRatio) {
            return;
        }
        this.aspectRatio = aspectRatio;
        projectionMatrix = calculateProjectionMatrix();
    }

    /**
     * Returns the distance from the camera center to the near clipping plane of this {@code Camera}.
     *
     * @return the distance from the camera center to the near clipping plane of this {@code Camera}.
     */
    public float getNear() {
        return near;
    }

    /**
     * Sets the distance from the camera center to the near clipping plane of this {@code Camera} and recalculates the
     * projection matrix if needed.
     *
     * @param near the new distance from the camera center to the near clipping plane of this {@code Camera}.
     */
    public void setNear(float near) {
        if (this.near == near) {
            return;
        }
        this.near = near;
        projectionMatrix = calculateProjectionMatrix();
    }

    /**
     * Returns the distance from the camera center to the far clipping plane of this {@code Camera}.
     *
     * @return the distance from the camera center to the far clipping plane of this {@code Camera}.
     */
    public float getFar() {
        return far;
    }

    /**
     * Sets the distance from the camera center to the far clipping plane of this {@code Camera} and recalculates the
     * projection matrix if needed.
     *
     * @param far the new distance from the camera center to the far clipping plane of this {@code Camera}.
     */
    public void setFar(float far) {
        if (this.far == far) {
            return;
        }
        this.far = far;
        projectionMatrix = calculateProjectionMatrix();
    }

    /**
     * Returns the background color used for this {@code Camera's} view.
     *
     * @return the background color used for this {@code Camera's} view.
     */
    public int getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Sets the background color used for this {@code Camera's} view.
     *
     * @param backgroundColor the new background color to be used for this {@code Camera's} view.
     */
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /**
     * Returns the {@code projection matrix} of this {@code Camera}.
     *
     * @return the {@code projection matrix} of this {@code Camera}.
     */
    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    /**
     * Calculates the {@code projection matrix} of this {@code Camera}.
     *
     * @return the {@code projection matrix} of this {@code Camera}.
     */
    public abstract Matrix4f calculateProjectionMatrix();
}