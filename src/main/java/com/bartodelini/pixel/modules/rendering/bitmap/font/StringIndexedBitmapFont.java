package com.bartodelini.pixel.modules.rendering.bitmap.font;

import com.bartodelini.pixel.modules.asset.AssetManager;
import com.bartodelini.pixel.modules.rendering.bitmap.Bitmap;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A <i>StringIndexedBitmapFont</i> is a {@linkplain BitmapFont} which is defined using a {@linkplain Bitmap} containing
 * its glyphs, as well as a string which defines the characters supported by this {@code BitmapFont}.
 *
 * @author Bartolini
 * @version 1.0
 * @see <a href="https://github.com/skeeto/Minicraft/blob/master/src/com/mojang/ld22/gfx/Font.java">Reference</a>.
 */
public class StringIndexedBitmapFont implements BitmapFont {

    /**
     * The default {@code StringIndexedBitmapFont}.
     */
    public static final BitmapFont DEFAULT_FONT = new StringIndexedBitmapFont(
            "textures/font.png", 6, 8,
            """
                    ABCDEFGHIJKLMNOPQRSTUVWXYZ
                    abcdefghijklmnopqrstuvwxyz
                    0123456789`-=~!@#$%^&*()_+
                    []\\{}|;':",./<>?""");


    private final Map<Character, Bitmap> charMap = new HashMap<>();
    private final BitmapFontMetrics fontMetrics;

    /**
     * Allocates a new {@code StringIndexedBitmapFont} object by passing in the name of the {@linkplain Bitmap}
     * containing the glyphs of this font, as well as the character width, height and the string containing the order of
     * characters.
     *
     * @param bitmapName the name of the {@code Bitmap} containing the glyphs of this font.
     * @param charWidth  the width of the characters of this font.
     * @param charHeight the height of the characters of this font.
     * @param chars      the string containing the order of characters of this font.
     * @throws NullPointerException     if the specified name of the bitmap or the index string are {@code null}.
     * @throws IllegalArgumentException if the specified character width or character height are negative.
     */
    public StringIndexedBitmapFont(String bitmapName, int charWidth, int charHeight, String chars) {
        Objects.requireNonNull(bitmapName, "bitmapName must not be null");
        if (charWidth <= 0) {
            throw new IllegalArgumentException("charWidth must be positive");
        }
        if (charHeight <= 0) {
            throw new IllegalArgumentException("charHeight must be positive");
        }
        Objects.requireNonNull(chars, "chars must not be null");
        if (chars.isEmpty()) {
            throw new IllegalArgumentException("chars must not be empty");
        }

        // Retrieve the font Bitmap and cut it to character Bitmaps
        Bitmap[][] font = AssetManager.getAsset(Bitmap.class, "textures/font.png")
                .getSubBitmaps(charWidth, charHeight);

        // Get the dimensions of the charIndex
        String[] splitChars = chars.split("\n");
        int charsCountX = splitChars[0].length();
        int charsCountY = splitChars.length;

        // Populate the char map
        for (int y = 0; y < charsCountY; y++) {
            for (int x = 0; x < charsCountX; x++) {
                // Get the index for the current char
                int charIndex = x + y * charsCountX;

                // Break if charIndex is bigger than the number of defined chars (-charsCountY because "\n" is ignored)
                if (charIndex >= chars.length() - charsCountY) break;
                // Map the char onto its matching char Bitmap (+y to account for the missing "\n")
                charMap.put(chars.charAt(charIndex + y), font[x][y]);
            }
        }

        // Create font metrics
        this.fontMetrics = new BitmapFontMetrics() {
            @Override
            public int getCharacterWidth() {
                return charWidth;
            }

            @Override
            public int getCharacterHeight() {
                return charHeight;
            }

            @Override
            public int stringWidth(String str) {
                return str.length() * charWidth;
            }

            @Override
            public int stringHeight(String str) {
                return charHeight;
            }
        };
    }

    @Override
    public Bitmap getBitmapForChar(char c) {
        return charMap.get(c);
    }

    @Override
    public BitmapFontMetrics getBitmapFontMetrics() {
        return fontMetrics;
    }
}