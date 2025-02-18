package com.bartodelini.pixel.event;

/**
 * An <i>EventHandler</i> is used to define actions for a specific {@linkplain Event}.
 *
 * @param <T> the {@code Event} this handler can handle.
 * @author Bartolini
 * @version 1.0
 */
public interface EventHandler<T extends Event> {

    /**
     * Called when a specific {@linkplain Event} of the {@linkplain EventType} is dispatched for which this
     * {@code EventHandler} is registered.
     *
     * @param event the dispatched {@code Event}.
     */
    void handle(T event);
}