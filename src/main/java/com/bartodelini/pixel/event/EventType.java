package com.bartodelini.pixel.event;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * An <i>EventType</i> represents a specific event type associated with an {@code Event}.
 * <p>
 * Event types form a hierarchy with the {@linkplain EventType#ROOT} (or adequately {@linkplain Event#ANY}) as its
 * root. This is useful in {@linkplain EventHandler} registration where a single {@code EventHandler} can be registered
 * to a super event type and will be receiving all its subtype events as well.
 * <p>
 * Note that you cannot construct two different {@code EventType} objects with the same name and parent.
 *
 * @param <T> the event class to which this type applies.
 * @author Bartolini
 * @version 1.0
 */
public class EventType<T extends Event> {

    /**
     * The root event type. All other event types are subtypes of it. It is the only event type which has its super
     * event type set to {@code null}.
     */
    public static final EventType<Event> ROOT = new EventType<>();

    private final String name;
    private final EventType<? super T> superType;

    private Set<EventType<? extends T>> subTypes;

    /**
     * Allocates a new {@code EventType} by passing in its name and super type and registers it as a subtype of the
     * passed in super type.
     *
     * @param name      the name of this {@code EventType}.
     * @param superType the super type of this {@code EventType}.
     * @throws NullPointerException if the specified name or the super type is {@code null}.
     */
    public EventType(String name, EventType<? super T> superType) {
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.superType = Objects.requireNonNull(superType, "superType must not be null");
        this.superType.registerSubType(this);
    }

    /**
     * Allocates a new {@code EventType} by passing in its name and setting its super type to
     * {@linkplain EventType#ROOT}. This constructor has the same effect as
     * {@linkplain #EventType(String, EventType) EventType} {@code (name, null)}.
     *
     * @param name the name of this {@code EventType}.
     * @throws NullPointerException if the specified name is {@code null}.
     */
    public EventType(String name) {
        this(name, ROOT);
    }

    /**
     * Allocates a new {@code EventType} by setting its name to {@code "EVENT"} and its super type to {@code null}. This
     * constructor is only used to instantiate {@linkplain EventType#ROOT}.
     */
    private EventType() {
        this.name = "EVENT";
        this.superType = null;
    }

    /**
     * Returns the name of this {@code EventType}.
     *
     * @return the name of this {@code EventType}.
     */
    public final String getName() {
        return name;
    }

    /**
     * Returns the super type of this {@code EventType}.
     *
     * @return the super type of this {@code EventType}.
     */
    public final EventType<? super T> getSuperType() {
        return superType;
    }

    /**
     * Registers the passed in {@code EventType} as a subtype of this {@code EventType}. More formally, adds the passed
     * in {@code EventType} to the {@linkplain Set} of subtypes of this {@code EventType}.
     *
     * @param subType the {@code EventType} to be added as a subtype to this {@code EventType}.
     * @throws NullPointerException  if the passed in {@code EventType} is {@code null}.
     * @throws IllegalStateException if the passed in {@code EventType} is already a subtype of this {@code EventType}.
     */
    private void registerSubType(EventType<? extends T> subType) {
        Objects.requireNonNull(subType, "subType must not be null");
        if (subTypes == null) {
            subTypes = new HashSet<>();
            if (!subTypes.add(subType)) {
                throw new IllegalStateException("EventType '" + subType + "' with parent '"
                        + subType.getSuperType() + "' already exists.");
            }
        }
    }
}