package com.bartodelini.pixel.modules.rendering.util;

import com.bartodelini.pixel.math.vector.Vector4f;
import com.bartodelini.pixel.modules.rendering.bitmap.primitive.Line;
import com.bartodelini.pixel.modules.rendering.bitmap.primitive.Triangle;
import com.bartodelini.pixel.modules.rendering.bitmap.primitive.Vertex;
import com.bartodelini.pixel.modules.rendering.components.shader.PrimitiveAssembly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A utility class providing useful functions for clipping.
 *
 * @author Bartolini
 * @version 1.0
 */
public final class ClipUtils {

    /**
     * A <i>OutCode</i> represents the location relative to the clipping volume.
     *
     * @author Bartolini
     * @version 1.0
     */
    public static class OutCode {

        /**
         * The {@code OutCode} for elements which lie inside the clip volume.
         */
        public static final int INSIDE = 0;

        /**
         * The {@code OutCode} for elements which lie outside to the left of the clip volume.
         */
        public static final int LEFT = 1;

        /**
         * The {@code OutCode} for elements which lie outside to the right of the clip volume.
         */
        public static final int RIGHT = 1 << 1;

        /**
         * The {@code OutCode} for elements which lie outside to the bottom of the clip volume.
         */
        public static final int BOTTOM = 1 << 2;

        /**
         * The {@code OutCode} for elements which lie outside to the top of the clip volume.
         */
        public static final int TOP = 1 << 3;

        /**
         * The {@code OutCode} for elements which lie outside to the back of the clip volume.
         */
        public static final int BACK = 1 << 4;

        /**
         * The {@code OutCode} for elements which lie outside to the front of the clip volume.
         */
        public static final int FRONT = 1 << 5;
    }

    /**
     * A <i>ClipSide</i> represents a side of a clip plane.
     *
     * @author Bartolini
     * @version 1.0
     */
    public enum ClipSide {

        /**
         * The positive {@code ClipSide}.
         */
        POSITIVE(1),

        /**
         * The negative {@code ClipSide}.
         */
        NEGATIVE(-1);

        private final int factor;

        /**
         * Allocates a new {@code ClipSide} by passing in its factor.
         *
         * @param factor the factor of the {@code ClipSide}.
         */
        ClipSide(int factor) {
            this.factor = factor;
        }

        /**
         * Returns the factor of the {@code ClipSide}.
         *
         * @return the factor of the {@code ClipSide}.
         */
        public int getFactor() {
            return factor;
        }
    }

    private ClipUtils() {
    }

    /**
     * Calculates the {@code OutCode} of the passed in {@linkplain Vector4f}.
     *
     * @param vec the {@code Vector4f} for which to calculate the {@code OutCode}.
     * @return the {@code OutCode} of specified {@code Vector4f}.
     * @throws NullPointerException if the specified {@code Vector4f} is {@code null}.
     */
    public static int getOutCode(Vector4f vec) {
        Objects.requireNonNull(vec, "vec must not be null");

        int outCode = OutCode.INSIDE;

        if (vec.getX() < -vec.getW()) {
            outCode |= OutCode.LEFT;
        } else if (vec.getX() > vec.getW()) {
            outCode |= OutCode.RIGHT;
        }
        if (vec.getY() < -vec.getW()) {
            outCode |= OutCode.BOTTOM;
        } else if (vec.getY() > vec.getW()) {
            outCode |= OutCode.TOP;
        }
        if (vec.getZ() < -vec.getW()) {
            outCode |= OutCode.BACK;
        } else if (vec.getZ() > vec.getW()) {
            outCode |= OutCode.FRONT;
        }

        return outCode;
    }

