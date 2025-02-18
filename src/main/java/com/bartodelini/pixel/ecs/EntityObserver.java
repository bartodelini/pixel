package com.bartodelini.pixel.ecs;

/**
 * An <i>EntityObserver</i> is notified when changes are made to an {@linkplain Entity}.
 *
 * @author Bartolini
 * @version 1.0
 */
public interface EntityObserver {

    /**
     * Called when a {@linkplain Component} was added to the observed {@linkplain Entity}.
     *
     * @param entity    the {@code Entity} which was updated.
     * @param component the {@code Component} added.
     */
    void componentAdded(Entity entity, Component component);

    /**
     * Called when a {@linkplain Component} was removed from the observed {@linkplain Entity}.
     *
     * @param entity    the {@code Entity} which was updated.
     * @param component the {@code Component} removed.
     */
    void componentRemoved(Entity entity, Component component);

    /**
     * Called when the name of the observed {@linkplain Entity} was changed.
     *
     * @param entity  the {@code Entity} which was updated.
     * @param oldName the old name of the {@code Entity}.
     * @param newName the new name of the {@code Entity}.
     */
    void nameChanged(Entity entity, String oldName, String newName);

    /**
     * Called when the layer of the observed {@linkplain Entity} was changed.
     *
     * @param entity   the {@code Entity} which was updated.
     * @param oldLayer the old layer of the {@code Entity}.
     * @param newLayer the new layer of the {@code Entity}.
     */
    void layerChanged(Entity entity, int oldLayer, int newLayer);

    /**
     * Called when the {@linkplain Scene} of the observed {@linkplain Entity} was changed.
     *
     * @param entity   the {@code Entity} which was updated.
     * @param oldScene the old {@code Scene} of the {@code Entity}.
     * @param newScene the new {@code Scene} of the {@code Entity}.
     */
    void sceneChanged(Entity entity, Scene oldScene, Scene newScene);

    /**
     * Called when the tag of the observed {@linkplain Entity} was changed.
     *
     * @param entity the {@code Entity} which was updated.
     * @param oldTag the old tag of the {@code Entity}.
     * @param newTag the new tag of the {@code Entity}.
     */
    void tagChanged(Entity entity, String oldTag, String newTag);

    /**
     * Called when the observed {@linkplain Entity} was activated.
     *
     * @param entity the {@code Entity} which was updated.
     */
    void entityActivated(Entity entity);

    /**
     * Called when the observed {@linkplain Entity} was deactivated.
     *
     * @param entity the {@code Entity} which was updated.
     */
    void entityDeactivated(Entity entity);
}