package com.bartodelini.pixel.modules.rendering.components.shader.textures;

import com.bartodelini.pixel.math.MathUtils;
import com.bartodelini.pixel.math.vector.Vector2f;

/**
 * A <i>UVWrapper</i> is used to wrap a texture coordinate.
 *
 * @author Bartolini
 * @version 1.0
 */
public interface UVWrapper {

    /**
     * Repeats the {@linkplain Texture} by performing a modulo operation on the specified texture coordinate.
     */
    UVWrapper REPEAT = new UVWrapper() {
        @Override
        public Vector2f wrap(Vector2f uv) {
            return new Vector2f(
                    MathUtils.mod(uv.getX(), 1),
                    MathUtils.mod(uv.getY(), 1));
        }

        @Override
        public int wrapU(int u, int textureWidth) {
            return MathUtils.mod(u, textureWidth);
        }

        @Override
        public int wrapV(int v, int textureHeight) {
            return MathUtils.mod(v, textureHeight);
        }
    };

    /**
     * Mirrors the {@linkplain Texture}.
     * @see <a href="https://registry.khronos.org/OpenGL/specs/gl/glspec46.core.pdf">Reference</a>
     */
    UVWrapper MIRRORED_REPEAT = new UVWrapper() {
        @Override
        public Vector2f wrap(Vector2f uv) {
            float u = uv.getX();
            float v = uv.getY();

            if (Math.floor(u) % 2 == 0) {
                u = MathUtils.mod(uv.getX(), 1);
            } else {
                u = MathUtils.mod(1 - uv.getX(), 1);
            }

            if (Math.floor(v) % 2 == 0) {
                v = MathUtils.mod(uv.getY(), 1);
            } else {
                v = MathUtils.mod(1 - uv.getY(), 1);
            }

            return new Vector2f(u, v);
        }

        @Override
        public int wrapU(int u, int textureWidth) {
            return mirroredRepeat(u, textureWidth);
        }

        @Override
        public int wrapV(int v, int textureHeight) {
            return mirroredRepeat(v, textureHeight);
        }

        private int mirroredRepeat(int w, int length) {
            return (length - 1) - mirror(MathUtils.mod(w, 2 * length) - length);
        }

        private int mirror(int a) {
            return a >= 0 ? a : -(1 + a);
        }
    };

    /**
     * Clamps the {@linkplain Texture} by clamping the specified texture coordinates between 0 and 1.
     */
    UVWrapper CLAMP_TO_EDGE = new UVWrapper() {
        @Override
        public Vector2f wrap(Vector2f uv) {
            return new Vector2f(
                    MathUtils.clamp(uv.getX(), 0, 1),
                    MathUtils.clamp(uv.getY(), 0, 1));
        }

        @Override
        public int wrapU(int u, int textureWidth) {
            return MathUtils.clamp(u, 0, textureWidth - 1);
        }

        @Override
        public int wrapV(int v, int textureHeight) {
            return MathUtils.clamp(v, 0, textureHeight - 1);
        }
    };

    /**
     * Wraps the specified texture coordinate {@linkplain Vector2f}.
     *
     * @param uv the {@code Vector2f} representing the texture coordinate.
     * @return the {@code Vector2f} representing the wrapped texture coordinate.
     */
    Vector2f wrap(Vector2f uv);

    /**
     * Wraps the specified u texture coordinate using the specified texture width.
     *
     * @param u the u texture coordinate.
     * @return the wrapped u texture coordinate.
     */
    int wrapU(int u, int textureWidth);

    /**
     * Wraps the specified v texture coordinate using the specified texture height.
     *
     * @param v the v texture coordinate.
     * @return the wrapped v texture coordinate.
     */
    int wrapV(int v, int textureHeight);
}