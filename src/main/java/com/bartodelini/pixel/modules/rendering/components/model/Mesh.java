package com.bartodelini.pixel.modules.rendering.components.model;

import com.bartodelini.pixel.math.vector.Vector2f;
import com.bartodelini.pixel.math.vector.Vector3f;

/**
 * A <i>Mesh</i> stores the data of a mesh.
 *
 * @param positions an array of {@linkplain Vector3f} representing the positions in a {@code Mesh}.
 * @param uvs       an array of {@linkplain Vector2f} representing the texture coordinates in a {@code Mesh}.
 * @param normals   an array of {@linkplain Vector3f} representing the normals in a {@code Mesh}.
 * @param faces     an array of {@linkplain Face Faces} representing the faces in a {@code Mesh}.
 * @param lines     an array of {@linkplain Line Lines} representing the lines in a {@code Mesh}.
 * @author Bartolini
 * @version 1.0
 */
public record Mesh(Vector3f[] positions, Vector2f[] uvs, Vector3f[] normals, Face[] faces,
                   Line[] lines) {

    /**
     * A <i>Face</i> stores three {@linkplain Point Points}, which make up a face.
     *
     * @param p1 the first {@code Point} of the {@code Face}.
     * @param p2 the second {@code Point} of the {@code Face}.
     * @param p3 the third {@code Point} of the {@code Face}.
     */
    public record Face(Point p1, Point p2, Point p3) {

        /**
         * A <i>Point</i> stores indices of the position, texture coordinate and normal of a {@code Point}, as well as a
         * {@linkplain Vector3f} representing the tangent in the {@code Point}, which can be used for the construction
         * of the tangent space.
         *
         * @param positionIndex the index of the position.
         * @param uvIndex       the index of the texture coordinate.
         * @param normalIndex   the index of the normal.
         * @param tangent       the tangent in the {@code Point}.
         */
        public record Point(int positionIndex, int uvIndex, int normalIndex, Vector3f tangent) {

            /**
             * Allocates a new {@code Point} by passing in the indices for the position, the texture coordinate and the
             * normal. Uses {@linkplain Vector3f#ZERO} for the tangent. This constructor has
             * the same effect as {@linkplain #Point(int, int, int, Vector3f) Point}
             * {@code (positionIndex, uvIndex, normalIndex, Vector3f.ZERO)}.
             *
             * @param positionIndex the index of the position.
             * @param uvIndex       the index of the texture coordinate.
             * @param normalIndex   the index of the normal.
             */
            public Point(int positionIndex, int uvIndex, int normalIndex) {
                this(positionIndex, uvIndex, normalIndex, Vector3f.ZERO);
            }
        }
    }

    /**
     * A <i>Line</i> stores two indices of positions, which make up a line.
     *
     * @param positionIndex1 the first position index.
     * @param positionIndex2 the second position index.
     */
    public record Line(int positionIndex1, int positionIndex2) {
    }
}