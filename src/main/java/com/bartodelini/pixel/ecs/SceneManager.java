package com.bartodelini.pixel.ecs;

import com.bartodelini.pixel.modules.scripting.ScriptableComponent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A <i>SceneManager</i> is used to manage {@linkplain Scene Scenes}. It holds a reference for the current {@code Scene}
 * and controls the changing of {@code Scenes}.
 *
 * @author Bartolini
 * @version 1.0
 */
public class SceneManager {

    private final Map<String, Scene> sceneMap = new HashMap<>();

    private Scene currentScene;

    /**
     * Allocates a new {@code SceneManager}, creates a default {@linkplain Scene} and sets it as the current one.
     */
    public SceneManager() {
        sceneMap.put("default", new Scene());
        currentScene = sceneMap.get("default");
    }

    /**
     * Returns the current {@linkplain Scene}.
     *
     * @return the current {@code Scene}.
     */
    public Scene getCurrentScene() {
        return currentScene;
    }

    /**
     * Changes the current {@linkplain Scene} by its corresponding name.
     *
     * @param name the name of the {@code Scene} to switch to.
     * @throws NullPointerException     if the specified name is {@code null}.
     * @throws IllegalArgumentException if a {@code Scene} with the specified {@code name} does not exist.
     */
    public void changeScene(String name) {
        Objects.requireNonNull(name, "name must not be null");
        if (!sceneMap.containsKey(name)) {
            throw new IllegalArgumentException("a scene with the specified name does not exist");
        }
        currentScene = sceneMap.get(name);
    }

    public void addScene(String name, Scene scene) {
        sceneMap.put(name, scene);
        scene.getComponents(ScriptableComponent.class).forEach(ScriptableComponent::start);
    }
}