package com.bartodelini.pixel;

import com.bartodelini.pixel.core.Engine;
import com.bartodelini.pixel.core.EngineModule;
import com.bartodelini.pixel.ecs.SceneManager;
import com.bartodelini.pixel.modules.asset.AssetManager;
import com.bartodelini.pixel.modules.asset.StringLoader;
import com.bartodelini.pixel.modules.input.Input;
import com.bartodelini.pixel.modules.rendering.Renderer;
import com.bartodelini.pixel.modules.rendering.bitmap.BitmapLoader;
import com.bartodelini.pixel.modules.rendering.components.model.MeshLoader;
import com.bartodelini.pixel.modules.scripting.ScriptManager;
import com.bartodelini.pixel.modules.terminal.Terminal;
import com.bartodelini.pixel.modules.terminal.ui.Autocomplete;
import com.bartodelini.pixel.modules.terminal.ui.SwingTerminalUI;
import com.bartodelini.pixel.modules.terminal.ui.color.DefaultColorScheme;
import com.bartodelini.pixel.modules.time.Time;

import java.awt.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;

/**
 * An <i>EngineFactory</i> is used to create {@linkplain Engine} instances equipped with commonly used
 * {@linkplain EngineModule EngineModules}.
 *
 * @author Bartolini
 * @version 1.0
 */
public class EngineFactory {

    private EngineFactory() {
    }

    /**
     * Creates an {@linkplain Engine} instance by specifying the width, height and scale used for the window, as well
     * as the {@linkplain SceneManager}. The {@code Engine} will be equipped with the following
     * {@linkplain EngineModule EngineModules}:
     * <ul>
     *   <li>{@linkplain AssetManager}</li>
     *   <li>{@linkplain Time}</li>
     *   <li>{@linkplain Terminal}</li>
     *   <li>{@linkplain Input}</li>
     *   <li>{@linkplain ScriptManager}</li>
     *   <li>{@linkplain Renderer}</li>
     *
     * @param width        the width of the window in pixels.
     * @param height       the height of the window in pixels.
     * @param scale        the scale of the pixels.
     * @param sceneManager the {@code SceneManager} used for the {@code Engine}.
     * @return an {@code Engine} instance equipped with common {@code EngineModules}, based on the
     * specified parameters.
     * @throws NullPointerException if the specified {@code SceneManager} is {@code null}.
     */
    public static Engine createEngineRuntime(int width, int height, int scale, SceneManager sceneManager) {
        Objects.requireNonNull(sceneManager, "sceneManager must not be null");

        Engine engine = new Engine(sceneManager, 100);

        Path assetsPath;
        try {
            URL assetsURL = Engine.class.getResource("/assets");
            if (assetsURL == null) {
                assetsURL = Engine.class.getResource("/");
            }
            assetsPath = Path.of(Objects.requireNonNull(assetsURL, "assetsURL must not be null").toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        engine.addModule(new AssetManager(assetsPath, new StringLoader(), new BitmapLoader(), new MeshLoader()));
        engine.addModule(new Time());
        engine.addModule(new Terminal(new SwingTerminalUI(
                "Terminal",
                new Dimension(800, 500),
                new Autocomplete(engine.getEnvironmentManager(), 5),
                new DefaultColorScheme())));
        engine.addModule(new Input());
        engine.addModule(new ScriptManager());
        engine.addModule(new Renderer(Engine.NAME + " v." + Engine.VERSION, width, height, scale));

        return engine;
    }

    /**
     * Creates an {@linkplain Engine} instance by specifying the width, height and scale used for the window. The
     * {@code Engine} will be equipped with the following {@linkplain EngineModule EngineModules}:
     * <ul>
     *   <li>{@linkplain AssetManager}</li>
     *   <li>{@linkplain Time}</li>
     *   <li>{@linkplain Terminal}</li>
     *   <li>{@linkplain Input}</li>
     *   <li>{@linkplain ScriptManager}</li>
     *   <li>{@linkplain Renderer}</li>
     *
     * @param width  the width of the window in pixels.
     * @param height the height of the window in pixels.
     * @param scale  the scale of the pixels.
     * @return an {@code Engine} instance equipped with common {@code EngineModules}, based on the
     * specified parameters.
     */
    public static Engine createEngineRuntime(int width, int height, int scale) {
        // Create a scene manager
        SceneManager sceneManager = new SceneManager();

        // Create an engine instance with the created sceneManager
        return createEngineRuntime(width, height, scale, sceneManager);
    }
}