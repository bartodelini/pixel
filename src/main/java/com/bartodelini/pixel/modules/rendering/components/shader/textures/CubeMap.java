package com.bartodelini.pixel.modules.rendering.components.shader.textures;

import com.bartodelini.pixel.math.vector.Vector3f;

import java.util.Objects;

/**
 * A <i>CubeMap</i> is a set of six {@linkplain Texture Textures} arranged in a cube manner, which can be accessed
 * using a {@linkplain Vector3f}.
 *
 * @author Bartolini
 * @version 1.0
 */
public class CubeMap {

    /**
     * The index of the {@linkplain Texture} representing the face along the positive x-axis of the {@code CubeMap}.
     */
    public static final int POSITIVE_X = 0;

    /**
     * The index of the {@linkplain Texture} representing the face along the negative x-axis of the {@code CubeMap}.
     */
    public static final int NEGATIVE_X = 1;

    /**
     * The index of the {@linkplain Texture} representing the face along the positive y-axis of the {@code CubeMap}.
     */
    public static final int POSITIVE_Y = 2;

    /**
     * The index of the {@linkplain Texture} representing the face along the negative y-axis of the {@code CubeMap}.
     */
    public static final int NEGATIVE_Y = 3;

    /**
     * The index of the {@linkplain Texture} representing the face along the positive z-axis of the {@code CubeMap}.
     */
    public static final int POSITIVE_Z = 4;

    /**
     * The index of the {@linkplain Texture} representing the face along the negative z-axis of the {@code CubeMap}.
     */
    public static final int NEGATIVE_Z = 5;

    private final Texture[] textures = new Texture[6];

    /**
     * Allocates a new {@code CubeMap} by passing in its six {@linkplain Texture Textures}.
     *
     * @param positiveX the {@code Texture} of the face along the positive x-axis of the {@code CubeMap}.
     * @param negativeX the {@code Texture} of the face along the negative x-axis of the {@code CubeMap}.
     * @param positiveY the {@code Texture} of the face along the positive y-axis of the {@code CubeMap}.
     * @param negativeY the {@code Texture} of the face along the negative y-axis of the {@code CubeMap}.
     * @param positiveZ the {@code Texture} of the face along the positive z-axis of the {@code CubeMap}.
     * @param negativeZ the {@code Texture} of the face along the negative z-axis of the {@code CubeMap}.
     * @throws NullPointerException if any of the specified {@code Textures} is {@code null}.
     */
    public CubeMap(
            Texture positiveX, Texture negativeX,
            Texture positiveY, Texture negativeY,
            Texture positiveZ, Texture negativeZ) {
        textures[POSITIVE_X] = Objects.requireNonNull(positiveX, "positiveX must not be null");
        textures[NEGATIVE_X] = Objects.requireNonNull(negativeX, "negativeX must not be null");
        textures[POSITIVE_Y] = Objects.requireNonNull(positiveY, "positiveY must not be null");
        textures[NEGATIVE_Y] = Objects.requireNonNull(negativeY, "negativeY must not be null");
        textures[POSITIVE_Z] = Objects.requireNonNull(positiveZ, "positiveZ must not be null");
        textures[NEGATIVE_Z] = Objects.requireNonNull(negativeZ, "negativeZ must not be null");
    }

    /**
     * Returns the texel which corresponds to the specified position.
     *
     * @param x the x-coordinate of the specified position.
     * @param y the y-coordinate of the specified position.
     * @param z the z-coordinate of the specified position.
     * @return the texel, which corresponds to the specified position.
     * @see <a href="https://www.gamedev.net/forums/topic
     * /687535-implementing-a-cube-map-lookup-function/5337472/">Reference</a>
     */
    public int getTexel(float x, float y, float z) {
        float absX = Math.abs(x);
        float absY = Math.abs(y);
        float absZ = Math.abs(z);

        float ma;
        float u;
        float v;
        int faceIndex;
        if (absZ >= absX && absZ >= absY) {
            faceIndex = z < 0 ? 5 : 4;
            ma = 0.5f / absZ;

            u = z < 0 ? -x : x;
            v = -y;
        } else if (absY >= absX) {
            faceIndex = y < 0 ? 3 : 2;
            ma = 0.5f / absY;

            u = x;
            v = y < 0 ? -z : z;
        } else {
            faceIndex = x < 0 ? 1 : 0;
            ma = 0.5f / absX;

            u = x < 0 ? z : -z;
            v = -y;
        }

        u = u * ma + 0.5f;
        v = v * ma + 0.5f;

        return textures[faceIndex].getTexel(u, -v);
    }

    /**
     * Returns the texel which corresponds to the specified position {@linkplain Vector3f}.
     *
     * @param coords the position to return the texel for.
     * @return the texel, which corresponds to the specified position.
     * @throws NullPointerException if the specified {@code Vertex3f} is {@code null}.
     * @see <a href="https://www.gamedev.net/forums/topic
     * /687535-implementing-a-cube-map-lookup-function/5337472/">Reference</a>
     */
    public int getTexel(Vector3f coords) {
        Objects.requireNonNull(coords, "coords must not be null");
        return getTexel(coords.getX(), coords.getY(), coords.getZ());
    }

    /**
     * Returns the {@linkplain Texture} of this {@code CubeMap} with the specified index.
     *
     * @param textureIndex the index of the {@code Texture} to return.
     * @return the {@code Texture} of this {@code CubeMap} with the specified index.
     */
    public Texture getTexture(int textureIndex) {
        return textures[textureIndex];
    }
}