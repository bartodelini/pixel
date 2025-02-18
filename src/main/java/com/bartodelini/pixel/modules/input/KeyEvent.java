package com.bartodelini.pixel.modules.input;

import com.bartodelini.pixel.event.Event;
import com.bartodelini.pixel.event.EventType;

import java.util.Objects;

/**
 * A <i>KeyEvent</i> is an {@linkplain Event} which occurs when pressing or releasing a key.
 *
 * @author Bartolini
 * @version 1.2
 */
public class KeyEvent extends Event {

    /**
     * ANY represents a KeyEvent of any type. It is the root for all key events.
     */
    public static final EventType<KeyEvent> ANY = new EventType<>("ANY", EventType.ROOT);

    /**
     * KEY_PRESSED represents a KeyEvent of which occurs when pressing a key.
     */
    public static final EventType<KeyEvent> KEY_PRESSED = new EventType<>("KEY_PRESSED", ANY);

    /**
     * KEY_RELEASED represents a KeyEvent of which occurs when releasing a key.
     */
    public static final EventType<KeyEvent> KEY_RELEASED = new EventType<>("KEY_RELEASED", ANY);

    private final Key key;

    /**
     * Allocates a new {@code KeyEvent} object by passing in an {@linkplain EventType} and a {@linkplain Key}.
     *
     * @param eventType the {@code EventType} of the {@code KeyEvent}.
     * @param key   the {@code Key} of the {@code KeyEvent}.
     * @throws NullPointerException if the specified {@code EventType} or {@code Key} are {@code null}.
     */
    public KeyEvent(EventType<KeyEvent> eventType, Key key) {
        super(eventType);
        this.key = Objects.requireNonNull(key, "key must not be null");
    }

    /**
     * Returns the {@linkplain Key} of this {@code KeyEvent}.
     *
     * @return the {@code Key} of this {@code KeyEvent}.
     */
    public Key getKey() {
        return key;
    }
}