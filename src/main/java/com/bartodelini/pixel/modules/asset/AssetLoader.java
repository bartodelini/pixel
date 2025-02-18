package com.bartodelini.pixel.modules.asset;

import java.io.InputStream;
import java.util.Objects;
import java.util.Set;

/**
 * An <i>AssetLoader</i> is used to load an asset of a specified type.
 *
 * @param <T> the type of the loaded asset.
 * @author Bartolini
 * @version 1.0
 */
public abstract class AssetLoader<T> {

    private final Class<T> assetType;
    private final Set<String> extensions;

    /**
     * Allocates a new {@code AssetLoader} by passing in the {@linkplain Class} representing the asset type this
     * {@code AssetLoader} supports, as well as the supported file extensions.
     *
     * @param assetType  the {@code Class} representing the type this {@code AssetLoader} supports.
     * @param extensions the file extensions this {@code AssetLoader} supports.
     * @throws NullPointerException     if the specified assetType or any of the specified extensions are {@code null}.
     * @throws IllegalArgumentException if the specified extensions contain duplicates.
     */
    public AssetLoader(Class<T> assetType, String... extensions) {
        this.assetType = Objects.requireNonNull(assetType, "assetType must not be null");
        this.extensions = Set.of(extensions);
    }

    /**
     * Returns the loaded asset from the passed in {@linkplain InputStream}.
     *
     * @param inputStream the {@code InputStream} to load the asset from.
     * @return the loaded asset from the specified {@code InputStream}.
     * @throws AssetLoadingException if the asset could not be loaded.
     */
    public abstract T loadAsset(InputStream inputStream) throws AssetLoadingException;

    /**
     * Returns the {@linkplain Class} representing the supported asset type.
     *
     * @return the {@code Class} representing the supported asset type.
     */
    public Class<T> getAssetType() {
        return assetType;
    }

    /**
     * Returns an unmodifiable {@linkplain Set} of supported file extensions.
     *
     * @return an unmodifiable {@code Set} of supported file extensions.
     */
    public Set<String> getExtensions() {
        return extensions;
    }
}