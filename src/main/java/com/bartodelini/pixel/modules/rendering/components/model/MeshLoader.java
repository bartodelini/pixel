package com.bartodelini.pixel.modules.rendering.components.model;

import com.bartodelini.pixel.math.vector.Vector2f;
import com.bartodelini.pixel.math.vector.Vector3f;
import com.bartodelini.pixel.modules.asset.AssetLoader;
import com.bartodelini.pixel.modules.asset.AssetLoadingException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * A <i>MeshLoader</i> is an {@linkplain AssetLoader}, which is used for loading {@code .obj} files into
 * {@linkplain Mesh Meshes}.
 *
 * @author Bartolini
 * @version 1.0
 */
public class MeshLoader extends AssetLoader<Mesh> {

    /**
     * Allocates a new {@code MeshLoader} object.
     */
    public MeshLoader() {
        super(Mesh.class, ".obj");
    }

    @Override
    public Mesh loadAsset(InputStream inputStream) throws AssetLoadingException {
        List<Vector3f> positions = new ArrayList<>();
        List<Vector2f> uvs = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();

        List<Mesh.Face> faces = new ArrayList<>();
        List<Mesh.Line> lines = new ArrayList<>();

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.length() < 2) {
                    continue;
                }
                String[] lineSplit = line.split(" ");
                switch (lineSplit[0]) {
                    case "v":
                        if (lineSplit.length < 4) {
                            continue;
                        }
                        positions.add(new Vector3f(
                                Float.parseFloat(lineSplit[1]),
                                Float.parseFloat(lineSplit[2]),
                                Float.parseFloat(lineSplit[3])));
                        break;
                    case "vt":
                        if (lineSplit.length < 3) {
                            continue;
                        }
                        uvs.add(new Vector2f(
                                Float.parseFloat(lineSplit[1]),
                                Float.parseFloat(lineSplit[2])));
                        break;
                    case "vn":
                        if (lineSplit.length < 4) {
                            continue;
                        }
                        normals.add(new Vector3f(
                                Float.parseFloat(lineSplit[1]),
                                Float.parseFloat(lineSplit[2]),
                                Float.parseFloat(lineSplit[3])));
                        break;
                    case "f":
                        if (lineSplit.length < 4) {
                            continue;
                        }
                        for (int i = 0; i < lineSplit.length - 3; i++) {
                            Mesh.Face face = new Mesh.Face(
                                    getPointFromString(lineSplit[1]),
                                    getPointFromString(lineSplit[2 + i]),
                                    getPointFromString(lineSplit[3 + i]));
                            if (!positions.isEmpty() && !uvs.isEmpty()) {
                                face = calculateTangents(positions, uvs, face);
                            }
                            faces.add(face);
                        }
                        break;
                    case "l":
                        // TODO: Add line loading.
                        break;
                }
            }
            if (normals.isEmpty()) {
                normals.add(Vector3f.ZERO);
            }
            if (uvs.isEmpty()) {
                uvs.add(Vector2f.ZERO);
            }
            return new Mesh(
                    positions.toArray(new Vector3f[0]),
                    uvs.toArray(new Vector2f[0]),
                    normals.toArray(new Vector3f[0]),
                    faces.toArray(new Mesh.Face[0]),
                    lines.toArray(new Mesh.Line[0]));
        } catch (IOException e) {
            throw new AssetLoadingException("could not load mesh", e);
        }
    }

    /**
     * A helper method used to construct a
     * {@linkplain Mesh.Face.Point} from a string.
     *
     * @param str the input string.
     * @return the {@code Point} constructed from the input string.
     */
    private Mesh.Face.Point getPointFromString(String str) {
        String[] splitData = str.split("/");
        if (splitData.length == 3) {
            return new Mesh.Face.Point(
                    Integer.parseInt(splitData[0]) - 1,
                    Integer.parseInt(splitData[1]) - 1,
                    Integer.parseInt(splitData[2]) - 1,
                    null);
        } else if (splitData.length == 2) {
            return new Mesh.Face.Point(
                    Integer.parseInt(splitData[0]) - 1,
                    Integer.parseInt(splitData[1]) - 1,
                    0,
                    null);
        } else {
            return new Mesh.Face.Point(
                    Integer.parseInt(splitData[0]) - 1,
                    0,
                    0,
                    null);
        }
    }

    /**
     * A helper method used to calculate a tangent for a
     * {@linkplain Mesh.Face}.
     *
     * @param positions the {@code List} of positions.
     * @param uvs       the {@code List} of texture coordinates.
     * @param face      the {@code Face} to calculate the tangents for.
     * @return a {@code Face} with the calculated tangent.
     * @see <a href="https://www.dropbox.com/sh/man3j36w56tnkiq/AAAmAMOvz4Ik2rlEo-2VBylKa
     * /normalMappingObjConverter?preview=NormalMappedObjLoader.java">Reference</a>
     */
    private Mesh.Face calculateTangents(List<Vector3f> positions, List<Vector2f> uvs, Mesh.Face face) {
        // Retrieve positions
        Vector3f v1Pos = positions.get(face.p1().positionIndex());
        Vector3f v2Pos = positions.get(face.p2().positionIndex());
        Vector3f v3Pos = positions.get(face.p3().positionIndex());

        // Calculate deltas
        Vector3f deltaPos1 = v2Pos.subtract(v1Pos);
        Vector3f deltaPos2 = v3Pos.subtract(v1Pos);
        Vector2f uv1 = uvs.get(face.p1().uvIndex());
        Vector2f uv2 = uvs.get(face.p2().uvIndex());
        Vector2f uv3 = uvs.get(face.p3().uvIndex());
        Vector2f deltaUV1 = uv2.subtract(uv1);
        Vector2f deltaUV2 = uv3.subtract(uv1);

        // Calculate tangent
        float r = 1.0f / (deltaUV1.getX() * deltaUV2.getY() - deltaUV1.getY() * deltaUV2.getX());
        deltaPos1 = deltaPos1.scale(deltaUV2.getY());
        deltaPos2 = deltaPos2.scale(deltaUV1.getY());
        Vector3f tangent = deltaPos1.subtract(deltaPos2);
        tangent = tangent.scale(r);

        // Recalculate points
        Mesh.Face.Point p1 = new Mesh.Face.Point(face.p1().positionIndex(), face.p1().uvIndex(), face.p1().normalIndex(), tangent);
        Mesh.Face.Point p2 = new Mesh.Face.Point(face.p2().positionIndex(), face.p2().uvIndex(), face.p2().normalIndex(), tangent);
        Mesh.Face.Point p3 = new Mesh.Face.Point(face.p3().positionIndex(), face.p3().uvIndex(), face.p3().normalIndex(), tangent);

        return new Mesh.Face(p1, p2, p3);
    }
}