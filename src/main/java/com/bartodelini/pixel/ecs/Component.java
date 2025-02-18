package com.bartodelini.pixel.ecs;

/**
 * A <i>Component</i> is the super class for all {@code Components} which an {@linkplain Entity} can hold.
 *
 * @author Bartolini
 * @version 1.1
 */

public abstract class Component {

    private Entity owner;
    private boolean active = false;

    /**
     * Returns the {@linkplain Entity} owning this {@code Component}.
     *
     * @return the {@code Entity} owning this {@code Component}.
     */
    public final Entity getOwner() {
        return owner;
    }

    /**
     * Sets the {@linkplain Entity} owning this {@code Component}.
     *
     * @param owner the {@code Entity} owning this {@code Component}.
     * @throws IllegalStateException if the owner of this {@code Component} is already set.
     */
    public final void setOwner(Entity owner) {
        if (this.owner != null) {
            throw new IllegalStateException("The owner of this Component has already been set");
        }
        this.owner = owner;
    }

    /**
     * Activates this {@code Component}.
     *
     * @throws IllegalStateException if this {@code Component} is already active or if the {@linkplain Entity}
     *                               owning this {@code Component} is not set yet.
     */
    public final void activate() {
        if (isActive()) {
            throw new IllegalStateException("Cannot activate an already active Component");
        }
        if (getOwner() == null) {
            throw new IllegalStateException("Cannot activate a Component without an owner");
        }
        active = true;
    }

    /**
     * Deactivates this {@code Component}.
     *
     * @throws IllegalStateException if this {@code Component} is already inactive.
     */
    public final void deactivate() {
        if (!isActive()) {
            throw new IllegalStateException("Cannot deactivate an already inactive Component");
        }
        active = false;
    }

    /**
     * Returns whether this {@code Component} is active.
     *
     * @return {@code true} if this {@code Component} is active; {@code false} otherwise.
     */
    public final boolean isActive() {
        return active;
    }
}