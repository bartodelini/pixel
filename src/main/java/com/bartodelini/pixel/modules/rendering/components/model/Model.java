package com.bartodelini.pixel.modules.rendering.components.model;

import com.bartodelini.pixel.ecs.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A <i>Model</i> is a {@linkplain Component}, which is used to store the {@linkplain Mesh} and a {@code Mesh}
 * representing its axis-aligned bounding box.
 *
 * @author Bartolini
 * @version 1.0
 */
public class Model extends Component {

    private static final Map<Mesh, Mesh> aabbMap = new HashMap<>();

    private final Mesh mesh;
    private final Mesh aabb;

    /**
     * Allocates a new {@code Model} by passing in its {@linkplain Mesh}.
     *
     * @param mesh the {@code Mesh} used for this {@code Model}.
     * @throws NullPointerException if the specified {@code Mesh} is {@code null}.
     */
    public Model(Mesh mesh) {
        this.mesh = Objects.requireNonNull(mesh, "mesh must not be null");
        // Assign the AABB, reuse one if one already exists for this particular Mesh, calculate otherwise
        this.aabb = aabbMap.computeIfAbsent(mesh, MeshUtils::calculateAABB);
    }

    /**
     * Return the {@linkplain Mesh} of this {@code Model}.
     *
     * @return the {@code Mesh} of this {@code Model}.
     */
    public Mesh getMesh() {
        return mesh;
    }

    /**
     * Return the {@linkplain Mesh} representing the bounding mesh of this {@code Model's} {@code Mesh}.
     *
     * @return the {@code Mesh} representing the bounding mesh of this {@code Model's} {@code Mesh}.
     */
    public Mesh getBoundingMesh() {
        return aabb;
    }
}