package com.bartodelini.pixel;

import com.bartodelini.pixel.core.Engine;
import com.bartodelini.pixel.core.UIUtils;
import com.bartodelini.pixel.ecs.Entity;
import com.bartodelini.pixel.ecs.Scene;
import com.bartodelini.pixel.environment.Command;
import com.bartodelini.pixel.logging.LogRecord;
import com.bartodelini.pixel.logging.LogRecordFormatter;
import com.bartodelini.pixel.logging.LoggerFactory;
import com.bartodelini.pixel.logging.StreamHandler;
import com.bartodelini.pixel.math.vector.Vector3f;
import com.bartodelini.pixel.modules.asset.AssetManager;
import com.bartodelini.pixel.modules.rendering.bitmap.Bitmap;
import com.bartodelini.pixel.modules.rendering.bitmap.Bitmaps;
import com.bartodelini.pixel.modules.rendering.bitmap.Colors;
import com.bartodelini.pixel.modules.rendering.bitmap.blending.BlendFunction;
import com.bartodelini.pixel.modules.rendering.components.camera.PerspectiveCamera;
import com.bartodelini.pixel.modules.rendering.components.camera.TurntableCameraController;
import com.bartodelini.pixel.modules.rendering.components.light.DirectionalLight;
import com.bartodelini.pixel.modules.rendering.components.light.PointLight;
import com.bartodelini.pixel.modules.rendering.components.light.Spotlight;
import com.bartodelini.pixel.modules.rendering.components.model.Mesh;
import com.bartodelini.pixel.modules.rendering.components.model.Model;
import com.bartodelini.pixel.modules.rendering.components.model.Transform;
import com.bartodelini.pixel.modules.rendering.components.shader.PhongShader;
import com.bartodelini.pixel.modules.rendering.components.shader.textures.CubeMap;
import com.bartodelini.pixel.modules.rendering.components.shader.textures.Texture;
import com.bartodelini.pixel.modules.rendering.components.shader.textures.TextureSampler;
import com.bartodelini.pixel.modules.rendering.components.shader.textures.UVWrapper;
import com.bartodelini.pixel.modules.rendering.components.text.Text;
import com.bartodelini.pixel.modules.scripting.ScriptableComponent;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        // Set default system LookAndFeel
        UIUtils.setSystemLookAndFeel();

        int width = 480;
        int height = 320;

        // Create an engine runtime
        Engine engine = EngineFactory.createEngineRuntime(width, height, 2);

        // Add a log handler to display log messages in the standard output stream
        LoggerFactory.addLogRecordHandler(new StreamHandler(System.out, new LogRecordFormatter() {
            final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    .withZone(ZoneId.systemDefault());

            @Override
            public String format(LogRecord record) {
                return dateFormatter.format(record.getInstant()) + " "
                        + record.getLevel().toString() + " "
                        + record.getSource().getSimpleName() + ": "
                        + record.getMessage();
            }
        }));

        // Add entities
        engine.invokeOnStart(() -> {
            // Retrieve the current scene
            Scene currentScene = engine.getSceneManager().getCurrentScene();

            // Add the main camera to the scene
            currentScene.addEntity(
                    new Entity(
                            "Main Camera",
                            new Transform(Vector3f.FRONT.scale(10)),
                            new TurntableCameraController(),
//                            new PerspectiveCamera(90, (float) width / height, 0.1f, 1000, 0xFF6EB1FF)));
                            new PerspectiveCamera(90, (float) width / height, 0.1f, 1000, 0xff87CEEB)));

            Scene blendingScene = new Scene();
            blendingScene.addEntity(
                    new Entity(
                            "Main Camera",
                            new Transform(Vector3f.FRONT.scale(7)),
                            new TurntableCameraController(),
//                            new PerspectiveCamera(90, (float) width / height, 0.1f, 1000, 0xFF6EB1FF)));
//                            new PerspectiveCamera(90, (float) width / height, 0.1f, 1000, 0xff87CEEB)));
                            new PerspectiveCamera(90, (float) width / height, 0.1f, 1000, 0xff000000)));

            blendingScene.addEntity(new Entity(
                    new Transform(Vector3f.LEFT.scale(2.4f).add(Vector3f.UP.scale(0f)), Vector3f.ZERO, Vector3f.ONE.scale(0.9f)),
                    new Model(AssetManager.getAsset(Mesh.class, "meshes/bunny_smooth.obj")),
                    new PhongShader(
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR), null)
                            .setShininess(20)
                            .setSpecularIntensity(0.9f)
                            .setAmbientLightColor(Colors.getGray(51))
                            .setNormalMapStrength(0.5f)
                            .setReflectionIntensity(0.4f)
                            .setTransparency(1f)
                            .enableBackFaceCulling(true)
                            .setOpaque(true)
                            .setBlendFunction(BlendFunction.DEFAULT)));

            blendingScene.addEntity(new Entity(
                    new Transform(Vector3f.RIGHT.scale(2.4f).add(Vector3f.UP.scale(0)), Vector3f.UP.scale((float)Math.PI), Vector3f.ONE.scale(0.6f)),
                    new Model(AssetManager.getAsset(Mesh.class, "meshes/teapot_uv.obj")),
                    new PhongShader(
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),null)
                            .setShininess(20)
                            .setSpecularIntensity(0.9f)
                            .setAmbientLightColor(Colors.getGray(51))
                            .setNormalMapStrength(0.5f)
                            .setReflectionIntensity(0.4f)
                            .setTransparency(0.7f)
                            .enableBackFaceCulling(true)
                            .setOpaque(true)
                            .setBlendFunction(BlendFunction.DEFAULT)));

            blendingScene.addEntity(new Entity(
                    new Transform(Vector3f.LEFT.scale(2.4f), Vector3f.UP.scale((float) Math.PI / 2), Vector3f.ONE.scale(2f)),
                    new Model(AssetManager.getAsset(Mesh.class, "meshes/plane.obj")),
                    new PhongShader(
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/grid64.png"),
                                    UVWrapper.REPEAT, TextureSampler.NEAREST),
                            null,
                            new Texture(Bitmaps.getUnicolor(1, 1, 0xff777777),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            null)
                            .setShininess(20)
                            .setSpecularIntensity(1f)
                            .setAmbientLightColor(Colors.getGray(51))
                            .setNormalMapStrength(0.5f)
                            .setReflectionIntensity(0f)));

            blendingScene.addEntity(new Entity(
                    new Transform(Vector3f.RIGHT.scale(2.4f), Vector3f.UP.scale((float) Math.PI / 2), Vector3f.ONE.scale(2f)),
                    new Model(AssetManager.getAsset(Mesh.class, "meshes/plane.obj")),
                    new PhongShader(
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/grid64.png"),
                                    UVWrapper.REPEAT, TextureSampler.NEAREST),
                            null,
                            new Texture(Bitmaps.getUnicolor(1, 1, 0xff777777),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            null)
                            .setShininess(20)
                            .setSpecularIntensity(1f)
                            .setAmbientLightColor(Colors.getGray(51))
                            .setNormalMapStrength(0.5f)
                            .setReflectionIntensity(0f)));

            blendingScene.addEntity(new Entity(
                    new Transform(new Vector3f(2, 5, -2)),
                    new PointLight(new Vector3f(0,0.2f,0)),
                    new ScriptableComponent() {
                        Transform transform;
                        Transform pivot;

                        @Override
                        public void start() {
                            transform = getOwner().getComponent(Transform.class);
                            pivot = new Transform();
                            transform.setParent(pivot);
                        }

                        @Override
                        public void update(double deltaTime) {
                            pivot.rotate(Vector3f.UP.scale((float) deltaTime * 2));
                        }
                    }
            ));

            blendingScene.addEntity(new Entity(
                    new Transform(new Vector3f(2, 5, 0)),
                    new PointLight(new Vector3f(0,0,0.2f)),
                    new ScriptableComponent() {
                        Transform transform;
                        Transform pivot;

                        @Override
                        public void start() {
                            transform = getOwner().getComponent(Transform.class);
                            pivot = new Transform();
                            transform.setParent(pivot);
                        }

                        @Override
                        public void update(double deltaTime) {
                            pivot.rotate(Vector3f.UP.scale((float) deltaTime * 1.2f));
                        }
                    }
            ));


            blendingScene.addEntity(new Entity(
                    new Transform(new Vector3f(-4, 0, 15)),
                    new DirectionalLight(Vector3f.ONE.scale(0.4f), Vector3f.BACK.add(Vector3f.DOWN).add(Vector3f.RIGHT))
            ));

            engine.getSceneManager().addScene("blending", blendingScene);

            Scene lightsScene = new Scene();
            lightsScene.addEntity(
                    new Entity(
                            "Main Camera",
                            new Transform(Vector3f.FRONT.scale(10)),
                            new TurntableCameraController(),
//                            new PerspectiveCamera(90, (float) width / height, 0.1f, 1000, 0xFF6EB1FF)));
                            new PerspectiveCamera(90, (float) width / height, 0.1f, 1000, 0xff87CEEB)));

            lightsScene.addEntity(new Entity(
                    new Transform(Vector3f.DOWN, Vector3f.UP.scale((float) Math.PI / 2), Vector3f.ONE.scale(6)),
                    new Model(AssetManager.getAsset(Mesh.class, "meshes/plane.obj")),
                    new PhongShader(
                            new Texture(Bitmaps.getUnicolor(1, 1, 0xff222222),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            null,
                            new Texture(Bitmaps.getUnicolor(1, 1, 0xff222222),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),null)
                            .setShininess(20)
                            .setSpecularIntensity(0f)
                            .setAmbientLightColor(Colors.getGray(51))
                            .setNormalMapStrength(0.5f)
                            .setReflectionIntensity(0.5f)));

            lightsScene.addEntity(new Entity(
                    new Transform(Vector3f.LEFT.scale(2.4f), Vector3f.UP.scale((float) Math.PI / 2), Vector3f.ONE.scale(2f)),
                    new Model(AssetManager.getAsset(Mesh.class, "meshes/plane.obj")),
                    new PhongShader(
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            null,
                            new Texture(Bitmaps.getUnicolor(1, 1, 0xff777777),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            null)
                            .setShininess(20)
                            .setSpecularIntensity(1f)
                            .setAmbientLightColor(Colors.getGray(51))
                            .setNormalMapStrength(0.5f)
                            .setReflectionIntensity(0f)));

            lightsScene.addEntity(new Entity(
                    new Transform(Vector3f.RIGHT.scale(2.4f), Vector3f.UP.scale((float) Math.PI / 2), Vector3f.ONE.scale(2f)),
                    new Model(AssetManager.getAsset(Mesh.class, "meshes/plane.obj")),
                    new PhongShader(
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            null)
                            .setShininess(20)
                            .setSpecularIntensity(1f)
                            .setAmbientLightColor(Colors.getGray(51))
                            .setNormalMapStrength(1f)
                            .setReflectionIntensity(0f)));

            lightsScene.addEntity(new Entity(
                    new Transform(new Vector3f(0, 2, 0)),
                    new PointLight(Vector3f.ONE.scale(1f))
            ));

            lightsScene.addEntity(new Entity(
                    new Transform(new Vector3f(-2f, 1, -2)),
                    new Spotlight(new Vector3f(0, 0.4f, 0.8f), Vector3f.DOWN.add(Vector3f.LEFT).add(Vector3f.FRONT.scale(2)), 1, 1)
            ));

            lightsScene.addEntity(new Entity(
                    new Transform(new Vector3f(2f, 1, -2)),
                    new Spotlight(new Vector3f(0, 0.8f, 0.4f), Vector3f.DOWN.add(Vector3f.RIGHT).add(Vector3f.FRONT.scale(2)), 1, 1)
            ));

            engine.getSceneManager().addScene("lights", lightsScene);

            Scene normalScene = new Scene();
            normalScene.addEntity(
                    new Entity(
                            "Main Camera",
                            new Transform(Vector3f.FRONT.scale(10)),
                            new TurntableCameraController(),
//                            new PerspectiveCamera(90, (float) width / height, 0.1f, 1000, 0xFF6EB1FF)));
                            new PerspectiveCamera(90, (float) width / height, 0.1f, 1000, 0xff87CEEB)));

            normalScene.addEntity(new Entity(
                    new Transform(Vector3f.LEFT.scale(2.4f), Vector3f.UP.scale((float) Math.PI / 2), Vector3f.ONE.scale(2f)),
                    new Model(AssetManager.getAsset(Mesh.class, "meshes/plane.obj")),
                    new PhongShader(
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            null,
                            new Texture(Bitmaps.getUnicolor(1, 1, 0xff777777),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            null)
                            .setShininess(20)
                            .setSpecularIntensity(1f)
                            .setAmbientLightColor(Colors.getGray(51))
                            .setNormalMapStrength(0.5f)
                            .setReflectionIntensity(0f)));

            normalScene.addEntity(new Entity(
                    new Transform(Vector3f.RIGHT.scale(2.4f), Vector3f.UP.scale((float) Math.PI / 2), Vector3f.ONE.scale(2f)),
                    new Model(AssetManager.getAsset(Mesh.class, "meshes/plane.obj")),
                    new PhongShader(
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            null)
                            .setShininess(20)
                            .setSpecularIntensity(1f)
                            .setAmbientLightColor(Colors.getGray(51))
                            .setNormalMapStrength(1f)
                            .setReflectionIntensity(0f)));

            normalScene.addEntity(new Entity(
                    new Transform(new Vector3f(0, 2, 0)),
                    new PointLight(Vector3f.ONE.scale(1f))
            ));

            normalScene.addEntity(new Entity(
                    new Transform(new Vector3f(-2f, 1, -2)),
                    new Spotlight(new Vector3f(0, 0.4f, 0.8f), Vector3f.DOWN.add(Vector3f.LEFT).add(Vector3f.FRONT.scale(2)), 1, 1)
            ));

            normalScene.addEntity(new Entity(
                    new Transform(new Vector3f(2f, 1, -2)),
                    new Spotlight(new Vector3f(0, 0.8f, 0.4f), Vector3f.DOWN.add(Vector3f.RIGHT).add(Vector3f.FRONT.scale(2)), 1, 1)
            ));

            engine.getSceneManager().addScene("normal", normalScene);

            Scene mirrorScene = new Scene();
            mirrorScene.addEntity(
                    new Entity(
                            "Main Camera",
                            new Transform(Vector3f.FRONT.scale(10)),
                            new TurntableCameraController(),
//                            new PerspectiveCamera(90, (float) width / height, 0.1f, 1000, 0xFF6EB1FF)));
                            new PerspectiveCamera(90, (float) width / height, 0.1f, 1000, 0xff87CEEB)));

            mirrorScene.addEntity(new Entity(
                    new Transform(Vector3f.ZERO, Vector3f.ZERO, Vector3f.ONE.scale(0.8f)),
                    new Model(AssetManager.getAsset(Mesh.class, "meshes/teapot_uv.obj")),
                    new PhongShader(
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),null)
                            .setShininess(20)
                            .setSpecularIntensity(0f)
                            .setAmbientLightColor(Colors.getGray(51))
                            .setNormalMapStrength(0.5f)
                            .setReflectionIntensity(0.5f)));

            mirrorScene.addEntity(new Entity(
                    new Transform(Vector3f.LEFT.scale(5), Vector3f.ZERO, Vector3f.ONE.scale(0.8f)),
                    new Model(AssetManager.getAsset(Mesh.class, "meshes/teapot_uv.obj")),
                    new PhongShader(
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),null)
                            .setShininess(20)
                            .setSpecularIntensity(0f)
                            .setAmbientLightColor(Colors.getGray(51))
                            .setNormalMapStrength(0.5f)
                            .setReflectionIntensity(0f)));

            mirrorScene.addEntity(new Entity(
                    new Transform(Vector3f.RIGHT.scale(5), Vector3f.ZERO, Vector3f.ONE.scale(0.8f)),
                    new Model(AssetManager.getAsset(Mesh.class, "meshes/teapot_uv.obj")),
                    new PhongShader(
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),null)
                            .setShininess(20)
                            .setSpecularIntensity(0f)
                            .setAmbientLightColor(Colors.getGray(0))
                            .setNormalMapStrength(0.5f)
                            .setReflectionIntensity(1f)));

            mirrorScene.addEntity(new Entity(
                    new Transform(new Vector3f(-4, 0, 15)),
                    new DirectionalLight(Vector3f.ONE.scale(0.4f), Vector3f.BACK.add(Vector3f.DOWN))
            ));

            engine.getSceneManager().addScene("mirror", mirrorScene);

            Scene platonicScene = new Scene();
            platonicScene.addEntity(
                    new Entity(
                            "Main Camera",
                            new Transform(Vector3f.FRONT.scale(10)),
                            new TurntableCameraController(),
//                            new PerspectiveCamera(90, (float) width / height, 0.1f, 1000, 0xFF6EB1FF)));
                            new PerspectiveCamera(90, (float) width / height, 0.1f, 1000, 0xff87CEEB)));

            platonicScene.addEntity(new Entity(
                    new Transform(Vector3f.BACK.scale(3), Vector3f.ZERO, Vector3f.ONE.scale(1.2f)),
                    new Model(AssetManager.getAsset(Mesh.class, "meshes/tetrahedron.obj")),
                    new PhongShader(
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),null)
                            .setShininess(20)
                            .setSpecularIntensity(0.9f)
                            .setAmbientLightColor(Colors.getGray(51))
                            .setNormalMapStrength(0.5f)
                            .setReflectionIntensity(0.4f)));

            platonicScene.addEntity(new Entity(
                    new Transform(Vector3f.BACK.scale(3).add(Vector3f.UP.scale(3)), Vector3f.ZERO, Vector3f.ONE),
                    new Text("Tetrahedron")));

            platonicScene.addEntity(new Entity(
                    new Transform(Vector3f.BACK.scale(3).add(Vector3f.LEFT.scale(5)), Vector3f.ZERO, Vector3f.ONE.scale(2f)),
                    new Model(AssetManager.getAsset(Mesh.class, "meshes/octahedron.obj")),
                    new PhongShader(
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),null)
                            .setShininess(20)
                            .setSpecularIntensity(0.9f)
                            .setAmbientLightColor(Colors.getGray(51))
                            .setNormalMapStrength(0.5f)
                            .setReflectionIntensity(0.4f)));

            platonicScene.addEntity(new Entity(
                    new Transform(Vector3f.BACK.scale(3).add(Vector3f.LEFT.scale(5)).add(Vector3f.UP.scale(3)), Vector3f.ZERO, Vector3f.ONE),
                    new Text("Octahedron")));

            platonicScene.addEntity(new Entity(
                    new Transform(Vector3f.BACK.scale(3).add(Vector3f.RIGHT.scale(5)).add(Vector3f.DOWN), Vector3f.UP.scale((float) Math.PI), Vector3f.ONE.scale(0.7f)),
                    new Model(AssetManager.getAsset(Mesh.class, "meshes/teapot_uv.obj")),
                    new PhongShader(
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),null)
                            .setShininess(20)
                            .setSpecularIntensity(0.9f)
                            .setAmbientLightColor(Colors.getGray(51))
                            .setNormalMapStrength(0.5f)
                            .setReflectionIntensity(0.4f)));

            platonicScene.addEntity(new Entity(
                    new Transform(Vector3f.BACK.scale(3).add(Vector3f.RIGHT.scale(5)).add(Vector3f.UP.scale(3)), Vector3f.ZERO, Vector3f.ONE),
                    new Text("Utah Teapot")));

            platonicScene.addEntity(new Entity(
                    new Transform(Vector3f.FRONT.scale(3), Vector3f.ZERO, Vector3f.ONE),
                    new Model(AssetManager.getAsset(Mesh.class, "meshes/dodecahedron.obj")),
                    new PhongShader(
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),null)
                            .setShininess(20)
                            .setSpecularIntensity(0.9f)
                            .setAmbientLightColor(Colors.getGray(51))
                            .setNormalMapStrength(0.5f)
                            .setReflectionIntensity(0.4f)));

            platonicScene.addEntity(new Entity(
                    new Transform(Vector3f.FRONT.scale(3).add(Vector3f.UP.scale(3)), Vector3f.ZERO, Vector3f.ONE),
                    new Text("Dodecahedron")));

            platonicScene.addEntity(new Entity(
                    new Transform(Vector3f.FRONT.scale(3).add(Vector3f.LEFT.scale(5)), Vector3f.ZERO, Vector3f.ONE),
                    new Model(AssetManager.getAsset(Mesh.class, "meshes/icosahedron.obj")),
                    new PhongShader(
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),null)
                            .setShininess(20)
                            .setSpecularIntensity(0.9f)
                            .setAmbientLightColor(Colors.getGray(51))
                            .setNormalMapStrength(0.5f)
                            .setReflectionIntensity(0.4f)));

            platonicScene.addEntity(new Entity(
                    new Transform(Vector3f.FRONT.scale(3).add(Vector3f.LEFT.scale(5)).add(Vector3f.UP.scale(3)), Vector3f.ZERO, Vector3f.ONE),
                    new Text("Icosahedron")));

            platonicScene.addEntity(new Entity(
                    new Transform(Vector3f.FRONT.scale(3).add(Vector3f.RIGHT.scale(5)), Vector3f.ZERO, Vector3f.ONE),
                    new Model(AssetManager.getAsset(Mesh.class, "meshes/cube.obj")),
                    new PhongShader(
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),
                            new Texture(AssetManager.getAsset(Bitmap.class, "textures/color_grid.png"),
                                    UVWrapper.REPEAT, TextureSampler.LINEAR),null)
                            .setShininess(20)
                            .setSpecularIntensity(0.9f)
                            .setAmbientLightColor(Colors.getGray(51))
                            .setNormalMapStrength(0.5f)
                            .setReflectionIntensity(0.4f)));

            platonicScene.addEntity(new Entity(
                    new Transform(Vector3f.FRONT.scale(3).add(Vector3f.RIGHT.scale(5)).add(Vector3f.UP.scale(3)), Vector3f.ZERO, Vector3f.ONE),
                    new Text("Hexahedron")));

            platonicScene.addEntity(new Entity(
                    new Transform(new Vector3f(-4, 0, 15)),
                    new DirectionalLight(Vector3f.ONE.scale(0.4f), Vector3f.BACK.add(Vector3f.DOWN).add(Vector3f.RIGHT))
            ));
            engine.getSceneManager().addScene("platonic", platonicScene);

            engine.getEnvironmentManager().getAllEnvironments().stream().filter(environment -> environment.getPrefix().equals("engine")).findFirst().get().addCommand(new Command("change_scene", "Changes the active scene.") {
                @Override
                public int execute(StringBuilder stringBuilder, List<String> args) {
                    if (!args.isEmpty()) {
                        try {
                            engine.getSceneManager().changeScene(args.get(0));
                        } catch (IllegalArgumentException e) {
                            stringBuilder.append("No scene with the name '" + args.get(0) + "' found.");
                            return -1;
                        }
                    }
                    return 0;
                }
            });

            // Add lights
            currentScene.addEntity(new Entity(
                    new Transform(new Vector3f(-4, 0, 6)),
                    new PointLight(Vector3f.ONE.scale(0.8f))
            ));

            engine.getSceneManager().changeScene("blending");
        });

        // Start the engine
        engine.start();
    }
}