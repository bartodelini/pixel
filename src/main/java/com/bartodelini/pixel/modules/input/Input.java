package com.bartodelini.pixel.modules.input;

import com.bartodelini.pixel.core.EngineModule;
import com.bartodelini.pixel.math.vector.Vector2f;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * An <i>Input</i> is an {@linkplain EngineModule} responsible for dispatching the default AWT input events to custom
 * {@linkplain KeyEvent KeyEvents} and {@linkplain MouseEvent MouseEvents}, as well as providing the support for
 * querying of key and mouse states.
 *
 * @author Bartolini
 * @version 1.1
 */
public class Input extends EngineModule {

    private static final HashMap<Key, State> keyStateMap = new HashMap<>();
    private static final HashMap<MouseButton, State> mouseButtonStateMap = new HashMap<>();

    /**
     * A <i>KeyState</i> represents the current state of a key or mouse button.
     */
    private enum State {
        PRESSED,
        DOWN,
        RELEASED,
        UP
    }

    private static Vector2f lastRelativeMousePosition = Vector2f.ZERO;
    private static int mouseWheelRotation = 0;
    private static double mouseWheelPreciseRotation = 0;
    private static boolean initialized = false;

    private final KeyEventWrapper keyEventWrapper = new KeyEventWrapper();
    private final Queue<Key> keysToPress = new LinkedList<>();
    private final Queue<Key> keysToRelease = new LinkedList<>();
    private final Queue<MouseEvent> mouseEventQueue = new ConcurrentLinkedQueue<>();
    private final Queue<MouseButton> mouseButtonsToPress = new LinkedList<>();
    private final Queue<MouseButton> mouseButtonsToRelease = new LinkedList<>();

    private List<KeyEvent> keyEvents;
    private List<MouseEvent> mouseEvents;

    /**
     * Allocates a new {@code Input} object.
     */
    public Input() {
        super("Input", "input");
    }

    @Override
    public void initialize() {
        // Add keyboard functionality
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyEventWrapper);

        // Add mouse functionality
        Toolkit.getDefaultToolkit().addAWTEventListener(event -> {
            java.awt.event.MouseEvent mouseEvent = (java.awt.event.MouseEvent) event;

            // Handle MOUSE_PRESSED event
            if (event.getID() == java.awt.event.MouseEvent.MOUSE_PRESSED) {
                mouseEventQueue.add(new MouseEvent(
                        MouseEvent.MOUSE_PRESSED,
                        MouseButton.getByValue(mouseEvent.getButton()),
                        new Vector2f(mouseEvent.getX(), mouseEvent.getY()),
                        mouseEvent.getClickCount()));
            }
            // Handle MOUSE_RELEASED event
            if (event.getID() == java.awt.event.MouseEvent.MOUSE_RELEASED) {
                mouseEventQueue.add(new MouseEvent(
                        MouseEvent.MOUSE_RELEASED,
                        MouseButton.getByValue(mouseEvent.getButton()),
                        new Vector2f(mouseEvent.getX(), mouseEvent.getY()),
                        mouseEvent.getClickCount()));
            }
            // Handle MOUSE_ENTERED event
            if (event.getID() == java.awt.event.MouseEvent.MOUSE_ENTERED) {
                mouseEventQueue.add(new MouseEvent(
                        MouseEvent.MOUSE_ENTERED,
                        MouseButton.getByValue(mouseEvent.getButton()),
                        new Vector2f(mouseEvent.getX(), mouseEvent.getY()),
                        mouseEvent.getClickCount()));
            }
            // Handle MOUSE_EXITED event
            if (event.getID() == java.awt.event.MouseEvent.MOUSE_EXITED) {
                mouseEventQueue.add(new MouseEvent(
                        MouseEvent.MOUSE_EXITED,
                        MouseButton.getByValue(mouseEvent.getButton()),
                        new Vector2f(mouseEvent.getX(), mouseEvent.getY()),
                        mouseEvent.getClickCount()));
            }
            // Handle MOUSE_MOVED event
            if (event.getID() == java.awt.event.MouseEvent.MOUSE_MOVED) {
                mouseEventQueue.add(new MouseEvent(
                        MouseEvent.MOUSE_MOVED,
                        MouseButton.getByValue(mouseEvent.getButton()),
                        new Vector2f(mouseEvent.getX(), mouseEvent.getY()),
                        mouseEvent.getClickCount()));
            }
            // Handle MOUSE_DRAGGED event
            if (event.getID() == java.awt.event.MouseEvent.MOUSE_DRAGGED) {
                mouseEventQueue.add(new MouseEvent(
                        MouseEvent.MOUSE_DRAGGED,
                        MouseButton.getByValue(mouseEvent.getButton()),
                        new Vector2f(mouseEvent.getX(), mouseEvent.getY()),
                        mouseEvent.getClickCount()));
            }
            // Handle MOUSE_WHEEL event
            if (event.getID() == java.awt.event.MouseEvent.MOUSE_WHEEL) {
                java.awt.event.MouseWheelEvent mouseWheelEvent = (java.awt.event.MouseWheelEvent) mouseEvent;
                mouseEventQueue.add(new MouseWheelEvent(
                        MouseWheelEvent.MOUSE_SCROLLED,
                        MouseButton.getByValue(mouseEvent.getButton()),
                        lastRelativeMousePosition,
                        mouseWheelEvent.getWheelRotation(), mouseWheelEvent.getPreciseWheelRotation()));
            }
        }, AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.MOUSE_WHEEL_EVENT_MASK);

