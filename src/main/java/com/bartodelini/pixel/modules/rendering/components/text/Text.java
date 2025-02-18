package com.bartodelini.pixel.modules.rendering.components.text;

import com.bartodelini.pixel.ecs.Component;
import com.bartodelini.pixel.modules.rendering.bitmap.font.BitmapFont;

import java.util.Objects;

/**
 * A <i>Text</i> is a {@linkplain Component} which holds information about a renderable text object in the scene.
 *
 * @author Bartolini
 * @version 1.0
 */
public class Text extends Component {

    private String text;
    private BitmapFont.TextAlignment alignment;
    private int color;

    /**
     * Allocates a new {@code Text} by passing in its text content, alignment and color.
     *
     * @param text      the text string used for the content.
     * @param alignment the alignment of the text.
     * @param color     the color of the text.
     * @throws NullPointerException if the specified text content or alignment are {@code null}.
     */
    public Text(String text, BitmapFont.TextAlignment alignment, int color) {
        this.text = Objects.requireNonNull(text, "text must not be null");
        this.alignment = Objects.requireNonNull(alignment, "alignment must not be null");
        this.color = color;
    }

    /**
     * Allocates a new {@code Text} by passing in its text content and alignment. This constructor has the same effect
     * as {@linkplain #Text(String, BitmapFont.TextAlignment, int) Text} {@code (text, alignment, 0xffffff)}.
     *
     * @param text      the text string used for the content.
     * @param alignment the alignment of the text.
     * @throws NullPointerException if the specified text content or alignment are {@code null}.
     */
    public Text(String text, BitmapFont.TextAlignment alignment) {
        this(text, alignment, 0xffffff);
    }

    /**
     * Allocates a new {@code Text} by passing in its text content and color. This constructor has the same effect as
     * {@linkplain #Text(String, BitmapFont.TextAlignment, int) Text}
     * {@code (text, BitmapFont.TextAlignment.CENTER, color)}.
     *
     * @param text  the text string used for the content.
     * @param color the color of the text.
     * @throws NullPointerException if the specified text content is {@code null}.
     */
    public Text(String text, int color) {
        this(text, BitmapFont.TextAlignment.CENTER, color);
    }

    /**
     * Allocates a new {@code Text} by passing in its text content. This constructor has the same effect as
     * {@linkplain #Text(String, BitmapFont.TextAlignment, int) Text}
     * {@code (text, BitmapFont.TextAlignment.CENTER, 0xffffff)}.
     *
     * @param text the text string used for the content.
     * @throws NullPointerException if the specified text content is {@code null}.
     */
    public Text(String text) {
        this(text, BitmapFont.TextAlignment.CENTER, 0xffffff);
    }

    /**
     * Returns the text content of this {@code Text}.
     *
     * @return the text content of this {@code Text}.
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text content of this {@code Text}.
     *
     * @param text the new text content of this {@code Text}.
     * @throws NullPointerException if the specified text content is {@code null}.
     */
    public void setText(String text) {
        this.text = Objects.requireNonNull(text, "text must not be null");
    }

    /**
     * Returns the {@linkplain BitmapFont.TextAlignment} of this
     * {@code Text}.
     *
     * @return the {@code TextAlignment} of this {@code Text}.
     */
    public BitmapFont.TextAlignment getAlignment() {
        return alignment;
    }

    /**
     * Sets the {@linkplain BitmapFont.TextAlignment} of this
     * {@code Text}.
     *
     * @param alignment the new {@code TextAlignment} of this {@code Text}.
     * @throws NullPointerException if the specified {@code TextAlignment} is {@code null}.
     */
    public void setAlignment(BitmapFont.TextAlignment alignment) {
        this.alignment = Objects.requireNonNull(alignment, "alignment must not be null");
    }

    /**
     * Returns the color of this {@code Text}.
     *
     * @return the color of this {@code Text}.
     */
    public int getColor() {
        return color;
    }

    /**
     * Sets the color of this {@code Text}.
     *
     * @param color the new color of this {@code Text}.
     */
    public void setColor(int color) {
        this.color = color;
    }
}