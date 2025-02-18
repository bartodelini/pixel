package com.bartodelini.pixel.event;

import java.util.*;

/**
 * An <i>EventBus</i> allows for registration of {@linkplain EventHandler EventHandlers} and the correct dispatching of
 * {@linkplain Event Events} to the registered handlers.
 *
 * @author Bartolini
 * @version 1.0
 */
public class EventBus {

    private final Map<EventType<? extends Event>, Set<EventHandler<? extends Event>>> eventHandlerMap;

    /**
     * Allocates a new {@code EventBus} object.
     */
    public EventBus() {
        eventHandlerMap = new HashMap<>();
    }

    /**
     * Dispatches the passed in {@linkplain Event} to all the {@linkplain EventHandler EventHandlers}, registered for
     * its {@linkplain EventType} and for all the supertypes of its type, all the way up to {@linkplain EventType#ROOT},
     * or adequately {@linkplain Event#ANY}.
     *
     * @param event the passed in {@code Event} to be dispatched.
     * @throws NullPointerException if the passed in {@code Event} is {@code null}.
     */
    @SuppressWarnings("unchecked")
    public void dispatchEvent(final Event event) {
        Objects.requireNonNull(event, "event must not be null");
        EventType<? extends Event> eventType = event.getType();
        do {
            Optional.ofNullable(eventHandlerMap.get(eventType))
                    .ifPresent(s -> s.forEach(h -> ((EventHandler<Event>) h).handle(event)));
            eventType = eventType.getSuperType();
        } while (eventType != null);
    }

    /**
     * Registers the passed in {@linkplain EventHandler} by adding it to the {@linkplain Set} of all handlers mapped
     * onto the specified {@linkplain EventType}.
     *
     * @param eventType    the {@code EventType} which the passed in {@code EventHandler} will be mapped onto.
     * @param eventHandler the {@code EventHandler} which will be mapped onto the passed {@code EventType}.
     * @param <T>          the specific {@code Event} class of the {@code EventHandler}.
     * @throws NullPointerException if the specified {@code EventType} or {@code EventHandler} is {@code null}.
     */
    public <T extends Event> void addEventHandler(
            final EventType<T> eventType,
            final EventHandler<T> eventHandler) {
        Objects.requireNonNull(eventType, "eventType must not be null");
        Objects.requireNonNull(eventHandler, "eventHandler must not be null");
        eventHandlerMap.computeIfAbsent(eventType, k -> new LinkedHashSet<>()).add(eventHandler);
    }

    /**
     * Removes the passed in {@linkplain EventHandler} by removing it from the {@linkplain  Set} of all handlers mapped
     * onto the specified {@linkplain EventType}.
     *
     * @param eventType    the {@code EventType} which the passed in {@code EventHandler} is mapped onto.
     * @param eventHandler the {@code EventHandler} to be removed.
     * @param <T>          the specific {@code Event} class of the {@code EventHandler}.
     * @throws NullPointerException if the specified {@code EventType} or {@code EventHandler} is {@code null}.
     */
    public <T extends Event> void removeEventHandler(
            final EventType<T> eventType,
            final EventHandler<T> eventHandler) {
        Objects.requireNonNull(eventType, "eventType must not be null");
        Objects.requireNonNull(eventHandler, "eventHandler must not be null");
        Optional.ofNullable(eventHandlerMap.get(eventType)).ifPresent(s -> s.remove(eventHandler));
    }
}