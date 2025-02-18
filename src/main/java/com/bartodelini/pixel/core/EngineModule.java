package com.bartodelini.pixel.core;

import com.bartodelini.pixel.ecs.Scene;
import com.bartodelini.pixel.ecs.SceneManager;
import com.bartodelini.pixel.environment.Environment;
import com.bartodelini.pixel.environment.EnvironmentManager;
import com.bartodelini.pixel.event.EventBus;

import java.util.Objects;

/**
 * An <i>EngineModule</i> is a base component of an {@code Engine}.
 * <p>
 * It is supposed to be responsible for one type of task in the engine's <i>main loop</i>. For instance rendering or
 * networking.
 *
 * @author Bartolini
 * @version 1.1
 */
public abstract class EngineModule {

    private final String name;

    protected final Environment environment;

    private SceneManager sceneManager;
    private EventBus eventBus;
    private EnvironmentManager environmentManager;
    private boolean active = false;

    /**
     * Allocates a new {@code EngineModule} object by passing in its name and a prefix, which will be used for its
     * {@linkplain Environment}.
     *
     * @param name   the name of this module.
     * @param prefix the prefix for the environment of this module.
     * @throws NullPointerException if the specified name or prefix are {@code null}.
     */
    public EngineModule(String name, String prefix) {
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.environment = new Environment(prefix);
    }

    /**
     * Returns the {@code name} of this {@code EngineModule}.
     *
     * @return the {@code name} of this {@code EngineModule}.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the {@code Environment} of this {@code EngineModule}.
     *
     * @return the {@code Environment} of this {@code EngineModule}.
     */
    public Environment getEnvironment() {
        return environment;
    }

    /**
     * Returns the {@linkplain SceneManager} attached to this {@code EngineModule}.
     *
     * @return the {@code SceneManager} attached to this {@code EngineModule}.
     */
    public SceneManager getSceneManager() {
        return sceneManager;
    }

    /**
     * Sets the {@linkplain SceneManager} of this {@code EngineModule}. This method can only be called if this
     * {@code EngineModule} is inactive, otherwise an exception is thrown.
     *
     * @param sceneManager the {@code SceneManager} to be attached to this {@code EngineModule}.
     * @throws IllegalStateException if this {@code EngineModule} is active.
     */
    public final void setSceneManager(SceneManager sceneManager) {
        if (isActive()) {
            throw new IllegalStateException("cannot set the SceneManager of an active EngineModule");
        }
        this.sceneManager = sceneManager;
    }

    /**
     * Returns the {@linkplain EventBus} attached to this {@code EngineModule}.
     *
     * @return the {@code EventBus} attached to this {@code EngineModule}.
     */
    public EventBus getEventBus() {
        return eventBus;
    }

    /**
     * Sets the {@linkplain EventBus} of this {@code EngineModule}. This method can only be called if this
     * {@code EngineModule} is inactive, otherwise an exception is thrown.
     *
     * @param eventBus the {@code EventBus} to be attached to this {@code EngineModule}.
     * @throws IllegalStateException if this {@code EngineModule} is active.
     */
    public final void setEventBus(EventBus eventBus) {
        if (isActive()) {
            throw new IllegalStateException("cannot set the EventBus of an active EngineModule");
        }
        this.eventBus = eventBus;
    }

    /**
     * Returns the current {@linkplain Scene} of the {@linkplain SceneManager} attached to this {@code EngineModule}.
     * This method has the same effect as {@code getSceneManager().getCurrentScene()}.
     *
     * @return the current {@code Scene} of the {@code SceneManager} attached to this {@code EngineModule}.
     */
    public Scene getScene() {
        return getSceneManager().getCurrentScene();
    }

    /**
     * Returns the {@linkplain EnvironmentManager} attached to this {@code EngineModule}.
     *
     * @return the {@code IEnvironmentManager} attached to this {@code EngineModule}.
     */
    public EnvironmentManager getEnvironmentManager() {
        return environmentManager;
    }

    /**
     * Sets the {@linkplain EnvironmentManager} of this {@code EngineModule}. This method can only be called if this
     * {@code EngineModule} is inactive, otherwise an exception is thrown.
     *
     * @param environmentManager the {@code EnvironmentManager} to be attached to this {@code EngineModule}.
     * @throws IllegalStateException if this {@code EngineModule} is active.
     */
    public void setEnvironmentManager(EnvironmentManager environmentManager) {
        if (isActive()) {
            throw new IllegalStateException("cannot set the EnvironmentManager of an active EngineModule");
        }
        this.environmentManager = environmentManager;
    }

    /**
     * Activates this {@code EngineModule}. This method can only be called if this {@code EngineModule} is not activated
     * yet and if the {@linkplain SceneManager}, {@linkplain EnvironmentManager} and {@linkplain EventBus} are
     * set for this {@code EngineModule} (all non {@code null}).
     *
     * @throws IllegalStateException if this {@code EngineModule} is already activated or if the
     *                               {@code SceneManager}, {@code EnvironmentManager} and {@code EventBus} are not set
     *                               for this {@code EngineModule} yet.
     */
    public final void activate() {
        if (isActive()) {
            throw new IllegalStateException("EngineModule already activated");
        }
        if (sceneManager == null) {
            throw new IllegalStateException("EngineModule has no attached SceneManager");
        }
        if (environmentManager == null) {
            throw new IllegalStateException("EngineModule has no attached EnvironmentManager");
        }
        if (eventBus == null) {
            throw new IllegalStateException("EngineModule has no attached EventBus");
        }
        active = true;
    }

    /**
     * Deactivates this {@code EngineModule}. This method can only be called if this {@code EngineModule} is inactive,
     * otherwise an exception is thrown.
     *
     * @throws IllegalStateException if this {@code EngineModule} is already deactivated.
     */
    public final void deactivate() {
        if (!isActive()) {
            throw new IllegalStateException("EngineModule already deactivated");
        }
        active = false;
    }

    /**
     * Returns whether this {@code EngineModule} is active.
     *
     * @return {@code true} if this {@code EngineModule} is active; {@code false} otherwise.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * This method is called before the {@linkplain Engine} enters the <i>main loop</i> and before the
     * {@linkplain #start() start} method is called. It should be used for any initialization actions needed by this
     * {@code EngineModule}.
     *
     * @throws ModuleInitializeException if this {@code EngineModule} failed initializing.
     */
    public void initialize() throws ModuleInitializeException {
    }

    /**
     * This method is called before the {@linkplain Engine} enters the <i>main loop</i> and after all
     * {@code EngineModules} finished initializing. It should be used for any actions needed to be executed once before
     * the start of the update cycle.
     */
    public void start() {
    }

    /**
     * This method is called in a fixed time interval, specified by the {@linkplain Engine}. It should be used for
     * actions which should be done within a fixedDeltaTime (e.g. physics calculations).
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
     * This method is called before the {@linkplain Engine} exits with the {@code exitCode}. It allows each
     * {@code EngineModule} to free up any used resources and perform any actions needed for a clean exit of this
     * {@code EngineModule}.
     *
     * @param exitCode the exit code which the {@code Engine} will exit with.
     */
    public void stop(int exitCode) {
    }
}