package com.bartodelini.pixel.modules.input;

import com.bartodelini.pixel.event.Event;
import com.bartodelini.pixel.event.EventType;
import com.bartodelini.pixel.math.vector.Vector2f;

import java.util.Objects;

/**
 * A <i>MouseEvent</i> is an {@linkplain Event} which occurs on various mouse actions, such as pressing a key or moving
 * the pointer.
 *
 * @author Bartolini
 * @version 1.2
 */
public class MouseEvent extends Event {

    /**
     * ANY represents a MouseEvent of any type. It is the root for all mouse events.
     */
    public static final EventType<MouseEvent> ANY = new EventType<>("ANY", EventType.ROOT);

    /**
     * MOUSE_PRESSED represents a MouseEvent which occurs when pressing a mouse button.
     */
    public static final EventType<MouseEvent> MOUSE_PRESSED = new EventType<>("MOUSE_PRESSED", ANY);

    /**
     * MOUSE_RELEASED represents a MouseEvent which occurs when releasing a mouse button.
     */
    public static final EventType<MouseEvent> MOUSE_RELEASED = new EventType<>("MOUSE_RELEASED", ANY);

    /**
     * MOUSE_MOVED represents a MouseEvent which occurs when the mouse pointer is moved.
     */
    public static final EventType<MouseEvent> MOUSE_MOVED = new EventType<>("MOUSE_MOVED", ANY);

    /**
     * MOUSE_ENTERED represents a MouseEvent which occurs when the mouse pointer enters the focussed area.
     */
    public static final EventType<MouseEvent> MOUSE_ENTERED = new EventType<>("MOUSE_ENTERED", ANY);

    /**
     * MOUSE_EXITED represents a MouseEvent which occurs when the mouse pointer exits the focussed area.
     */
    public static final EventType<MouseEvent> MOUSE_EXITED = new EventType<>("MOUSE_EXITED", ANY);

    /**
     * MOUSE_DRAGGED represents a MouseEvent which occurs when the mouse pointer moves while a button is pressed.
     */
    public static final EventType<MouseEvent> MOUSE_DRAGGED = new EventType<>("MOUSE_DRAGGED", ANY);

    private final Vector2f mousePosition;
    private final MouseButton mouseButton;
    private final int clickCount;

    /**
     * Allocates a new {@code MouseEvent} object by passing in its {@linkplain EventType}, {@linkplain MouseButton},
     * the current pointer position, as well as the click count.
     *
     * @param type          the {@code EventType} of the {@code MouseEvent}.
     * @param mouseButton   the {@code MouseButton} of this {@code MouseEvent}.
     * @param mousePosition the pointer position at the time of the event.
     * @param clickCount    the clickCount of this {@code MouseEvent}.
     * @throws NullPointerException if the specified {@code EventType}, {@code MouseButton}, or pointer position are
     *                              {@code null}.
     */
    public MouseEvent(EventType<? extends Event> type, MouseButton mouseButton, Vector2f mousePosition, int clickCount) {
        super(type);
        this.mouseButton = Objects.requireNonNull(mouseButton, "mouseButton must not be null");
        this.mousePosition = Objects.requireNonNull(mousePosition, "mousePosition must not be null");
        this.clickCount = clickCount;
    }

    /**
     * Allocates a new {@code MouseEvent} object by passing in its {@linkplain EventType}, the current pointer
     * position, as well as the click count. This constructor has the same effect as
     * {@linkplain #MouseEvent(EventType, MouseButton, Vector2f, int) MouseEvent}
     * {@code (type, MouseButton.NONE, mousePosition, clickCount)}.
     *
     * @param type          the {@code EventType} of the {@code MouseEvent}.
     * @param mousePosition the pointer position at the time of the event.
     * @param clickCount    the clickCount of this {@code MouseEvent}.
     * @throws NullPointerException if the specified {@code EventType} or pointer position are {@code null}.
     */
    public MouseEvent(EventType<? extends Event> type, Vector2f mousePosition, int clickCount) {
        this(type, MouseButton.NONE, mousePosition, clickCount);
    }

    /**
     * Allocates a new {@code MouseEvent} object by passing in its {@linkplain EventType}, {@linkplain MouseButton}, as
     * well as the current pointer position.This constructor has the same effect as
     * {@linkplain #MouseEvent(EventType, MouseButton, Vector2f, int) MouseEvent}
     * {@code (type, mouseButton, mousePosition, 0)}.
     *
     * @param type          the {@code EventType} of the {@code MouseEvent}.
     * @param mouseButton   the {@code MouseButton} of this {@code MouseEvent}.
     * @param mousePosition the pointer position at the time of the event.
     * @throws NullPointerException if the specified {@code EventType}, {@code MouseButton}, or pointer position are
     *                              {@code null}.
     */
    public MouseEvent(EventType<? extends Event> type, MouseButton mouseButton, Vector2f mousePosition) {
        this(type, mouseButton, mousePosition, 0);
    }

    /**
     * Allocates a new {@code MouseEvent} object by passing in its {@linkplain EventType} and the current pointer
     * position. This constructor has the same effect as
     * {@linkplain #MouseEvent(EventType, MouseButton, Vector2f, int) MouseEvent}
     * {@code (type, MouseButton.NONE, mousePosition, 0)}.
     *
     * @param type          the {@code EventType} of the {@code MouseEvent}.
     * @param mousePosition the pointer position at the time of the event.
     * @throws NullPointerException if the specified {@code EventType} or pointer position are {@code null}.
     */
    public MouseEvent(EventType<? extends Event> type, Vector2f mousePosition) {
        this(type, MouseButton.NONE, mousePosition, 0);
    }

    /**
     * Returns the mouse position at the time of this {@code MouseEvent}.
     *
     * @return the mouse position at the time of this {@code MouseEvent}.
     */
    public Vector2f getMousePosition() {
        return mousePosition;
    }

    /**
     * Returns the {@linkplain MouseButton} of this {@code MouseEvent}.
     *
     * @return the {@code MouseButton} of this {@code MouseEvent}.
     */
    public MouseButton getMouseButton() {
        return mouseButton;
    }

    /**
     * Returns the number of clicks associated with this {@code MouseEvent}.
     *
     * @return the number of clicks associated with this {@code MouseEvent}.
     */
    public int getClickCount() {
        return clickCount;
    }
}