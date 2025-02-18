package com.bartodelini.pixel.modules.input;

import com.bartodelini.pixel.event.Event;
import com.bartodelini.pixel.event.EventType;
import com.bartodelini.pixel.math.vector.Vector2f;

/**
 * A <i>MouseWheelEvent</i> is an {@linkplain MouseEvent} which occurs while scrolling.
 *
 * @author Bartolini
 * @version 1.0
 */
public class MouseWheelEvent extends MouseEvent {

    /**
     * MOUSE_SCROLLED represents a MouseWheelEvent of which occurs while scrolling.
     */
    public static final EventType<MouseWheelEvent> MOUSE_SCROLLED = new EventType<>("MOUSE_SCROLLED", MouseEvent.ANY);

    private final int wheelRotation;
    private final double preciseWheelRotation;

    /**
     * Allocates a new {@code MouseWheelEvent} object by passing in an {@linkplain EventType}, the
     * {@linkplain MouseButton} concerned with this event, the current pointer position, the number of increments the
     * mouse wheel was rotated by as well as the precise number of increments the mouse wheel was rotated by.
     *
     * @param type                 the {@code EventType} of the {@code MouseWheelEvent}.
     * @param mouseButton          the {@code MouseButton} of this {@code MouseEvent}.
     * @param mousePosition        the pointer position at the time of the event.
     * @param wheelRotation        the number of increments the mouse wheel was rotated by.
     * @param preciseWheelRotation the precise number of increments the mouse wheel was rotated by.
     * @throws NullPointerException if the specified {@code EventType}, {@code MouseButton}, or pointer position are
     *                              {@code null}.
     */
    public MouseWheelEvent(EventType<? extends Event> type, MouseButton mouseButton, Vector2f mousePosition,
                           int wheelRotation, double preciseWheelRotation) {
        super(type, mouseButton, mousePosition);
        this.wheelRotation = wheelRotation;
        this.preciseWheelRotation = preciseWheelRotation;
    }

    /**
     * Returns the number of increments the mouse wheel was rotated by.
     *
     * @return the number of increments the mouse wheel was rotated by.
     */
    public int getWheelRotation() {
        return wheelRotation;
    }

    /**
     * Returns the precise number of increments the mouse wheel was rotated by.
     *
     * @return the precise number of increments the mouse wheel was rotated by.
     */
    public double getPreciseWheelRotation() {
        return preciseWheelRotation;
    }
}