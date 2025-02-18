package com.bartodelini.pixel.ecs;

/**
 * A <i>SceneObserver</i> is notified when changes are made to a {@linkplain Scene}.
 *
 * @author Bartolini
 * @version 1.0
 */
public interface SceneObserver {

    /**
     * Called when an {@linkplain Entity} was added to the {@linkplain Scene}.
     *
     * @param entity the {@code Entity} added to the {@code Scene}.
     */
    void entityAdded(Entity entity);

    /**
     * Called when an {@linkplain Entity} was removed from the {@linkplain Scene}.
     *
     * @param entity the {@code Entity} removed from the {@code Scene}.
     */
    void entityRemoved(Entity entity);
}