    /**
     * Returns the conjunction (logical AND) of the {@linkplain OutCode OutCodes} of the three clip space positions of
     * the specified {@linkplain Triangle}.
     *
     * @param triangle the {@code Triangle} for which to calculate the conjunction of {@code OutCodes}.
     * @return the conjunction of {@code OutCodes} of the specified {@code Triangle}.
     * @throws NullPointerException if the specified {@code Triangle} is {@code null}.
     */
    public static int getTriangleOutCodeConjunction(Triangle triangle) {
        Objects.requireNonNull(triangle, "triangle must not be null");
        int outCode = -1;
        outCode &= getOutCode(triangle.getV1().getClipSpacePosition());
        outCode &= getOutCode(triangle.getV2().getClipSpacePosition());
        outCode &= getOutCode(triangle.getV3().getClipSpacePosition());
        return outCode;
    }

    /**
     * Returns whether the specified {@linkplain Triangle} lies completely outside the clip volume.
     *
     * @param triangle the {@code Triangle} to check.
     * @return {@code true} if the specified {@code Triangle} lies completely outside the clip volume; {@code false} otherwise.
     * @throws NullPointerException if the specified {@code Triangle} is {@code null}.
     */
    public static boolean isTriangleCompletelyOutside(Triangle triangle) {
        return getTriangleOutCodeConjunction(triangle) != OutCode.INSIDE;
    }

    /**
     * Returns whether all specified {@linkplain Triangle Triangles} are completely outside the clip volume.
     *
     * @param triangles the {@code List} of {@code Triangles} to check.
     * @return {@code true} if all specified {@code Triangles} are completely outside the clip volume; {@code false} otherwise.
     * @throws NullPointerException if the specified {@code List} or any of the {@code Triangles} inside the
     *                              {@code List} are {@code null}.
     */
    public static boolean allTrianglesCompletelyOutside(List<Triangle> triangles) {
        Objects.requireNonNull(triangles, "triangles must not be null");
        boolean allOutside = true;
        for (Triangle triangle : triangles) {
            allOutside &= isTriangleCompletelyOutside(triangle);
        }
        return allOutside;
    }

    /**
     * Returns the disjunction (logical OR) of the {@linkplain OutCode OutCodes} of the three clip space positions of
     * the specified {@linkplain Triangle}.
     *
     * @param triangle the {@code Triangle} for which to calculate the disjunction of {@code OutCodes}.
     * @return the disjunction of {@code OutCodes} of the specified {@code Triangle}.
     * @throws NullPointerException if the specified {@code Triangle} is {@code null}.
     */
    public static int getTriangleOutCodeDisjunction(Triangle triangle) {
        Objects.requireNonNull(triangle, "triangle must not be null");
        int outCode = OutCode.INSIDE;
        outCode |= getOutCode(triangle.getV1().getClipSpacePosition());
        outCode |= getOutCode(triangle.getV2().getClipSpacePosition());
        outCode |= getOutCode(triangle.getV3().getClipSpacePosition());
        return outCode;
    }

    /**
     * Returns whether the specified {@linkplain Triangle} lies completely inside the clip volume.
     *
     * @param triangle the {@code Triangle} to check.
     * @return {@code true} if the specified {@code Triangle} lies completely inside the clip volume; {@code false} otherwise.
     * @throws NullPointerException if the specified {@code Triangle} is {@code null}.
     */
    public static boolean isTriangleCompletelyInside(Triangle triangle) {
        return getTriangleOutCodeDisjunction(triangle) == OutCode.INSIDE;
    }

    /**
     * Returns whether all specified {@linkplain Triangle Triangles} are completely inside the clip volume.
     *
     * @param triangles the {@code List} of {@code Triangles} to check.
     * @return {@code true} if all specified {@code Triangles} are completely inside the clip volume; {@code false} otherwise.
     * @throws NullPointerException if the specified {@code List} or any of the {@code Triangles} inside the
     *                              {@code List} are {@code null}.
     */
    public static boolean allTrianglesCompletelyInside(List<Triangle> triangles) {
        Objects.requireNonNull(triangles, "triangles must not be null");
        boolean allOutside = true;
        for (Triangle triangle : triangles) {
            allOutside &= isTriangleCompletelyInside(triangle);
        }
        return allOutside;
    }

