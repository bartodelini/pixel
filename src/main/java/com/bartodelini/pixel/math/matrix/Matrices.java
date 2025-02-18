package com.bartodelini.pixel.math.matrix;

import com.bartodelini.pixel.math.vector.Vector3f;

import java.util.Objects;

/**
 * A utility class providing functions for creating common {@linkplain Matrix4f matrices}.
 *
 * @author Bartolini
 * @version 1.0
 */
public class Matrices {

    private Matrices() {
    }

    /**
     * Returns a viewport transformation matrix based on the passed in x and y position, as well as the width, height
     * and the values for the minimum and maximum depth.
     *
     * @param x       the x offset of the viewport.
     * @param y       the y offset of the viewport.
     * @param width   the width of the viewport.
     * @param height  the height of the viewport.
     * @param nearVal the depth value for a point on the near clipping plane.
     * @param farVal  the depth value for a point on the far clipping plane.
     * @return a viewport transformation matrix based on the specified parameters.
     */
    public static Matrix4f getViewport(int x, int y, int width, int height, float nearVal, float farVal) {
        float halfWidth = width / 2f;
        float halfHeight = height / 2f;
        return new Matrix4f(
                halfWidth, 0, 0, x + halfWidth - 0.5f,
                0, -halfHeight, 0, y + halfHeight - 0.5f,
                0, 0, (farVal - nearVal) / 2, (farVal + nearVal) / 2,
                0, 0, 0, 1);
    }

    /**
     * Returns a viewport transformation matrix based on the passed in width and height. This method has the same
     * effect as {@linkplain #getViewport(int, int, int, int, float, float) getViewport}
     * {@code (0, 0, width, height, 0, 1)}.
     *
     * @param width  the width of the viewport.
     * @param height the height of the viewport.
     * @return a viewport transformation matrix based on the specified width and height.
     */
    public static Matrix4f getViewport(int width, int height) {
        return getViewport(0, 0, width, height, 0, 1);
    }

    /**
     * Returns a view matrix based on the passed camera position, the point the camera is looking
     * at and the camera's up vector.
     *
     * @param cameraPosition the position of the camera.
     * @param center         the point the camera is looking at.
     * @param up             the camera's up vector.
     * @return a view matrix based on the specified parameters.
     * @throws NullPointerException if any of the specified vectors is {@code null}.
     */
    public static Matrix4f getView(Vector3f cameraPosition, Vector3f center, Vector3f up) {
        Objects.requireNonNull(cameraPosition, "cameraPosition must not be null");
        Objects.requireNonNull(center, "center must not be null");
        Objects.requireNonNull(up, "up must not be null");

        Vector3f z = cameraPosition.subtract(center).normalize();
        Vector3f x = up.cross(z).normalize();
        Vector3f y = z.cross(x);
        Vector3f cameraTranslation = cameraPosition.negate();

        return new Matrix4f(
                x.getX(), x.getY(), x.getZ(), cameraTranslation.getX(),
                y.getX(), y.getY(), y.getZ(), cameraTranslation.getY(),
                z.getX(), z.getY(), z.getZ(), cameraTranslation.getZ(),
                0, 0, 0, 1);
    }

    /**
     * Returns a matrix for an orthographic-view frustum.
     *
     * @param left   the distance from the center to the left view frustum plane.
     * @param right  the distance from the center to the right view frustum plane.
     * @param bottom the distance from the center to the bottom view frustum plane.
     * @param top    the distance from the center to the top view frustum plane.
     * @param near   the distance from the center to the near view frustum plane.
     * @param far    the distance from the center to the far view frustum plane.
     * @return a matrix for an orthographic-view frustum based on the specified parameters.
     */
    public static Matrix4f getOrthographicProjection(
            float left, float right, float bottom, float top, float near, float far) {
        return new Matrix4f(
                2 / (right - left), 0, 0, -(left + right) / (right - left),
                0, 2 / (top - bottom), 0, -(bottom + top) / (top - bottom),
                0, 0, -2 / (far - near), -(far + near) / (far - near),
                0, 0, 0, 1);
    }

    /**
     * Returns a matrix for an orthographic-view frustum.
     *
     * @param fov         the vertical field of view angle in degrees.
     * @param aspectRatio the aspect ratio of the width to the height.
     * @param near        the distance from the center to the near view frustum plane.
     * @param far         the distance from the center to the far view frustum plane.
     * @return a matrix for an orthographic-view frustum based on the specified parameters.
     */
    public static Matrix4f getOrthographicProjection(float fov, float aspectRatio, float near, float far) {
        float f = (float) Math.tan(fov * Math.PI / 360.0);
        float top = near * f;
        float right = top * aspectRatio;
        return getOrthographicProjection(-right, right, -top, top, near, far);
    }

    /**
     * Returns a matrix for perspective-view frustum.
     *
     * @param left   the distance from the center to the left view frustum plane.
     * @param right  the distance from the center to the right view frustum plane.
     * @param bottom the distance from the center to the bottom view frustum plane.
     * @param top    the distance from the center to the top view frustum plane.
     * @param near   the distance from the center to the near view frustum plane.
     * @param far    the distance from the center to the far view frustum plane.
     * @return a matrix for perspective-view frustum based on the specified parameters.
     */
    public static Matrix4f getPerspectiveProjection(
            float left, float right, float bottom, float top, float near, float far) {
        return new Matrix4f(
                2 * near / (right - left), 0, (left + right) / (right - left), 0,
                0, 2 * near / (top - bottom), (bottom + top) / (top - bottom), 0,
                0, 0, -(far + near) / (far - near), -2 * far * near / (far - near),
                0, 0, -1, 0);
    }

    /**
     * Returns a matrix for a symmetric perspective-view frustum.
     *
     * @param fov         the vertical field of view angle in degrees.
     * @param aspectRatio the aspect ratio of the width to the height.
     * @param near        the distance from the center to the near view frustum plane.
     * @param far         the distance from the center to the far view frustum plane.
     * @return a matrix for a symmetric perspective-view frustum based on the specified parameters.
     */
    public static Matrix4f getPerspectiveProjection(float fov, float aspectRatio, float near, float far) {
        float f = (float) Math.tan(fov * Math.PI / 360.0);
        float top = near * f;
        float right = top * aspectRatio;
        return getPerspectiveProjection(-right, right, -top, top, near, far);
    }
}