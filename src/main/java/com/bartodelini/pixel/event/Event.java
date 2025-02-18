package com.bartodelini.pixel.event;

import java.util.Objects;

/**
 * An <i>Event</i> is a simplified version of the {@code Event} found in the <b>JavaFX</b> library.
 * <p>
 * It is the base class for all events.
 *
 * @author Bartolini
 * @version 1.0
 */
public class Event {

    /**
     * The supertype for all events.
     */
    public static final EventType<Event> ANY = EventType.ROOT;

    private final EventType<? extends Event> type;

    /**
     * Allocates a new {@code Event} object by passing in its {@linkplain EventType}.
     *
     * @param type the {@code EventType} of this {@code Event}.
     * @throws NullPointerException if the specified {@code EventType} if {@code null}.
     */
    public Event(EventType<? extends Event> type) {
        this.type = Objects.requireNonNull(type, "type must not be null");
    }

    /**
     * Returns the {@linkplain EventType} of this {@code Event}.
     *
     * @return the {@code EventType} of this {@code Event}.
     */
    public EventType<? extends Event> getType() {
        return type;
    }
}