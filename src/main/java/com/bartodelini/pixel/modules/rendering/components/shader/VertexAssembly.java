package com.bartodelini.pixel.modules.rendering.components.shader;

import com.bartodelini.pixel.modules.rendering.bitmap.primitive.Vertex;
import com.bartodelini.pixel.modules.rendering.components.model.Mesh;

import java.util.List;

/**
 * A <i>VertexAssembly</i> is used to create a {@linkplain List} of {@linkplain Vertex Vertices} from a
 * {@linkplain Mesh}.
 *
 * @author Bartolini
 * @version 1.0
 */
@FunctionalInterface
public interface VertexAssembly {

    /**
     * Returns a {@linkplain List} of {@linkplain Vertex Vertices} based on the specified {@linkplain Mesh}.
     *
     * @param mesh the {@code Mesh} to create the {@code Vertices} from.
     * @return a {@code List} of {@code Vertices} based on the specified {@code Mesh}.
     */
    List<Vertex> getVertices(Mesh mesh);
}
