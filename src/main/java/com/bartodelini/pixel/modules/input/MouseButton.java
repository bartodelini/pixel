package com.bartodelini.pixel.modules.input;

import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A <i>MouseButton</i> is used to map Java's mouse button codes to more human-readable mnemonics.
 *
 * @author Bartolini
 * @version 1.2
 */
public enum MouseButton {


    /**
     * NONE represents {@linkplain MouseEvent#NOBUTTON}.
     */
    NONE("NONE", "None", MouseEvent.NOBUTTON),

    /**
     * LEFT represents {@linkplain MouseEvent#BUTTON1}.
     */
    LEFT("mouse1", "Left Mouse", MouseEvent.BUTTON1),

    /**
     * MIDDLE represents {@linkplain MouseEvent#BUTTON2}.
     */
    MIDDLE("mouse2", "Middle Mouse", MouseEvent.BUTTON2),

    /**
     * RIGHT represents {@linkplain MouseEvent#BUTTON3}.
     */
    RIGHT("mouse3", "Right Mouse", MouseEvent.BUTTON3),

    /**
     * RIGHT represents {@linkplain MouseEvent#BUTTON3}.
     */
    MOUSE4("mouse4", "Side Mouse 1", 4),

    /**
     * RIGHT represents {@linkplain MouseEvent#BUTTON3}.
     */
    MOUSE5("mouse5", "Side Mouse 2", 5);

    private static final Map<String, MouseButton> aliasMap = new HashMap<>();
    private static final Map<String, MouseButton> nameMap = new HashMap<>();
    private static final Map<Integer, MouseButton> valueMap = new HashMap<>();

    static {
        for (MouseButton mouseButton : MouseButton.values()) {
            aliasMap.put(mouseButton.getAlias(), mouseButton);
            nameMap.put(mouseButton.getName(), mouseButton);
            valueMap.put(mouseButton.getValue(), mouseButton);
        }
    }

    /**
     * Returns a {@code MouseButton} by its alias.
     *
     * @param alias the alias of the wished {@code MouseButton}.
     * @return the {@code MouseButton} with the matching alias, or {@code null} if no such {@code MouseButton} was found.
     * @throws NullPointerException if the specified alias is {@code null}.
     */
    public static MouseButton getByAlias(String alias) {
        Objects.requireNonNull(alias, "alias must not be null");
        return aliasMap.get(alias);
    }

    /**
     * Returns a {@code MouseButton} by its name.
     *
     * @param name the name of the wished {@code MouseButton}.
     * @return the {@code MouseButton} with the matching name, or {@code null} if no such {@code MouseButton} was found.
     * @throws NullPointerException if the specified name is {@code null}.
     */
    public static MouseButton getByName(String name) {
        Objects.requireNonNull(name, "name must not be null");
        return nameMap.get(name);
    }

    /**
     * Returns a {@code MouseButton} by its numerical value.
     *
     * @param value the numerical value of the wished {@code MouseButton}.
     * @return the {@code MouseButton} with the numerical value, or {@code null} if no such {@code MouseButton} was found.
     */
    public static MouseButton getByValue(int value) {
        return valueMap.get(value);
    }

    private final String alias;
    private final String name;
    private final int value;

    /**
     * Allocates a new {@code MouseButton} object by passing in an alias, a name, as well as a value.
     *
     * @param alias the alias used for this {@code MouseButton}.
     * @param name  the name used for this {@code MouseButton}.
     * @param value the value used for this {@code MouseButton}.
     * @throws NullPointerException if the specified alias or name are {@code null}.
     */
    MouseButton(String alias, String name, int value) {
        this.alias = Objects.requireNonNull(alias, "alias must not be null");
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.value = value;
    }

    /**
     * Returns the alias of this {@code MouseButton}.
     *
     * @return the alias of this {@code MouseButton}.
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Returns the name of this {@code MouseButton}.
     *
     * @return the name of this {@code MouseButton}.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the numerical value of this {@code MouseButton}.
     *
     * @return the numerical value of this {@code MouseButton}.
     */
    public int getValue() {
        return value;
    }
}