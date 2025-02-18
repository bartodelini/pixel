package com.bartodelini.pixel.modules.scripting;

import com.bartodelini.pixel.ecs.Component;
import com.bartodelini.pixel.ecs.Entity;
import com.bartodelini.pixel.ecs.Scene;

/**
 * A <i>ScriptableComponent</i> is used to implement behaviours on {@linkplain Entity Entities}.
 *
 * @author Bartolini
 * @version 1.1
 */
public abstract class ScriptableComponent extends Component {

    /**
     * Instantiates, then adds to the {@linkplain Scene} and finally returns an {@linkplain Entity} constructed from the
     * specified {@linkplain Component Components}.
     *
     * @param components the {@code Components} used to instantiate the {@code Entity}.
     * @return the instantiated {@code Entity}.
     * @throws IllegalStateException if this {@code Component} has no attached owner, or the owner of this
     *                               {@code Component} has no attached {@code Scene}.
     */
    public Entity instantiateEntity(Component... components) {
        if (getOwner() == null) {
            throw new IllegalStateException("Component has no attached owner");
        }
        if (getOwner().getScene() == null) {
            throw new IllegalStateException("the owner of this Component has no attached Scene");
        }
        Entity entity = new Entity(components);
        getOwner().getScene().addEntity(entity);
        return entity;
    }

    /**
     * Returns the {@linkplain Component} whose type is encapsulated by the passed in {@linkplain Class}. For the sake
     * of convenience the returned {@code Component} is cast to the type encapsulated by the specified {@code Class}.
     *
     * @param clazz the {@code Class} encapsulating the wished {@code Component} type.
     * @param <T>   the {@code Component} class.
     * @return the {@code Component} whose type is encapsulated by the passed {@code Class}, or {@code null} if no such
     * {@code Component} is present in this {@code Entity}.
     * @throws IllegalArgumentException if this {@code Component} has no attached owner.
     * @throws NullPointerException     if the specified {@code Class} encapsulating the {@code Component} is
     *                                  {@code null}.
     */
    public <T extends Component> T getComponent(Class<T> clazz) {
        if (getOwner() == null) {
            throw new IllegalStateException("Component has no attached owner");
        }
        return getOwner().getComponent(clazz);
    }

    /**
     * Removes the specified {@linkplain Entity} from the {@linkplain Scene} of the owner of this {@code Component}.
     *
     * @param entity the {@code Entity} to remove.
     * @throws IllegalStateException if this {@code Component} has no attached owner, the owner of this
     *                               {@code Component} has no attached {@code Scene}, or the specified {@code Entity} is
     *                               not present in the {@code Scene}.
     * @throws NullPointerException  if the specified {@code Entity} is {@code null}.
     */
    public void destroyEntity(Entity entity) {
        if (getOwner() == null) {
            throw new IllegalStateException("Component has no attached owner");
        }
        if (getOwner().getScene() == null) {
            throw new IllegalStateException("the owner of this Component has no attached Scene");
        }
        getOwner().getScene().removeEntity(entity);
    }

    /**
     * Removes the specified {@code Component} from the owner of this {@code Component}.
     *
     * @param component the {@code Component} to remove.
     * @throws IllegalStateException if this {@code Component} has no attached owner, or the specified {@code Component}
     *                               is not attached to the owner of this {@code Entity}.
     * @throws NullPointerException  if the specified {@code Component} is {@code null}.
     */
    public void destroyComponent(Component component) {
        if (getOwner() == null) {
            throw new IllegalStateException("Component has no attached owner");
        }
        component.getOwner().removeComponent(component);
    }

    /**
     * This method is called before the {@code Engine} enters the <i>main loop</i> and after all Engine Modules finished
     * initializing. It should be used for any actions needed to be executed once before the start of the update cycle.
     */
    public void start() {
    }

    /**
     * This method is called in a fixed time interval, specified by the {@code Engine}. It should be used for actions
     * which should be done within a fixedDeltaTime (e.g. physics calculations).
     *
     * @param fixedDeltaTime the fixed delta time between {@code fixedUpdate} calls.
     */
    public void fixedUpdate(double fixedDeltaTime) {
    }

    /**
     * This method is called on every loop of the <i>engine loop</i>. It should be used for actions which should be done
     * as often as possible (e.g. rendering).
     *
     * @param deltaTime the delta time between {@code update} calls.
     */
    public void update(double deltaTime) {
    }

    public void lateUpdate() {
    }

    /**
     * This method is called on every loop of the <i>engine loop</i>, right before the
     * {@linkplain #update(double) update} method. It provides a way of interpolating between fixed update calls, as it
     * specifies what fraction of {@code fixedUpdate} was missed since the last {@code update} call.
     *
     * @param alpha the missed fraction of the {@code fixedUpdate} in the current update cycle.
     */
    public void interpolate(double alpha) {
    }

    /**
     * This method is called before the {@code Engine} exits with the {@code exitCode}. It allows each
     * {@code ScriptableComponent} to free up any used resources and perform any actions needed for a clean exit of this
     * {@code ScriptableComponent}.
     *
     * @param exitCode the exit code which the {@code Engine} will exit with.
     */
    public void stop(int exitCode) {
    }
}