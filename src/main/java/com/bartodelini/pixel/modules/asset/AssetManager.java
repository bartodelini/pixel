package com.bartodelini.pixel.modules.asset;

import com.bartodelini.pixel.core.EngineModule;
import com.bartodelini.pixel.core.ModuleInitializeException;
import com.bartodelini.pixel.logging.Logger;
import com.bartodelini.pixel.logging.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOError;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

/**
 * An <i>AssetManager</i> is an {@linkplain EngineModule} responsible for loading and providing assets.
 *
 * @author Bartolini
 * @version 1.1
 */
public class AssetManager extends EngineModule {

    private static final Map<Class<?>, Map<String, Object>> assetMap = new HashMap<>();

    private static boolean initialized = false;

    private final Logger logger = LoggerFactory.getLogger(this);
    private final Map<String, AssetLoader<?>> assetLoaderMap = new HashMap<>();
    private final Path assetsPath;

    /**
     * Allocates a new {@code AssetManager} by passing in the root {@linkplain Path} of the assets, as well as the
     * {@linkplain AssetLoader AssetLoaders}.
     *
     * @param assetsPath   the root {@code Path} of the assets.
     * @param assetLoaders the {@code AssetLoaders} to be used by this {@code AssetManager}.
     * @throws NullPointerException     if the specified assetsPath or any of the specified {@code AssetLoaders} are
     *                                  {@code null}.
     * @throws IllegalArgumentException if there are duplicates in the specified {@code AssetLoaders}, or if multiple of
     *                                  the specified {@code AssetLoaders} share supported file extensions.
     */
    public AssetManager(Path assetsPath, AssetLoader<?>... assetLoaders) {
        super("Asset Manager", "asset");
        this.assetsPath = Objects.requireNonNull(assetsPath, "assetPath must not be null");

        // Check for duplicate or null AssetLoaders
        Set<AssetLoader<?>> assetLoaderSet = Set.of(assetLoaders);

        // Check for duplicate supported file extensions
        Set<String> extensionSet = new HashSet<>();
        for (AssetLoader<?> assetLoader : assetLoaderSet) {
            for (String extension : assetLoader.getExtensions()) {
                if (!extensionSet.add(extension)) {
                    throw new IllegalArgumentException("specified AssetLoaders share supported file extensions");
                }
            }
        }

        // Add AssetLoaders
        for (AssetLoader<?> assetLoader : assetLoaders) {
            addAssetLoader(assetLoader);
        }
    }

    @Override
    public void initialize() throws ModuleInitializeException {
        int totalAssets = 0;
        try (Stream<Path> assetPaths = Files.walk(assetsPath)) {
            // Iterate through all files in the specified assets path
            for (Path path : assetPaths.filter(Files::isRegularFile).toList()) {
                // Get file name and format it accordingly
                String assetName = path.subpath(assetsPath.getNameCount(), path.getNameCount()).toString();
                assetName = assetName.replace('\\', '/');

                // Ignore file with no file extension
                if (!assetName.contains(".")) {
                    continue;
                }

                // Get file extension
                String fileExtension = assetName.substring(assetName.lastIndexOf("."));

                // Get according AssetLoader for file extension
                AssetLoader<?> assetLoader = assetLoaderMap.get(fileExtension);

                // Ignore file if no AssetLoader was found for it
                if (assetLoader == null) {
                    continue;
                }

                // Load asset and add it to assetMap
                Object asset = assetLoader.loadAsset(new FileInputStream(path.toAbsolutePath().toString()));
                logger.info("Loaded " + assetName);
                totalAssets++;
                assetMap.computeIfAbsent(asset.getClass(), c -> new HashMap<>()).put(assetName, asset);
            }
        } catch (SecurityException | IOException | IOError | AssetLoadingException e) {
            throw new ModuleInitializeException(this, "error reading assets at '" + assetsPath + "'", e);
        }

        logger.fine("Finished loading " + totalAssets + " assets.");
        initialized = true;
    }

    /**
     * Helper method used to add the passed in {@linkplain AssetLoader} to the {@linkplain Map} of supported
     * {@code AssetLoaders}.
     *
     * @param assetLoader the {@code AssetLoader} to add.
     * @throws NullPointerException if the specified {@code AssetLoader} is {@code null};
     */
    private void addAssetLoader(AssetLoader<?> assetLoader) {
        Objects.requireNonNull(assetLoader, "assetLoader must not be null");
        assetLoader.getExtensions().forEach(extension -> assetLoaderMap.put(extension, assetLoader));
    }

    /**
     * Returns whether the {@code AssetManager} is initialized.
     *
     * @return {@code true} if the {@code AssetManager} is initialized; {@code false} otherwise.
     */
    public static boolean isInitialized() {
        return initialized;
    }

    /**
     * Returns an asset by passing in the {@linkplain Class} representing its type, as well as its name.
     *
     * @param clazz the {@code Class} representing the type of the asset.
     * @param name  the name of the asset.
     * @param <T>   the type of the asset.
     * @return the asset, cast to the type.
     * @throws IllegalStateException    if the {@code AssetManager} is not initialized.
     * @throws NullPointerException     if the specified {@code Class} or name are {@code null}.
     * @throws IllegalArgumentException if no asset of the specified type and with the specified name exists.
     */
    public static <T> T getAsset(Class<T> clazz, String name) {
        if (!isInitialized()) {
            throw new IllegalStateException("cannot retrieve asset; AssetManager is not initialized");
        }
        Objects.requireNonNull(clazz, "clazz must not be null");
        Objects.requireNonNull(name, "name must not be null");
        Map<String, Object> typeMap = assetMap.get(clazz);
        if (typeMap == null) {
            throw new IllegalArgumentException("no asset of type '" + clazz + "' found");
        }
        T asset = clazz.cast(typeMap.get(name));
        if (asset == null) {
            throw new IllegalArgumentException("no asset of type '" + clazz + "' with the name '" + name + "' found");
        }
        return asset;
    }
}