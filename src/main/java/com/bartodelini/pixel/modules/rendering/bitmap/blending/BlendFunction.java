package com.bartodelini.pixel.modules.rendering.bitmap.blending;

import com.bartodelini.pixel.modules.rendering.bitmap.Colors;

/**
 * A <i>BlendFunction</i> is used for blending between fragments which are not opaque.
 *
 * @author Bartolini
 * @version 1.0
 */
@FunctionalInterface
public interface BlendFunction {

    /**
     * The default {@code BlendingFunction}. Returns a linear interpolation between source and destination with the
     * factor {@code (1 - (source >> 24 & 0xff) / 255.0f)}.
     */
    BlendFunction DEFAULT = (source, destination) -> {
        int[] colors1 = Colors.getARGB(source);
        int[] colors2 = Colors.getARGB(destination);

        float alpha = colors1[0] / 255.0f;
        colors1[0] = Math.round(255 * (alpha + (1 - alpha) * (colors2[0] / 255.0f)));
        colors1[1] = Math.round(colors1[1] * alpha + colors2[1] * (1 - alpha));
        colors1[2] = Math.round(colors1[2] * alpha + colors2[2] * (1 - alpha));
        colors1[3] = Math.round(colors1[3] * alpha + colors2[3] * (1 - alpha));

        return Colors.getColor(colors1);
    };

    /**
     * Returns the result of blending the {@code source} and {@code destination} colors. Here source denotes the newly
     * rendered color and destination is the color currently stored in the framebuffer.
     *
     * @param source      the newly rendered color.
     * @param destination the color currently stored in the framebuffer.
     * @return the result of blending the {@code source} and {@code destination}.
     */
    int blend(int source, int destination);
}