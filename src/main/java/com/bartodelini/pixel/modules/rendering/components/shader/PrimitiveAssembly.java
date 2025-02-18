package com.bartodelini.pixel.modules.rendering.components.shader;

import com.bartodelini.pixel.modules.rendering.bitmap.primitive.Triangle;
import com.bartodelini.pixel.modules.rendering.bitmap.primitive.Vertex;

import java.util.ArrayList;
import java.util.List;

/**
 * A <i>PrimitiveAssembly</i> is used to create {@linkplain Triangle Triangles} from a {@linkplain List} of
 * {@linkplain Vertex Vertices}.
 *
 * @author Bartolini
 * @version 1.0
 */
@FunctionalInterface
public interface PrimitiveAssembly {

    /**
     * Creates {@linkplain Triangle Triangles} from three consecutive {@linkplain Vertex Vertices} in the
     * {@linkplain List}.
     */
    PrimitiveAssembly CONSECUTIVE = vertices -> {
        List<Triangle> triangles = new ArrayList<>();
        for (int i = 0; i < vertices.size() - 2; i += 3) {
            triangles.add(new Triangle(
                    vertices.get(i),
                    vertices.get(i + 1),
                    vertices.get(i + 2)));
        }
        return triangles;
    };

    /**
     * Creates {@linkplain Triangle Triangles} using the first {@linkplain Vertex} in the {@linkplain List} and two
     * consecutive {@code Vertices}.
     */
    PrimitiveAssembly FAN = vertices -> {
        List<Triangle> triangles = new ArrayList<>();
        for (int i = 1; i < vertices.size() - 1; i++) {
            triangles.add(new Triangle(
                    vertices.get(0),
                    vertices.get(i),
                    vertices.get(i + 1)));
        }
        return triangles;
    };

    /**
     * Returns a {@linkplain List} of {@linkplain Triangle Triangles} based on the specified {@code List} of
     * {@linkplain Vertex Vertices}.
     *
     * @param vertices the {@code List} of {@code Vertices} to create {@code Triangles} from.
     * @return a {@code List} of {@code Triangles} based on the specified {@code Vertices}.
     */
    List<Triangle> getTrianglesFromVertices(List<Vertex> vertices);
}