        initialized = true;
    }

    @Override
    public void update(double deltaTime) {
        // Press keys
        while (!keysToPress.isEmpty()) {
            keyStateMap.put(keysToPress.poll(), State.DOWN);
        }

        // Release keys
        while (!keysToRelease.isEmpty()) {
            keyStateMap.put(keysToRelease.poll(), State.UP);
        }

        // Press mouse buttons
        while (!mouseButtonsToPress.isEmpty()) {
            mouseButtonStateMap.put(mouseButtonsToPress.poll(), State.DOWN);
        }

        // Release mouse buttons
        while (!mouseButtonsToRelease.isEmpty()) {
            mouseButtonStateMap.put(mouseButtonsToRelease.poll(), State.UP);
        }

        // Reset mouse wheel data
        mouseWheelRotation = 0;
        mouseWheelPreciseRotation = 0;

        boolean dispatchKeyEvents = keyEventWrapper.hasEnqueuedEvents();
        boolean dispatchMouseEvents = !mouseEventQueue.isEmpty();

        // Return if there are no new events to process
        if (!dispatchKeyEvents && !dispatchMouseEvents) {
            return;
        }

        // Process key events
        if (dispatchKeyEvents) {
            // Poll key events
            keyEvents = keyEventWrapper.pollEvents();

            // Process key events
            keyEvents.forEach(keyEvent -> {
                // Retrieve the last key state
                State lastState = keyStateMap.get(keyEvent.getKey());

                // Update key state based on current event and last key state
                if (keyEvent.getType().equals(KeyEvent.KEY_PRESSED)) {
                    if (lastState != State.DOWN) {
                        keyStateMap.put(keyEvent.getKey(), State.PRESSED);
                        keysToPress.add(keyEvent.getKey());
                    }
                } else {
                    keyStateMap.put(keyEvent.getKey(), State.RELEASED);
                    keysToRelease.add(keyEvent.getKey());
                }
            });
        }

        // Process mouse events
        if (dispatchMouseEvents) {
            // Poll mouse events
            mouseEvents = new LinkedList<>();
            int mouseEventCount = mouseEventQueue.size();
            for (int i = 0; i < mouseEventCount; i++) {
                mouseEvents.add(mouseEventQueue.poll());
            }

            // Process mouse events
            mouseEvents.forEach(mouseEvent -> {
                // Retrieve the last mouse button state
                State lastState = mouseButtonStateMap.get(mouseEvent.getMouseButton());

                // Update mouse button state based on current event
                if (mouseEvent.getType() == MouseEvent.MOUSE_PRESSED) {
                    if (lastState != State.DOWN) {
                        mouseButtonStateMap.put(mouseEvent.getMouseButton(), State.PRESSED);
                        mouseButtonsToPress.add(mouseEvent.getMouseButton());
                    }
                } else if (mouseEvent.getType() == MouseEvent.MOUSE_RELEASED) {
                    mouseButtonStateMap.put(mouseEvent.getMouseButton(), State.RELEASED);
                    mouseButtonsToRelease.add(mouseEvent.getMouseButton());
                }

                // Update the mouse wheel data if the event is a MouseWheelEvent
                if (mouseEvent instanceof MouseWheelEvent mouseWheelEvent) {
                    mouseWheelRotation = mouseWheelEvent.getWheelRotation();
                    mouseWheelPreciseRotation = mouseWheelEvent.getPreciseWheelRotation();
                }
            });

            // Update the last relative mouse position to the position found in the latest mouse event
            lastRelativeMousePosition = mouseEvents.get(mouseEvents.size() - 1).getMousePosition();
        }

        if (dispatchKeyEvents) {
            // Dispatch all key events accumulated throughout the last frame
            keyEvents.forEach(keyEvent -> getEventBus().dispatchEvent(keyEvent));
        }

        if (dispatchMouseEvents) {
            // Dispatch all mouse events accumulated throughout the last frame
            mouseEvents.forEach(mouseEvent -> getEventBus().dispatchEvent(mouseEvent));
        }
    }

    /**
     * Returns whether the {@code Input} is initialized.
     *
     * @return {@code true} if the {@code Input} is initialized; {@code false} otherwise.
     */
    public static boolean isInitialized() {
        return initialized;
    }

    /**
     * Returns whether the passed in {@linkplain Key} is pressed (only true for the one frame it was pressed in).
     *
     * @param key the {@code Key} to check.
     * @return {@code true} if the specified {@code Key} is pressed; {@code false} otherwise.
     * @throws NullPointerException  if the specified {@code Key} is {@code null}.
     * @throws IllegalStateException if the {@code Input} is not initialized.
     */
    public static boolean isKeyPressed(Key key) {
        Objects.requireNonNull(key, "key must not be null");
        if (!isInitialized()) {
            throw new IllegalStateException("cannot check for key state; Input is not initialized");
        }
        return keyStateMap.get(key) == State.PRESSED;
    }

    /**
     * Returns whether the passed in {@linkplain Key} is down.
     *
     * @param key the {@code Key} to check.
     * @return {@code true} if the specified {@code Key} is down; {@code false} otherwise.
     * @throws NullPointerException  if the specified {@code Key} is {@code null}.
     * @throws IllegalStateException if the {@code Input} is not initialized.
     */
    public static boolean isKeyDown(Key key) {
        Objects.requireNonNull(key, "key must not be null");
        if (!isInitialized()) {
            throw new IllegalStateException("cannot check for key state; Input is not initialized");
        }
        State state = keyStateMap.get(key);
        return state == State.PRESSED || state == State.DOWN;
    }

    /**
     * Returns whether the passed in {@linkplain Key} is released (only true for the one frame it was released in).
     *
     * @param key the {@code Key} to check.
     * @return {@code true} if the specified {@code Key} is released; {@code false} otherwise.
     * @throws NullPointerException  if the specified {@code Key} is {@code null}.
     * @throws IllegalStateException if the {@code Input} is not initialized.
     */
    public static boolean isKeyReleased(Key key) {
        Objects.requireNonNull(key, "key must not be null");
        if (!isInitialized()) {
            throw new IllegalStateException("cannot check for key state; Input is not initialized");
        }
        return keyStateMap.get(key) == State.RELEASED;
    }

    /**
     * Returns whether the passed in {@linkplain Key} is up.
     *
     * @param key the {@code Key} to check.
     * @return {@code true} if the specified {@code Key} is up; {@code false} otherwise.
     * @throws NullPointerException  if the specified {@code Key} is {@code null}.
     * @throws IllegalStateException if the {@code Input} is not initialized.
     */
    public static boolean isKeyUp(Key key) {
        Objects.requireNonNull(key, "key must not be null");
        if (!isInitialized()) {
            throw new IllegalStateException("cannot check for key state; Input is not initialized");
        }
        State state = keyStateMap.get(key);
        return state == State.RELEASED || state == State.UP;
    }

    /**
     * Returns whether the passed in {@linkplain MouseButton} is pressed (only true for the one frame it was pressed
     * in).
     *
     * @param mouseButton the {@code MouseButton} to check.
     * @return {@code true} if the specified {@code MouseButton} is pressed; {@code false} otherwise.
     * @throws NullPointerException  if the specified {@code MouseButton} is {@code null}.
     * @throws IllegalStateException if the {@code Input} is not initialized.
     */
    public static boolean isMouseButtonPressed(MouseButton mouseButton) {
        Objects.requireNonNull(mouseButton, "mouseButton must not be null");
        if (!isInitialized()) {
            throw new IllegalStateException("cannot check for mouse button state; Input is not initialized");
        }
        return mouseButtonStateMap.get(mouseButton) == State.PRESSED;
    }

    /**
     * Returns whether the passed in {@linkplain MouseButton} is down.
     *
     * @param mouseButton the {@code MouseButton} to check.
     * @return {@code true} if the specified {@code MouseButton} is down; {@code false} otherwise.
     * @throws NullPointerException  if the specified {@code MouseButton} is {@code null}.
     * @throws IllegalStateException if the {@code Input} is not initialized.
     */
    public static boolean isMouseButtonDown(MouseButton mouseButton) {
        Objects.requireNonNull(mouseButton, "mouseButton must not be null");
        if (!isInitialized()) {
            throw new IllegalStateException("cannot check for mouse button state; Input is not initialized");
        }
        State state = mouseButtonStateMap.get(mouseButton);
        return state == State.PRESSED || state == State.DOWN;
    }

    /**
     * Returns whether the passed in {@linkplain MouseButton} is released (only true for the one frame it was released
     * in).
     *
     * @param mouseButton the {@code MouseButton} to check.
     * @return {@code true} if the specified {@code MouseButton} is released; {@code false} otherwise.
     * @throws NullPointerException  if the specified {@code MouseButton} is {@code null}.
     * @throws IllegalStateException if the {@code Input} is not initialized.
     */
    public static boolean isMouseButtonReleased(MouseButton mouseButton) {
        Objects.requireNonNull(mouseButton, "mouseButton must not be null");
        if (!isInitialized()) {
            throw new IllegalStateException("cannot check for mouse button state; Input is not initialized");
        }
        return mouseButtonStateMap.get(mouseButton) == State.RELEASED;
    }

    /**
     * Returns whether the passed in {@linkplain MouseButton} is up.
     *
     * @param mouseButton the {@code MouseButton} to check.
     * @return {@code true} if the specified {@code MouseButton} is up; {@code false} otherwise.
     * @throws NullPointerException  if the specified {@code MouseButton} is {@code null}.
     * @throws IllegalStateException if the {@code Input} is not initialized.
     */
    public static boolean isMouseButtonUp(MouseButton mouseButton) {
        Objects.requireNonNull(mouseButton, "mouseButton must not be null");
        if (!isInitialized()) {
            throw new IllegalStateException("cannot check for mouse button state; Input is not initialized");
        }
        State state = mouseButtonStateMap.get(mouseButton);
        return state == State.RELEASED || state == State.UP;
    }

    /**
     * Returns the current global mouse position.
     *
     * @return a {@linkplain Vector2f} object containing the current global mouse position.
     */
    public static Vector2f getGlobalMousePosition() {
        Point mousePosition = MouseInfo.getPointerInfo().getLocation();
        return new Vector2f((float) mousePosition.getX(), (float) mousePosition.getY());
    }

    /**
     * Returns the current relative mouse position.
     *
     * @return a {@linkplain Vector2f} object containing the current relative mouse position.
     */
    public static Vector2f getRelativeMousePosition() {
        return lastRelativeMousePosition;
    }

    /**
     * Returns the current mouse wheel rotation.
     *
     * @return the current mouse wheel rotation.
     */
    public static int getMouseWheelRotation() {
        return mouseWheelRotation;
    }

    /**
     * Returns the current precise mouse wheel rotation.
     *
     * @return the current precise mouse wheel rotation.
     */
    public static double getMouseWheelPreciseRotation() {
        return mouseWheelPreciseRotation;
    }
}