    /**
     * Clips the specified {@linkplain Triangle} against the clip volume and returns a {@linkplain List} of clipped
     * {@code Triangles}.
     *
     * @param triangle the {@code Triangle to clip}.
     * @return a {@code List} of clipped {@code Triangles}.
     * @throws NullPointerException if the specified {@code Triangle} is {@code null}.
     */
    public static List<Triangle> clipTriangleAgainstClipVolume(Triangle triangle) {
        Objects.requireNonNull(triangle, "triangle must not be null");

        // Calculate out codes
        final int v1OutCode = getOutCode(triangle.getV1().getClipSpacePosition());
        final int v2OutCode = getOutCode(triangle.getV2().getClipSpacePosition());
        final int v3OutCode = getOutCode(triangle.getV3().getClipSpacePosition());

        // No clipping needed if whole triangle is outside
        if ((v1OutCode & v2OutCode & v3OutCode) != OutCode.INSIDE) {
            return Collections.emptyList();
        }

        // Calculate triangle out code
        final int triangleOutCode = v1OutCode | v2OutCode | v3OutCode;

        // No clipping needed if whole triangle is inside
        if (triangleOutCode == OutCode.INSIDE) {
            return List.of(triangle);
        }

        // Clip triangle vertices against clip volume
        List<Vertex> clippedVertices = clipVerticesAgainstClipVolume(triangle.getV1(), triangle.getV2(), triangle.getV3());

        // Create triangles from clipped vertices
        return PrimitiveAssembly.FAN.getTrianglesFromVertices(clippedVertices);
    }

    /**
     * Clips the specified {@linkplain Line} against the clip volume and returns the clipped {@code Line}.
     *
     * @param line the {@code Line} to clip.
     * @return the clipped {@code Line}.
     * @throws NullPointerException if the specified {@code Triangle} is {@code null}.
     */
    public static Line clipLineAgainstClipVolume(Line line) {
        Objects.requireNonNull(line, "line must not be null");

        // Calculate out codes
        final int v1OutCode = getOutCode(line.getV1().getClipSpacePosition());
        final int v2OutCode = getOutCode(line.getV2().getClipSpacePosition());

        // No clipping needed if whole line is outside
        if ((v1OutCode & v2OutCode) != OutCode.INSIDE) {
            return null;
        }

        // Calculate line out code
        final int lineOutCode = v1OutCode | v2OutCode;

        // No clipping needed if whole line is inside
        if (lineOutCode == OutCode.INSIDE) {
            return line;
        }

        // Clip line vertices against clip volume
        List<Vertex> clippedVertices = clipVerticesAgainstClipVolume(line.getV1(), line.getV2());

        // Check if enough vertices were returned to form a line
        if (clippedVertices.size() != 2) {
            return null;
        }

        // Create line from clipped vertices
        return new Line(clippedVertices.get(0), clippedVertices.get(1));
    }

    /**
     * Clips the specified {@linkplain List} of {@linkplain Vertex Vertices} against the clip volume and returns a
     * {@code List} of clipped {@code Vertices}.
     *
     * @param vertices the {@code List} of {@code Vertices} to clip.
     * @return a {@code List} of clipped {@code Vertices}.
     * @throws NullPointerException if the specified {@code List} of {@code Vertices} or any of the specified
     *                              {@code Vertices} are {@code null}.
     * @see <a href="https://github.com/BennyQBD/3DSoftwareRenderer/blob/master/src/RenderContext.java">Reference</a>
     */
    public static List<Vertex> clipVerticesAgainstClipVolume(List<Vertex> vertices) {
        List<Vertex> result = vertices;

        // Clip vertices along all axes
        for (int i = 0; i < 3; i++) {
            result = clipVerticesAlongClipAxis(result, i);

            // Return if no vertices left to clip
            if (result.isEmpty()) {
                return result;
            }
        }

        return result;
    }

    /**
     * Clips the specified varargs of {@linkplain Vertex Vertices} against the clip volume and returns a
     * {@linkplain List} of clipped {@code Vertices}.
     *
     * @param vertices the varargs of {@code Vertices} to clip.
     * @return a {@code List} of clipped {@code Vertices}.
     * @throws NullPointerException if the specified varargs of {@code Vertices} or any of the specified
     *                              {@code Vertices} are {@code null}.
     * @see <a href="https://github.com/BennyQBD/3DSoftwareRenderer/blob/master/src/RenderContext.java">Reference</a>
     */
    public static List<Vertex> clipVerticesAgainstClipVolume(Vertex... vertices) {
        return clipVerticesAgainstClipVolume(List.of(vertices));
    }

