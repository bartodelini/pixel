package com.bartodelini.pixel.modules.rendering.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * A utility class providing useful functions for working on images.
 *
 * @author Bartolini
 * @version 1.0
 */
public class ImageUtils {

    /**
     * A {@linkplain DateTimeFormatter} used when creating names for saved images.
     */
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh-mm-ss_SSS")
            .withZone(ZoneId.systemDefault());

    /**
     * Saves the specified {@linkplain BufferedImage} to the disk.
     *
     * @param image the {@code BufferedImage} to save.
     * @throws NullPointerException if the specified {@code BufferedImage} is {@code null}.
     * @throws RuntimeException     if an error occurs when writing the image.
     */
    public static void saveImage(BufferedImage image) {
        Objects.requireNonNull(image, "image must not be null");
        File file = new File(formatter.format(Instant.now()) + ".png");
        try {
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}