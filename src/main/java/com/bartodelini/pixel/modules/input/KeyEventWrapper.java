package com.bartodelini.pixel.modules.input;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * A <i>KeyEventWrapper</i> is used to listen to and map Java's AWT events to {@linkplain KeyEvent KeyEvents}.
 *
 * @author Bartolini
 * @version 1.0
 */
public class KeyEventWrapper implements KeyEventDispatcher {

    private final Queue<com.bartodelini.pixel.modules.input.KeyEvent> eventQueue = new ConcurrentLinkedQueue<>();

    @Override
    public boolean dispatchKeyEvent(java.awt.event.KeyEvent e) {
        switch (e.getID()) {
            // Create and add a KEY_PRESSED KeyEvent
            case KeyEvent.KEY_PRESSED -> eventQueue.add(new com.bartodelini.pixel.modules.input.KeyEvent(
                    com.bartodelini.pixel.modules.input.KeyEvent.KEY_PRESSED,
                    Key.getFromAWTKeyEvent(e)));

            // Create and add a KEY_RELEASED KeyEvent
            case KeyEvent.KEY_RELEASED -> eventQueue.add(new com.bartodelini.pixel.modules.input.KeyEvent(
                    com.bartodelini.pixel.modules.input.KeyEvent.KEY_RELEASED,
                    Key.getFromAWTKeyEvent(e)));
        }
        return false;
    }

    /**
     * Returns a {@linkplain List} of enqueued {@linkplain com.bartodelini.pixel.modules.input.KeyEvent KeyEvents}.
     *
     * @return a {@code List} of enqueued {@code KeyEvents}.
     */
    public List<com.bartodelini.pixel.modules.input.KeyEvent> pollEvents() {
        List<com.bartodelini.pixel.modules.input.KeyEvent> result = new LinkedList<>();
        int keyEventCount = eventQueue.size();
        for (int i = 0; i < keyEventCount; i++) {
            result.add(eventQueue.poll());
        }
        return result;
    }

    /**
     * Returns whether this {@code KeyEventWrapper} has enqueued events.
     *
     * @return {@code true} if this {@code KeyEventWrapper} has enqueues events.
     */
    public boolean hasEnqueuedEvents() {
        return !eventQueue.isEmpty();
    }
}