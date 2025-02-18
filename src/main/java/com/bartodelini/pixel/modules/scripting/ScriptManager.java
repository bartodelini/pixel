package com.bartodelini.pixel.modules.scripting;

import com.bartodelini.pixel.core.EngineModule;
import com.bartodelini.pixel.ecs.Entity;
import com.bartodelini.pixel.ecs.SceneObserver;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

/**
 * A <i>ScriptManager</i> is used to manage {@linkplain ScriptableComponent ScriptableComponents}. It calls their start,
 * fixedUpdate, update, lateUpdate, interpolate and stop methods appropriately.
 *
 * @author Bartolini
 * @version 1.1
 */
public class ScriptManager extends EngineModule {

    private final Queue<Entity> entitiesToStart = new LinkedList<>();

    /**
     * Allocates a new {@code ScriptManager} object.
     */
    public ScriptManager() {
        super("Script Manager", "script");
    }

    @Override
    public void initialize() {
        getScene().addSceneObserver(new SceneObserver() {
            @Override
            public void entityAdded(Entity entity) {
                if (entity.hasComponent(ScriptableComponent.class)) {
                    entitiesToStart.add(entity);
                }
            }

            @Override
            public void entityRemoved(Entity entity) {
            }
        });
    }

    @Override
    public void start() {
        getScene().getComponents(ScriptableComponent.class).forEach(ScriptableComponent::start);
    }

    @Override
    public void fixedUpdate(double fixedDeltaTime) {
        getScene().getComponents(ScriptableComponent.class).forEach(s -> s.fixedUpdate(fixedDeltaTime));
    }

    @Override
    public void update(double deltaTime) {
        if (!entitiesToStart.isEmpty()) {
            int queueSize = entitiesToStart.size();
            for (int i = 0; i < queueSize; i++) {
                Optional<Entity> optionalEntity = Optional.ofNullable(entitiesToStart.poll());
                optionalEntity.ifPresent(entity -> {
                    Optional<ScriptableComponent> componentOptional =
                            Optional.ofNullable(entity.getComponent(ScriptableComponent.class));
                    componentOptional.ifPresent(ScriptableComponent::start);
                });
            }
        }
        getScene().getComponents(ScriptableComponent.class).forEach(s -> s.update(deltaTime));
    }

    @Override
    public void lateUpdate() {
        getScene().getComponents(ScriptableComponent.class).forEach(ScriptableComponent::lateUpdate);
    }

    @Override
    public void interpolate(double alpha) {
        getScene().getComponents(ScriptableComponent.class).forEach(s -> s.interpolate(alpha));
    }

    @Override
    public void stop(int exitCode) {
        getScene().getComponents(ScriptableComponent.class).forEach(s -> s.stop(exitCode));
    }
}