package com.bartodelini.pixel.modules.rendering.bitmap.filter;

import com.bartodelini.pixel.modules.rendering.bitmap.Bitmap;

/**
 * A <i>PostProcessingFilter</i> can be used to apply post-processing effects to a {@linkplain Bitmap}.
 *
 * @author Bartolini
 * @version 1.0
 */
@FunctionalInterface
public interface PostProcessingFilter {

    /**
     * Applies post-processing effects to the specified {@linkplain Bitmap}.
     *
     * @param bitmap the bitmap to process.
     */
    void filter(Bitmap bitmap);
}