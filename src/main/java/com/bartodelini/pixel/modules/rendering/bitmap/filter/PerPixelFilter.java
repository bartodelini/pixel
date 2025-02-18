package com.bartodelini.pixel.modules.rendering.bitmap.filter;

import com.bartodelini.pixel.modules.rendering.bitmap.Bitmap;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * A <i>PerPixelFilter</i> is a {@linkplain PostProcessingFilter} which applies a fixed function for every pixel of a
 * {@linkplain Bitmap} in order to perform post-processing.
 *
 * @author Bartolini
 * @version 1.0
 */
public class PerPixelFilter implements PostProcessingFilter {

    private final Function<Integer, Integer> filterFunction;

    /**
     * Allocates a new {@code PerPixelFilter} by specifying the {@linkplain Function} which will be applied for every
     * pixel.
     *
     * @param filterFunction the {@code Function} which will be applied for every pixel.
     * @throws NullPointerException if the specified {@code Function} is {@code null}.
     */
    public PerPixelFilter(Function<Integer, Integer> filterFunction) {
        this.filterFunction = Objects.requireNonNull(filterFunction, "filterFunction must not be null");
    }

    @Override
    public void filter(Bitmap bitmap) {
        IntStream.range(0, bitmap.getPixels().length).parallel().forEach(i ->
                bitmap.getPixels()[i] = filterFunction.apply(bitmap.getPixels()[i]));
    }
}