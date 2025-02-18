package com.bartodelini.pixel.modules.rendering.components.model;

import com.bartodelini.pixel.math.vector.Vector2f;
import com.bartodelini.pixel.math.vector.Vector3f;

/**
 * A utility class providing functions useful functions for {@linkplain Mesh Meshes}.
 *
 * @author Bartolini
 * @version 1.0
 */
public final class MeshUtils {

    private MeshUtils() {
    }

    /**
     * Calculates the axis-aligned bounding box (AABB) of the specified {@linkplain Mesh}.
     *
     * @param mesh the {@code Mesh} to calculate the AABB for.
     * @return the AABB of the specified {@code Mesh}.
     */
    public static Mesh calculateAABB(Mesh mesh) {
        // Initialize variables
        float minX = Float.MAX_VALUE;
        float maxX = Float.MIN_VALUE;
        float minY = Float.MAX_VALUE;
        float maxY = Float.MIN_VALUE;
        float minZ = Float.MAX_VALUE;
        float maxZ = Float.MIN_VALUE;

        // Find the min and max coordinates along the x, y and z axes
        for (Vector3f vec : mesh.positions()) {
            if (minX > vec.getX()) {
                minX = vec.getX();
            }
            if (maxX < vec.getX()) {
                maxX = vec.getX();
            }
            if (minY > vec.getY()) {
                minY = vec.getY();
            }
            if (maxY < vec.getY()) {
                maxY = vec.getY();
            }
            if (minZ > vec.getZ()) {
                minZ = vec.getZ();
            }
            if (maxZ < vec.getZ()) {
                maxZ = vec.getZ();
            }
        }

        // Create the eight positions for the box corner points
        Vector3f[] positions = new Vector3f[8];
        positions[0] = new Vector3f(minX, minY, minZ);
        positions[1] = new Vector3f(maxX, minY, minZ);
        positions[2] = new Vector3f(maxX, minY, maxZ);
        positions[3] = new Vector3f(minX, minY, maxZ);
        positions[4] = new Vector3f(minX, maxY, minZ);
        positions[5] = new Vector3f(maxX, maxY, minZ);
        positions[6] = new Vector3f(maxX, maxY, maxZ);
        positions[7] = new Vector3f(minX, maxY, maxZ);

        // Construct the box faces
        Mesh.Face[] faces = new Mesh.Face[12];
        faces[0] = new Mesh.Face(
                new Mesh.Face.Point(0, 0, 0),
                new Mesh.Face.Point(1, 0, 0),
                new Mesh.Face.Point(2, 0, 0));
        faces[1] = new Mesh.Face(
                new Mesh.Face.Point(2, 0, 0),
                new Mesh.Face.Point(3, 0, 0),
                new Mesh.Face.Point(0, 0, 0));
        faces[2] = new Mesh.Face(
                new Mesh.Face.Point(0, 0, 0),
                new Mesh.Face.Point(7, 0, 0),
                new Mesh.Face.Point(4, 0, 0));
        faces[3] = new Mesh.Face(
                new Mesh.Face.Point(0, 0, 0),
                new Mesh.Face.Point(3, 0, 0),
                new Mesh.Face.Point(7, 0, 0));
        faces[4] = new Mesh.Face(
                new Mesh.Face.Point(7, 0, 0),
                new Mesh.Face.Point(2, 0, 0),
                new Mesh.Face.Point(6, 0, 0));
        faces[5] = new Mesh.Face(
                new Mesh.Face.Point(7, 0, 0),
                new Mesh.Face.Point(3, 0, 0),
                new Mesh.Face.Point(2, 0, 0));
        faces[6] = new Mesh.Face(
                new Mesh.Face.Point(6, 0, 0),
                new Mesh.Face.Point(1, 0, 0),
                new Mesh.Face.Point(5, 0, 0));
        faces[7] = new Mesh.Face(
                new Mesh.Face.Point(6, 0, 0),
                new Mesh.Face.Point(2, 0, 0),
                new Mesh.Face.Point(1, 0, 0));
        faces[8] = new Mesh.Face(
                new Mesh.Face.Point(5, 0, 0),
                new Mesh.Face.Point(0, 0, 0),
                new Mesh.Face.Point(4, 0, 0));
        faces[9] = new Mesh.Face(
                new Mesh.Face.Point(5, 0, 0),
                new Mesh.Face.Point(1, 0, 0),
                new Mesh.Face.Point(0, 0, 0));
        faces[10] = new Mesh.Face(
                new Mesh.Face.Point(4, 0, 0),
                new Mesh.Face.Point(6, 0, 0),
                new Mesh.Face.Point(5, 0, 0));
        faces[11] = new Mesh.Face(
                new Mesh.Face.Point(4, 0, 0),
                new Mesh.Face.Point(7, 0, 0),
                new Mesh.Face.Point(6, 0, 0));

        // Create arrays for the texture coordinates and normals and fill them solely with the zero vector
        Vector2f[] uvs = new Vector2f[]{Vector2f.ZERO};
        Vector3f[] normals = new Vector3f[]{Vector3f.ZERO};

        return new Mesh(positions, uvs, normals, faces, new Mesh.Line[0]);
    }
}