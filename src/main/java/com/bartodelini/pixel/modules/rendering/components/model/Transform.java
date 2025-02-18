package com.bartodelini.pixel.modules.rendering.components.model;

import com.bartodelini.pixel.ecs.Component;
import com.bartodelini.pixel.ecs.Entity;
import com.bartodelini.pixel.math.matrix.Matrix4f;
import com.bartodelini.pixel.math.vector.Vector3f;

import java.util.Objects;

/**
 * A <i>Transform</i> is a {@linkplain Component}, which is used to store and manipulate the scene orientation of an
 * {@linkplain Entity}.
 *
 * @author Bartolini
 * @version 1.0
 */
public class Transform extends Component {

    private Vector3f position;
    private Vector3f rotation;
    private Vector3f scale;
    private Matrix4f transformMatrix;

    private Transform parent;

    /**
     * Allocates a new {@code Transform} by passing in its position, rotation and scale.
     *
     * @param position the position of this {@code Transform}.
     * @param rotation the rotation of this {@code Transform}.
     * @param scale    the scale of this {@code Transform}.
     * @throws NullPointerException if any of the specified parameters is {@code null}.
     */
    public Transform(Vector3f position, Vector3f rotation, Vector3f scale) {
        this.position = Objects.requireNonNull(position, "position must not be null");
        this.rotation = Objects.requireNonNull(rotation, "rotation must not be null");
        this.scale = Objects.requireNonNull(scale, "scale must not be null");
        updateTransformMatrix();
    }

    /**
     * Allocates a new {@code Transform} by passing in its position. This constructor has the same effect as
     * {@linkplain #Transform(Vector3f, Vector3f, Vector3f) Transform} {@code (position, Vector3f.ZERO, Vector3f.ONE)}.
     *
     * @param position the position of this {@code Transform}.
     * @throws NullPointerException if the specified position is {@code null}.
     */
    public Transform(Vector3f position) {
        this(position, Vector3f.ZERO, Vector3f.ONE);
    }

    /**
     * Allocates a new {@code Transform}. This constructor has the same effect as
     * {@linkplain #Transform(Vector3f, Vector3f, Vector3f) Transform}
     * {@code (Vector3f.ZERO, Vector3f.ZERO, Vector3f.ONE)}.
     */
    public Transform() {
        this(Vector3f.ZERO, Vector3f.ZERO, Vector3f.ONE);
    }

    /**
     * Returns the position of this {@code Transform}.
     *
     * @return the position of this {@code Transform}.
     */
    public Vector3f getPosition() {
        return position;
    }

    /**
     * Sets the position for this {@code Transform} and updates the transform {@linkplain Matrix4f}.
     *
     * @param position the new position for this {@code Transform}.
     * @throws NullPointerException if the specified {@linkplain Vector3f} is {@code null}.
     */
    public void setPosition(Vector3f position) {
        this.position = Objects.requireNonNull(position, "position must not be null");
        updateTransformMatrix();
    }

    /**
     * Adds the specified translation {@linkplain Vector3f} to the position of this {@code Transform}.
     *
     * @param translation the translation {@code Vector3f} to be added to the position of this {@code Transform}.
     * @throws NullPointerException if the specified {@code Vector3f} is {@code null}.
     */
    public void translate(Vector3f translation) {
        setPosition(getPosition().add(translation));
    }

    /**
     * Returns the rotation of this {@code Transform}.
     *
     * @return the rotation of this {@code Transform}.
     */
    public Vector3f getRotation() {
        return rotation;
    }

    /**
     * Sets the rotation for this {@code Transform} and updates the transform {@linkplain Matrix4f}.
     *
     * @param rotation the new rotation for this {@code Transform}.
     * @throws NullPointerException if the specified {@linkplain Vector3f} is {@code null}.
     */
    public void setRotation(Vector3f rotation) {
        this.rotation = Objects.requireNonNull(rotation, "rotation must not be null");
        updateTransformMatrix();
    }

    /**
     * Adds the specified rotation {@linkplain Vector3f} to the rotation of this {@code Transform}.
     *
     * @param rotation the rotation {@code Vector3f} to be added to the rotation of this {@code Transform}.
     * @throws NullPointerException if the specified {@code Vector3f} is {@code null}.
     */
    public void rotate(Vector3f rotation) {
        setRotation(getRotation().add(rotation));
    }

    /**
     * Returns the scale of this {@code Transform}.
     *
     * @return the scale of this {@code Transform}.
     */
    public Vector3f getScale() {
        return scale;
    }

    /**
     * Sets the scale for this {@code Transform} and updates the transform {@linkplain Matrix4f}.
     *
     * @param scale the new scale for this {@code Transform}.
     * @throws NullPointerException if the specified {@linkplain Vector3f} is {@code null}.
     */
    public void setScale(Vector3f scale) {
        this.scale = Objects.requireNonNull(scale, "scale must not be null");
        updateTransformMatrix();
    }

    /**
     * Adds the specified scale {@linkplain Vector3f} to the scale of this {@code Transform}.
     *
     * @param scale the scale {@code Vector3f} to be added to the scale of this {@code Transform}.
     * @throws NullPointerException if the specified {@code Vector3f} is {@code null}.
     */
    public void scale(Vector3f scale) {
        setScale(getScale().add(scale));
    }

    /**
     * Returns the parent {@code Transform} of this {@code Transform}.
     *
     * @return the parent {@code Transform} of this {@code Transform}, or {@code null} if no parent is set.
     */
    public Transform getParent() {
        return parent;
    }

    /**
     * Sets the parent {@code Transform} for this {@code Transform}.
     *
     * @param parent the new parent {@code Transform} for this {@code Transform}.
     */
    public void setParent(Transform parent) {
        this.parent = parent;
    }

    /**
     * Returns the transform {@linkplain Matrix4f} of this {@code Transform}.
     *
     * @return the transform {@code Matrix4f} of this {@code Transform}.
     */
    public Matrix4f getTransformMatrix() {
        if (parent == null) {
            return transformMatrix;
        }
        return parent.getTransformMatrix().multiply(transformMatrix);
    }

    /**
     * A helper method used to update the transform {@linkplain Matrix4f} of this {@code Transform}.
     */
    private void updateTransformMatrix() {
        transformMatrix = Matrix4f.IDENTITY
                .translate(position)
                .rotate(rotation.getX(), Vector3f.RIGHT)
                .rotate(rotation.getY(), Vector3f.UP)
                .rotate(rotation.getZ(), Vector3f.FRONT)
                .scale(scale);
    }
}
