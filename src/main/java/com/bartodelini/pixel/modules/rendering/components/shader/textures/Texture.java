package com.bartodelini.pixel.modules.rendering.components.shader.textures;

import com.bartodelini.pixel.math.vector.Vector2f;
import com.bartodelini.pixel.modules.rendering.bitmap.Bitmap;

import java.util.Objects;

/**
 * A <i>Texture</i> is a {@linkplain Bitmap}, equipped with a {@linkplain UVWrapper} and a {@linkplain TextureSampler}.
 *
 * @author Bartolini
 * @version 1.0
 */
public class Texture {

    private final Bitmap bitmap;
    private UVWrapper uvWrapper;
    private TextureSampler textureSampler;

    /**
     * Allocates a new {@code Texture} by passing in its {@linkplain Bitmap}, {@linkplain UVWrapper} and its
     * {@linkplain TextureSampler}.
     *
     * @param bitmap         the {@code Bitmap} used for this {@code Texture}.
     * @param uvWrapper      the {@code UVWrapper} used for this {@code Texture}.
     * @param textureSampler the {@code TextureSampler} used for this {@code Texture}.
     * @throws NullPointerException if any of the specified parameters is {@code null}.
     */
    public Texture(Bitmap bitmap, UVWrapper uvWrapper, TextureSampler textureSampler) {
        this.bitmap = Objects.requireNonNull(bitmap, "bitmap must not be null");
        this.uvWrapper = Objects.requireNonNull(uvWrapper, "uvWrapper must not be null");
        this.textureSampler = Objects.requireNonNull(textureSampler, "textureSampler must not be null");
    }

    /**
     * Allocates a new {@code Texture} by passing in its {@linkplain Bitmap} and its {@linkplain UVWrapper}. This
     * constructor has the same effect as {@linkplain #Texture(Bitmap, UVWrapper, TextureSampler) Texture}
     * {@code (bitmap, uvWrapper, TextureSampler.NEAREST)}.
     *
     * @param bitmap    the {@code Bitmap} used for this {@code Texture}.
     * @param uvWrapper the {@code UVWrapper} used for this {@code Texture}.
     * @throws NullPointerException if any of the specified parameters is {@code null}.
     */
    public Texture(Bitmap bitmap, UVWrapper uvWrapper) {
        this(bitmap, uvWrapper, TextureSampler.NEAREST);
    }

    /**
     * Allocates a new {@code Texture} by passing in its {@linkplain Bitmap} and its {@linkplain TextureSampler}. This
     * constructor has the same effect as {@linkplain #Texture(Bitmap, UVWrapper, TextureSampler) Texture}
     * {@code (bitmap, UVWrapper.REPEAT, textureSampler)}.
     *
     * @param bitmap         the {@code Bitmap} used for this {@code Texture}.
     * @param textureSampler the {@code TextureSampler} used for this {@code Texture}.
     * @throws NullPointerException if any of the specified parameters is {@code null}.
     */
    public Texture(Bitmap bitmap, TextureSampler textureSampler) {
        this(bitmap, UVWrapper.REPEAT, textureSampler);
    }

    /**
     * Allocates a new {@code Texture} by passing in its {@linkplain Bitmap}. This
     * constructor has the same effect as {@linkplain #Texture(Bitmap, UVWrapper, TextureSampler) Texture}
     * {@code (bitmap, UVWrapper.REPEAT, TextureSampler.NEAREST)}.
     *
     * @param bitmap the {@code Bitmap} used for this {@code Texture}.
     * @throws NullPointerException if the specified {@code Bitmap} is {@code null}.
     */
    public Texture(Bitmap bitmap) {
        this(bitmap, UVWrapper.REPEAT, TextureSampler.NEAREST);
    }

    /**
     * Returns the {@linkplain Bitmap} used by this {@code Texture}.
     *
     * @return the {@code Bitmap} used by this {@code Texture}.
     */
    public Bitmap getBitmap() {
        return bitmap;
    }

    /**
     * Returns the {@linkplain UVWrapper} used by this {@code Texture}.
     *
     * @return the {@code UVWrapper} used by this {@code Texture}.
     */
    public UVWrapper getUVWrapper() {
        return uvWrapper;
    }

    /**
     * Sets the {@linkplain UVWrapper} for this {@code Texture}.
     *
     * @param uvWrapper the new {@code UVWrapper} for this {@code Texture}.
     * @throws NullPointerException if the specified {@code UVWrapper} is {@code null}.
     */
    public void setUVWrapper(UVWrapper uvWrapper) {
        this.uvWrapper = Objects.requireNonNull(uvWrapper, "uvWrapper must not be null");
    }

    /**
     * Returns the {@linkplain TextureSampler} used by this {@code Texture}.
     *
     * @return the {@code TextureSampler} used by this {@code Texture}.
     */
    public TextureSampler getTextureSampler() {
        return textureSampler;
    }

    /**
     * Sets the {@linkplain TextureSampler} for this {@code Texture}.
     *
     * @param textureSampler the new {@code TextureSampler} for this {@code Texture}.
     * @throws NullPointerException if the specified {@code TextureSampler} is {@code null}.
     */
    public void setTextureSampler(TextureSampler textureSampler) {
        this.textureSampler = textureSampler;
    }

    /**
     * Returns the texel at the specified position {@linkplain Vector2f} using the {@linkplain Bitmap} and the
     * {@linkplain TextureSampler} of this {@code Texture}.
     *
     * @param uv the texture coordinates of the texel to return.
     * @return the texel at the specified position.
     * @throws NullPointerException if the specified texture coordinates are {@code null}.
     */
    public int getTexel(Vector2f uv) {
        return textureSampler.sample(this, uv);
    }

    /**
     * Returns the texel at the specified position using the {@linkplain Bitmap} and the {@linkplain TextureSampler} of
     * this {@code Texture}.
     *
     * @param u the u texture coordinate of the texel to return.
     * @param v the v texture coordinate of the texel to return.
     * @return the texel at the specified position.
     */
    public int getTexel(float u, float v) {
        return getTexel(new Vector2f(u, v));
    }
}