    /**
     * Clips the specified component of the specified {@linkplain List} of {@linkplain Vertex Vertices} against the clip
     * volume and returns a {@code List} of clipped {@code Vertices}.
     *
     * @param vertices        the {@code List} of {@code Vertices} to clip.
     * @param componentToClip the component to clip.
     * @return a {@code List} of clipped {@code Vertices}.
     * @throws NullPointerException if the specified {@code List} of {@code Vertices} or any of the specified
     *                              {@code Vertices} are {@code null}.
     * @see <a href="https://github.com/BennyQBD/3DSoftwareRenderer/blob/master/src/RenderContext.java">Reference</a>
     */
    public static List<Vertex> clipVerticesAlongClipAxis(List<Vertex> vertices, int componentToClip) {
        // Clip the positive side first
        List<Vertex> clippedVertices = clipVerticesAgainstClipPlane(vertices, componentToClip, ClipSide.POSITIVE);

        // Return if there are no more vertices left
        if (clippedVertices.isEmpty()) {
            return clippedVertices;
        }

        // Clip the negative side
        return clipVerticesAgainstClipPlane(clippedVertices, componentToClip, ClipSide.NEGATIVE);
    }

    /**
     * Clips the specified component of the specified {@linkplain List} of {@linkplain Vertex Vertices} against the
     * specified {@linkplain ClipSide} of the clip volume.
     *
     * @param vertices        the {@code List} of {@code Vertices} to clip.
     * @param componentToClip the component to clip.
     * @param clipSide        the {@code ClipSide} to clip against.
     * @return a {@code List} of clipped {@code Vertices}.
     * @throws NullPointerException if the specified {@code List} of {@code Vertices}, any of the specified
     *                              {@code Vertices}, or the {@code ClipSide} are {@code null}.
     * @see <a href="https://github.com/BennyQBD/3DSoftwareRenderer/blob/master/src/RenderContext.java">Reference</a>
     */
    public static List<Vertex> clipVerticesAgainstClipPlane(List<Vertex> vertices, int componentToClip, ClipSide clipSide) {
        Objects.requireNonNull(vertices, "vertices must not be null");
        Objects.requireNonNull(clipSide, "clipSide must not be null");

        // Return if there is nothing to clip
        if (vertices.isEmpty()) {
            return vertices;
        }

        // Initialize variables
        List<Vertex> result = new ArrayList<>();
        final int side = clipSide.getFactor();

        Vertex previousVertex = vertices.get(vertices.size() - 1);
        float previousComponent = previousVertex.getClipSpacePosition().get(componentToClip) * side;
        boolean previousInside = previousComponent <= previousVertex.getClipSpacePosition().getW();

        // Iterate through all vertices
        for (Vertex currentVertex : vertices) {
            float currentComponent = currentVertex.getClipSpacePosition().get(componentToClip) * side;
            boolean currentInside = currentComponent <= currentVertex.getClipSpacePosition().getW();

            // If one of the currently handled vertices is outside and the other inside then clip
            if (currentInside ^ previousInside) {
                // Calculate the interpolation factor alpha between the two vertices
                float alpha = (previousVertex.getClipSpacePosition().getW() - previousComponent)
                        / ((previousVertex.getClipSpacePosition().getW() - previousComponent)
                        - (currentVertex.getClipSpacePosition().getW() - currentComponent));
                // Add the result of interpolation to the result list
                result.add(previousVertex.lerp(currentVertex, alpha));
            }

            // Add the current vertex to the result list if it is inside
            if (currentInside) {
                result.add(currentVertex);
        }

            // Update the variables
            previousVertex = currentVertex;
            previousComponent = currentComponent;
            previousInside = currentInside;
        }

        return result;
    }
}