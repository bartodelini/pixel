package com.bartodelini.pixel.modules.asset;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * A <i>StringLoader</i> is an {@linkplain AssetLoader} which is used for loading ".txt" files into
 * {@linkplain String Strings}.
 *
 * @author Bartolini
 * @version 1.0
 */
public class StringLoader extends AssetLoader<String> {

    /**
     * Allocates a new {@code StringLoader} object.
     */
    public StringLoader() {
        super(String.class, ".txt");
    }

    @Override
    public String loadAsset(InputStream inputStream) throws AssetLoadingException {
        try {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new AssetLoadingException("could not load the asset", e);
        }
    }
}