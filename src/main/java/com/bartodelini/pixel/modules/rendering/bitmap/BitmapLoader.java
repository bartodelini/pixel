package com.bartodelini.pixel.modules.rendering.bitmap;

import com.bartodelini.pixel.modules.asset.AssetLoader;
import com.bartodelini.pixel.modules.asset.AssetLoadingException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * A <i>BitmapLoader</i> is an {@linkplain AssetLoader} which is used for loading {@code .png}, {@code .jpeg},
 * {@code .jpg} and {@code .bmp} files into {@linkplain Bitmap Bitmaps}.
 *
 * @author Bartolini
 * @version 1.0
 */
public class BitmapLoader extends AssetLoader<Bitmap> {

    /**
     * Allocates a new {@code BitmapLoader} object.
     */
    public BitmapLoader() {
        super(Bitmap.class, ".png", ".jpeg", ".jpg", ".bmp");
    }

    @Override
    public Bitmap loadAsset(InputStream inputStream) throws AssetLoadingException {
        try {
            BufferedImage image = ImageIO.read(inputStream);
            int width = image.getWidth();
            int height = image.getHeight();
            Bitmap result = new Bitmap(width, height);
            image.getRGB(0, 0, width, height, result.getPixels(), 0, width);

            return result;
        } catch (IllegalArgumentException | IOException e) {
            throw new AssetLoadingException("error reading image data", e);
        }
    }
}