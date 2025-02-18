package com.bartodelini.pixel.modules.rendering.components.shader.textures;

import com.bartodelini.pixel.math.MathUtils;
import com.bartodelini.pixel.math.vector.Vector2f;
import com.bartodelini.pixel.modules.rendering.bitmap.Bitmap;
import com.bartodelini.pixel.modules.rendering.bitmap.Colors;

/**
 * A <i>TextureSampler</i> is used to sample a texture coordinate from a {@linkplain Bitmap}.
 *
 * @author Bartolini
 * @version 1.0
 */
@FunctionalInterface
public interface TextureSampler {

    /**
     * The simplest {@code TextureSampler}. Retrieves the texel by simply accessing the {@linkplain Bitmap} at the
     * specified texture coordinate after wrapping it using the {@linkplain UVWrapper} of the {@linkplain Texture}.
     */
    TextureSampler NEAREST = (texture, uv) -> {
        // Wrap uv coordinates
        Vector2f wrappedUV = texture.getUVWrapper().wrap(uv);

        // Calculate texel coordinates
        final float u = wrappedUV.getX() * texture.getBitmap().getWidth() - 0.5f;
        final float v = (1 - wrappedUV.getY()) * texture.getBitmap().getHeight() - 0.5f;
        final int xp = MathUtils.clamp(Math.round(u), 0, texture.getBitmap().getWidth() - 1);
        final int yp = MathUtils.clamp(Math.round(v), 0, texture.getBitmap().getHeight() - 1);

        // Return texel value
        return texture.getBitmap().getPixel(xp, yp);
    };

    /**
     * Returns the bilinear interpolation of four texel values, adjacent to the specified texture coordinate.
     */
    TextureSampler LINEAR = (texture, uv) -> {
        // Calculate texel offsets
        final float u = uv.getX() * texture.getBitmap().getWidth() - 0.5f;
        final int uRounded = Math.round(u);
        final float du = u - uRounded;
        final float v = (1 - uv.getY()) * texture.getBitmap().getHeight() - 0.5f;
        final int vRounded = Math.round(v);
        final float dv = v - vRounded;

        // Calculate interpolation constants
        final int uOffset = du < 0 ? -1 : 0;
        final float uAlpha = (du < 0) ? du + 1 : du;
        final int vOffset = dv < 0 ? -1 : 0;
        final float vAlpha = (dv < 0) ? dv + 1 : dv;

        // Calculate texel coordinates
        int[] us = new int[2];
        us[0] = texture.getUVWrapper().wrapU(uRounded + uOffset, texture.getBitmap().getWidth());
        us[1] = texture.getUVWrapper().wrapU(uRounded + uOffset + 1, texture.getBitmap().getWidth());
        int[] vs = new int[2];
        vs[0] = texture.getUVWrapper().wrapV(vRounded + vOffset, texture.getBitmap().getHeight());
        vs[1] = texture.getUVWrapper().wrapV(vRounded + vOffset + 1, texture.getBitmap().getHeight());

        // Retrieve colors of adjacent texels
        int[][] colors = new int[2][2];
        colors[0][0] = texture.getBitmap().getPixel(us[0], vs[0]);
        colors[0][1] = texture.getBitmap().getPixel(us[0], vs[1]);
        colors[1][0] = texture.getBitmap().getPixel(us[1], vs[0]);
        colors[1][1] = texture.getBitmap().getPixel(us[1], vs[1]);

        // Interpolate colors
        int[] verticalColors = new int[2];
        verticalColors[0] = Colors.interpolateARGB(colors[0][0], colors[1][0], uAlpha);
        verticalColors[1] = Colors.interpolateARGB(colors[0][1], colors[1][1], uAlpha);

        // Return texel value
        return Colors.interpolateARGB(verticalColors[0], verticalColors[1], vAlpha);
    };

    /**
     * Samples the specified {@linkplain Texture} at the specified texture coordinate.
     *
     * @param texture the {@code Texture} to sample from.
     * @param uv      the texture coordinate to sample at.
     * @return the sampled texel color.
     */
    int sample(Texture texture, Vector2f uv);
}