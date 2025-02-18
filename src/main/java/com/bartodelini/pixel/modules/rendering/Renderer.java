package com.bartodelini.pixel.modules.rendering;

import com.bartodelini.pixel.core.EngineModule;
import com.bartodelini.pixel.core.ModuleInitializeException;
import com.bartodelini.pixel.ecs.Component;
import com.bartodelini.pixel.ecs.Entity;
import com.bartodelini.pixel.environment.Variable;
import com.bartodelini.pixel.logging.Logger;
import com.bartodelini.pixel.logging.LoggerFactory;
import com.bartodelini.pixel.math.matrix.Matrices;
import com.bartodelini.pixel.math.matrix.Matrix4f;
import com.bartodelini.pixel.math.vector.Vector3f;
import com.bartodelini.pixel.math.vector.Vector4f;
import com.bartodelini.pixel.modules.asset.AssetManager;
import com.bartodelini.pixel.modules.input.Input;
import com.bartodelini.pixel.modules.input.Key;
import com.bartodelini.pixel.modules.rendering.bitmap.Bitmap;
import com.bartodelini.pixel.modules.rendering.bitmap.BitmapGraphics3D;
import com.bartodelini.pixel.modules.rendering.bitmap.Colors;
import com.bartodelini.pixel.modules.rendering.bitmap.filter.PerPixelFilter;
import com.bartodelini.pixel.modules.rendering.bitmap.filter.PostProcessingFilter;
import com.bartodelini.pixel.modules.rendering.bitmap.font.BitmapFont;
import com.bartodelini.pixel.modules.rendering.bitmap.primitive.Triangle;
import com.bartodelini.pixel.modules.rendering.bitmap.primitive.Vertex;
import com.bartodelini.pixel.modules.rendering.bitmap.shader.GeometryShader;
import com.bartodelini.pixel.modules.rendering.bitmap.shader.ShaderUniforms;
import com.bartodelini.pixel.modules.rendering.components.camera.Camera;
import com.bartodelini.pixel.modules.rendering.components.light.DirectionalLight;
import com.bartodelini.pixel.modules.rendering.components.light.Light;
import com.bartodelini.pixel.modules.rendering.components.light.Spotlight;
import com.bartodelini.pixel.modules.rendering.components.model.Mesh;
import com.bartodelini.pixel.modules.rendering.components.model.Model;
import com.bartodelini.pixel.modules.rendering.components.model.Transform;
import com.bartodelini.pixel.modules.rendering.components.shader.Shader;
import com.bartodelini.pixel.modules.rendering.components.text.Text;
import com.bartodelini.pixel.modules.rendering.util.ClipUtils;
import com.bartodelini.pixel.modules.rendering.util.FPSTracker;
import com.bartodelini.pixel.modules.rendering.util.ImageUtils;
import com.bartodelini.pixel.modules.time.Time;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A <i>Renderer</i> is an {@linkplain EngineModule} used to render and display content.
 *
 * @author Bartolini
 * @version 1.0
 */
public class Renderer extends EngineModule {

    private final Logger logger = LoggerFactory.getLogger(this);
    private final String iconPath = "/icons/pixel_icon.png";
    private final String title;
    private final Variable<Integer> varWidth;
    private final Variable<Integer> varHeight;
    private final Variable<Integer> varScale;
    private final Variable<Integer> varWireframeMode;
    private final Variable<Integer> varShowStats;
    private final Variable<Boolean> varBackFaceCulling;
    private final Variable<Boolean> varDrawBoundingMesh;
    private final Variable<Boolean> varDrawLightPositions;
    private final Variable<Boolean> varDrawText;
    private final FPSTracker fpsTracker = new FPSTracker();
    private final PostProcessingFilter noFocusFilter;

    private final AtomicLong totalTransformTime = new AtomicLong();
    private final AtomicLong totalClipTime = new AtomicLong();
    private final AtomicLong totalRenderTime = new AtomicLong();

    private JFrame frame;
    private Canvas canvas;
    private BufferedImage displayImage;
    private BufferStrategy bufferStrategy;

    private Bitmap frameBuffer;
    private BitmapGraphics3D graphics;

    private Matrix4f viewport;
    private Camera camera;

    /**
     * Allocates a new {@code Renderer} by passing in the title, width, height, and pixel scale for the window.
     *
     * @param title  the title for the window.
     * @param width  the width for the window.
     * @param height the height for the window.
     * @param scale  the size of the pixels in the window.
     * @throws NullPointerException if the specified title is {@code null}.
     */
    public Renderer(String title, int width, int height, int scale) {
        super("Renderer", "renderer");

        // Reference the title
        this.title = Objects.requireNonNull(title, "title must not be null");

        // Initialize variables
        getEnvironment().addVariable(this.varWidth = new Variable<>(
                "width", width, true, 16, false, Integer.MAX_VALUE,
                "The width of the window"));

        getEnvironment().addVariable(this.varHeight = new Variable<>(
                "height", height, true, 16, false, Integer.MAX_VALUE,
                "The height of the window"));

        getEnvironment().addVariable(this.varScale = new Variable<>(
                "scale", scale, true, 1, false, Integer.MAX_VALUE,
                "The pixel size in the window"));

        getEnvironment().addVariable(this.varWireframeMode = new Variable<>(
                "wireframe_mode", 0, true, 0, true, 2,
                "Enables the wireframe render mode."));

        getEnvironment().addVariable(this.varShowStats = new Variable<>(
                "show_stats", 0, true, 0, true, 2,
                "Enables the display of statistic data."));

        getEnvironment().addVariable(this.varBackFaceCulling = new Variable<>(
                "back-face_culling", true,
                "Enables back-face culling."));

        getEnvironment().addVariable(this.varDrawBoundingMesh = new Variable<>(
                "draw_bounding_mesh", false,
                "Enables the drawing of bounding meshes."));

        getEnvironment().addVariable(this.varDrawLightPositions = new Variable<>(
                "draw_light_positions", false,
                "Enables the drawing of light positions."));

        getEnvironment().addVariable(this.varDrawText = new Variable<>(
                "draw_text", false,
                "Enables the drawing of text."));

        this.noFocusFilter = new PerPixelFilter(color -> Colors.darken(color, 0.6f));
    }

    @Override
    public void initialize() throws ModuleInitializeException {
        // Check if rendering is supported in the current graphics environment
        if (GraphicsEnvironment.isHeadless()) {
            throw new ModuleInitializeException(this, "this graphics environment does not support rendering");
        }

        // Create frame and canvas
        frame = new JFrame(title);
        canvas = new Canvas();
        frame.add(canvas);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        // Set the frame icon
        try {
            URL iconURL = getClass().getResource(iconPath);
            Objects.requireNonNull(iconURL, "could not load main window icon");
            frame.setIconImage(new ImageIcon(ImageIO.read(iconURL)).getImage());
        } catch (NullPointerException | IllegalArgumentException | IOException e) {
            logger.error("Could not load the main window icon.", e);
        }

        // Add variable change hooks
        varWidth.addChangeHook(integer -> resizeWindow());
        varHeight.addChangeHook(integer -> resizeWindow());
        varScale.addChangeHook(integer -> resizeWindow());

        // Resize the window
        resizeWindow();
    }

    @Override
    public void start() {
        // Show the window
        frame.setVisible(true);
    }

    @Override
    public void update(double deltaTime) {
        // Save the current frame as an image
        if (Input.isKeyPressed(Key.F11)) {
            ImageUtils.saveImage(displayImage);
        }

        // Search for an active camera
        Optional<Camera> optionalCamera =
                getScene().getComponents(Camera.class).stream().filter(Component::isActive).findFirst();

        // Return if no active camera was found
        if (optionalCamera.isEmpty()) {
            // Draw error message
            String mainMessage = "NO ACTIVE CAMERA IN THE CURRENT SCENE!";
            String secondaryMessage = "Add a camera to render the scene.";
            int stringHeight = graphics.getFontMetrics().stringHeight(mainMessage);
            graphics.drawString(mainMessage,
                    frameBuffer.getWidth() / 2, frameBuffer.getHeight() / 2,
                    BitmapFont.TextAlignment.CENTER, 0xff0000);
            graphics.drawString(secondaryMessage,
                    frameBuffer.getWidth() / 2, 4 + stringHeight + frameBuffer.getHeight() / 2,
                    BitmapFont.TextAlignment.CENTER);

            // Update the canvas and return
            updateCanvas();
            return;
        }

        // Check if it's the same camera
        if (camera != optionalCamera.get()) {
            // ... if not then update
            camera = optionalCamera.get();

            // Update the aspect ratio if needed
            if (camera.getAspectRatio() != (float) frameBuffer.getWidth() / frameBuffer.getHeight()) {
                camera.setAspectRatio((float) frameBuffer.getWidth() / frameBuffer.getHeight());
            }
        }

        // Return if the found camera has no transform component attached
        Transform cameraTransform = camera.getOwner().getComponent(Transform.class);
        if (cameraTransform == null) {
            // Draw error message
            String mainMessage = "THE ACTIVE CAMERA HAS NO ATTACHED TRANSFORM COMPONENT!";
            String secondaryMessage = "Make sure to add the transform component to the camera.";
            int stringHeight = graphics.getFontMetrics().stringHeight(mainMessage);
            graphics.drawString(mainMessage,
                    frameBuffer.getWidth() / 2, frameBuffer.getHeight() / 2,
                    BitmapFont.TextAlignment.CENTER, 0xff0000);
            graphics.drawString(secondaryMessage,
                    frameBuffer.getWidth() / 2, 4 + stringHeight + frameBuffer.getHeight() / 2,
                    BitmapFont.TextAlignment.CENTER);

            // Update the canvas and return
            updateCanvas();
            return;
        }

        // Clear the screen
        graphics.clear(camera.getBackgroundColor());

        // Clear the depth buffer
        graphics.clearDepthBuffer();

        // Begin time measure
        totalTransformTime.set(0);
        totalClipTime.set(0);
        totalRenderTime.set(0);
        long frametimeStart = System.nanoTime();

        // Calculate the view matrix
        Matrix4f view = cameraTransform.getTransformMatrix().inverse();

        // Calculate the projectionView matrix (view -> projection)
        Matrix4f projectionView = camera.getProjectionMatrix().multiply(view);

        // Initialize variables for statistics
        AtomicInteger drawnTriangles = new AtomicInteger();

        // Group entities by their opacity
        List<Entity> opaqueEntities = new LinkedList<>();
        List<Entity> transparentEntities;

        Vector3f cameraPosition = new Vector3f(cameraTransform.getTransformMatrix().multiply(Vector3f.ZERO));
        Map<Float, List<Entity>> transparentEntityMap = new TreeMap<>();

        // Iterate through all entities in the scene
        getScene().getEntitiesWithComponents(Transform.class, Model.class, Shader.class).forEach(entity -> {
            Shader shader = entity.getComponent(Shader.class);

            // If entity uses an opaque shader, add it to the list of opaque entities and return...
            if (shader.isOpaque()) {
                opaqueEntities.add(entity);
                return;
            }

            // ...otherwise add the entity to the map of transparent entities
            Transform transform = entity.getComponent(Transform.class);
            Vector3f position = new Vector3f(transform.getTransformMatrix().multiply(Vector3f.ZERO));
            transparentEntityMap.computeIfAbsent(-cameraPosition.distance(position), d -> new LinkedList<>()).add(entity);
        });
        transparentEntities = transparentEntityMap.values().parallelStream().flatMap(Collection::stream).toList();

        // Draw opaque entities
        opaqueEntities.parallelStream().forEach(entity -> {
            // Reference components
            Transform transform = entity.getComponent(Transform.class);
            Model model = entity.getComponent(Model.class);
            Shader shader = entity.getComponent(Shader.class);

            // Return if any component is inactive
            if (!transform.isActive() || !model.isActive() || !shader.isActive()) {
                return;
            }

            // Calculate the shader uniforms using the uniform assembly of the shader
            ShaderUniforms shaderUniforms = shader.getShaderUniforms(camera, camera.getProjectionMatrix(), view, projectionView, transform);

            // Get shadable triangles
            List<Triangle> triangles = getShadableTriangles(model, shaderUniforms, shader);

            // Fragment shader
            triangles.parallelStream().forEach(triangle -> {
                long renderStartTime = System.nanoTime();
                graphics.shadeTriangle(triangle, shaderUniforms, shader.getFragmentShader());
                totalRenderTime.addAndGet(System.nanoTime() - renderStartTime);
                drawnTriangles.incrementAndGet();
            });
        });

        // Enable read-only access to the depth buffer
        graphics.setDepthBufferReadOnly(true);

        // Draw transparent entities
        transparentEntities.forEach(entity -> {
            // Reference components
            Transform transform = entity.getComponent(Transform.class);
            Model model = entity.getComponent(Model.class);
            Shader shader = entity.getComponent(Shader.class);

            // Return if any component is inactive
            if (!transform.isActive() || !model.isActive() || !shader.isActive()) {
                return;
            }

            // Calculate the shader uniforms using the uniform assembly of the shader
            ShaderUniforms shaderUniforms = shader.getShaderUniforms(camera, camera.getProjectionMatrix(), view, projectionView, transform);

            // Get shadable triangles
            List<Triangle> triangles = getShadableTriangles(model, shaderUniforms, shader);

            // Sort triangles by distance to camera
            if (!shader.isBackFaceCullingEnabled()) {
                Map<Float, List<Triangle>> triangleMap = new TreeMap<>();
                triangles.forEach(triangle -> {
                    float meanDepth = triangle.getV1().getScreenSpacePosition().getZ()
                            + triangle.getV2().getScreenSpacePosition().getZ()
                            + triangle.getV3().getScreenSpacePosition().getZ();
                    meanDepth /= 3;
                    triangleMap.computeIfAbsent(-meanDepth, d -> new LinkedList<>()).add(triangle);
                });
                triangles = triangleMap.values().parallelStream().flatMap(Collection::stream).toList();
            }

            // Group triangles into front facing ones and back facing ones
            List<Triangle> frontFacingTriangles = new LinkedList<>();
            List<Triangle> backFacingTriangles = new LinkedList<>();
            triangles.forEach(triangle -> {
                if (triangle.isFrontFacing()) {
                    frontFacingTriangles.add(triangle);
                } else {
                    backFacingTriangles.add(triangle);
                }
            });

            // Render the back facing triangles
            backFacingTriangles.forEach(triangle -> {
                long renderStartTime = System.nanoTime();
                graphics.shadeTriangle(triangle, shaderUniforms, shader.getFragmentShader(), shader.getBlendFunction());
                totalRenderTime.addAndGet(System.nanoTime() - renderStartTime);
                drawnTriangles.incrementAndGet();
            });

            // Render the front facing triangles
            frontFacingTriangles.forEach(triangle -> {
                long renderStartTime = System.nanoTime();
                graphics.shadeTriangle(triangle, shaderUniforms, shader.getFragmentShader(), shader.getBlendFunction());
                totalRenderTime.addAndGet(System.nanoTime() - renderStartTime);
                drawnTriangles.incrementAndGet();
            });
        });

        // Disable read-only access to the depth buffer
        graphics.setDepthBufferReadOnly(false);

        // Draw light icons
        if (varDrawLightPositions.getValue()) {
            getScene().getEntitiesWithComponents(Transform.class, Light.class).parallelStream().forEach(entity -> {
                // Reference components
                Transform transform = entity.getComponent(Transform.class);
                Light light = entity.getComponent(Light.class);

                // Return if any component is inactive
                if (!transform.isActive() || !light.isActive()) {
                    return;
                }

                // Calculate clip space position
                Vector3f position = transform.getPosition();
                Vector4f clipSpacePosition = projectionView.multiply(position);

                // Return if the position is outside the camera frustum
                if (ClipUtils.getOutCode(clipSpacePosition) != ClipUtils.OutCode.INSIDE) {
                    return;
                }

                // Calculate screen space position
                Vector4f screenSpacePosition = viewport.multiply(clipSpacePosition.scale(1 / clipSpacePosition.getW()));

                // Draw light icon
                String bitmapName = "point_light.png";
                if (light instanceof DirectionalLight) {
                    bitmapName = "directional_light.png";
                } else if (light instanceof Spotlight) {
                    bitmapName = "spotlight.png";
                }
                Bitmap lightBitmap = AssetManager.getAsset(Bitmap.class, "textures/" + bitmapName);
                graphics.drawBitmap(lightBitmap,
                        (int) screenSpacePosition.getX() - lightBitmap.getWidth() / 2,
                        (int) screenSpacePosition.getY() - lightBitmap.getHeight() / 2,
                        Colors.getColor(light.getColor()));
            });
        }

        // Draw text
        if (varDrawText.getValue()) {
            getScene().getEntitiesWithComponents(Transform.class, Text.class).parallelStream().forEach(entity -> {
                // Reference components
                Transform transform = entity.getComponent(Transform.class);
                Text text = entity.getComponent(Text.class);

                // Return if any component is inactive
                if (!transform.isActive() || !text.isActive()) {
                    return;
                }

                // Calculate clip space position
                Vector3f position = transform.getPosition();
                Vector4f clipSpacePosition = projectionView.multiply(position);

                // Return if the position is outside the camera frustum
                if (ClipUtils.getOutCode(clipSpacePosition) != ClipUtils.OutCode.INSIDE) {
                    return;
                }

                // Calculate screen space position
                Vector4f screenSpacePosition = viewport.multiply(clipSpacePosition.scale(1 / clipSpacePosition.getW()));

                // Draw text
                int x = (int) screenSpacePosition.getX();
                int y = (int) screenSpacePosition.getY();
                graphics.drawString(text.getText(), x, y, text.getAlignment(), text.getColor());
            });
        }

        // Apply post-processing filters
        camera.getPostProcessingFilters().forEach(filter -> filter.filter(frameBuffer));

        if (varWireframeMode.getValue() > 0) {
            graphics.drawString("Wireframe mode", 10, 10, 0xffffff, 0xa0a0a0);
        }

        // Update the fps counter
        fpsTracker.update(deltaTime);

        // Draw statistics
        if (varShowStats.getValue() > 0) {
            graphics.drawString(fpsTracker.getFPS() + " FPS",
                    frameBuffer.getWidth() - 10, 10, BitmapFont.TextAlignment.RIGHT, 0xffffff, 0xa0a0a0);
            graphics.drawString(drawnTriangles.get() + " TRI",
                    frameBuffer.getWidth() - 10, 20, BitmapFont.TextAlignment.RIGHT, 0xffffff, 0xa0a0a0);
        }
        if (varShowStats.getValue() > 1) {
            // Calculate total times
            final long totalFrametime = System.nanoTime() - frametimeStart;
            graphics.drawString("Real Frametime: " + String.format(Locale.US, "%.2f", (totalFrametime / 1e6)) + " ms", 10, 10, 0xffffff, 0xa0a0a0);

            double totalTransformTimeMS = ((double) totalTransformTime.get()) / 1e6;
            double totalClipTimeMS = ((double) totalClipTime.get()) / 1e6;
            double totalRenderTimeMS = ((double) totalRenderTime.get()) / 1e6;
            double totalMS = totalTransformTimeMS + totalClipTimeMS + totalRenderTimeMS;
            graphics.drawString("Transform: " + String.format(Locale.US, "%.2f", totalTransformTimeMS) + " ms (" + String.format(Locale.US, "%.2f", 100 * totalTransformTimeMS / totalMS) + "%)", 10, 20, 0xffffff, 0xa0a0a0);
            graphics.drawString("Clip:      " + String.format(Locale.US, "%.2f", totalClipTimeMS) + " ms (" + String.format(Locale.US, "%.2f", 100 * totalClipTimeMS / totalMS) + "%)", 10, 30, 0xffffff, 0xa0a0a0);
            graphics.drawString("Rasterize: " + String.format(Locale.US, "%.2f", totalRenderTimeMS) + " ms (" + String.format(Locale.US, "%.2f", 100 * totalRenderTimeMS / totalMS) + "%)", 10, 40, 0xffffff, 0xa0a0a0);
        }

        // Inform if not focused
        if (!canvas.hasFocus()) {
            // Darken the screen
            noFocusFilter.filter(frameBuffer);

            // Render message
            int color = Colors.getGray((int) (180 + (255 - 180) * (Math.sin(Time.getTime() * 10))));
            graphics.drawString("Click to focus!",
                    graphics.getWidth() / 2, graphics.getHeight() / 2, BitmapFont.TextAlignment.CENTER, color);
        }

        // Update the canvas
        updateCanvas();
    }

    /**
     * A helper method used to return a {@linkplain List} of shadable {@linkplain Triangle Triangles}.
     *
     * @param model          the {@linkplain Model} where the {@code Triangles} originate from.
     * @param shaderUniforms the {@linkplain ShaderUniforms} for the current shader invocation.
     * @param shader         the {@linkplain Shader}.
     * @return a {@code List} of shadable {@code Triangles}.
     */
    private List<Triangle> getShadableTriangles(Model model, ShaderUniforms shaderUniforms, Shader shader) {
        // Process vertices and assemble primitives of the bounding mesh
        List<Triangle> boundingMeshTriangles = processVerticesAndAssemblePrimitives(model.getBoundingMesh(), shaderUniforms, shader);

        // Return if the bounding mesh is completely outside the camera frustum
        if (ClipUtils.allTrianglesCompletelyOutside(boundingMeshTriangles)) {
            return new LinkedList<>();
        }

        // Process vertices and assemble primitives
        List<Triangle> triangles = processVerticesAndAssemblePrimitives(model.getMesh(), shaderUniforms, shader);

        // Clip if needed
        final boolean partiallyVisible = !ClipUtils.allTrianglesCompletelyInside(boundingMeshTriangles);
        if (partiallyVisible) {
            boundingMeshTriangles = boundingMeshTriangles.parallelStream().flatMap(triangle -> ClipUtils.clipTriangleAgainstClipVolume(triangle).stream()).toList();
            long clipTimeStart = System.nanoTime();
            triangles = triangles.parallelStream().flatMap(triangle -> ClipUtils.clipTriangleAgainstClipVolume(triangle).stream()).toList();
            totalClipTime.addAndGet(System.nanoTime() - clipTimeStart);
        }

        // If selected, render the bounding mesh
        if (varDrawBoundingMesh.getValue()) {
            // Perspective division and viewport transform of the bounding mesh
            perspectiveDivisionAndViewportTransform(boundingMeshTriangles);
            int color = partiallyVisible ? 0xffff0000 : 0xff00ff00;
            drawTriangles(boundingMeshTriangles, color);
        }

        // Perspective division and viewport transform
        perspectiveDivisionAndViewportTransform(triangles);

        // Back-face culling
        if (shader.isBackFaceCullingEnabled() && varBackFaceCulling.getValue()) {
            triangles = triangles.parallelStream().filter(Triangle::isFrontFacing).toList();
        }

        // If selected, render in wireframe mode
        if (varWireframeMode.getValue() > 0) {
            drawTriangles(triangles, 0xffff00ff);
            if (varWireframeMode.getValue() == 1) {
                return new LinkedList<>();
            }
        }

        return triangles;
    }

    /**
     * A helper method used to process and assemble {@linkplain Triangle Triangles} from a specified {@linkplain Mesh},
     * {@linkplain ShaderUniforms}, and {@linkplain Shader}.
     *
     * @param mesh           the {@code Mesh} where the {@code Triangles} originate from.
     * @param shaderUniforms the {@code ShaderUniforms} for the current shader invocation.
     * @param shader         the {@code Shader}.
     * @return a {@code List} of processed and assembled {@code Triangles}.
     */
    private List<Triangle> processVerticesAndAssemblePrimitives(Mesh mesh, ShaderUniforms shaderUniforms, Shader shader) {
        // Get a list of vertices using the Vertex Assembly of the shader
        List<Vertex> vertices = shader.getVertexAssembly().getVertices(mesh);

        // Apply the vertex shader of the shader to each vertex
        long transformStartTime = System.nanoTime();
        vertices.parallelStream().forEach(vertex -> shader.getVertexShader().shade(shaderUniforms, vertex));
        totalTransformTime.addAndGet(System.nanoTime() - transformStartTime);

        // Check if a shader has altered the screen space position
        if (vertices.parallelStream().anyMatch(vertex -> vertex.getScreenSpacePosition() != null)) {
            throw new IllegalStateException("a vertex shader has set the screen space position of a vertex");
        }

        // Get a list of triangles using the Primitive Assembly of the shader
        List<Triangle> triangles = shader.getPrimitiveAssembly().getTrianglesFromVertices(vertices);

        // Apply the geometry shader of the shader if present
        GeometryShader geometryShader = shader.getGeometryShader();
        if (geometryShader != null) {
            triangles = triangles.parallelStream()
                    .flatMap(triangle -> geometryShader.shade(shaderUniforms, triangle).stream())
                    .filter(Triangle.class::isInstance).map(Triangle.class::cast).toList();
        }

        return triangles;
    }

    /**
     * A helper method used to perform the perspective division and the viewport transformation on the specified
     * {@linkplain List} of {@linkplain Triangle Triangles}.
     *
     * @param triangles the {@code List} of {@code Triangles} to process.
     */
    private void perspectiveDivisionAndViewportTransform(List<Triangle> triangles) {
        // Perspective division and viewport transform
        triangles.parallelStream().forEach(triangle -> {
            // Reference vertex positions
            Vector4f p1 = triangle.getV1().getClipSpacePosition();
            Vector4f p2 = triangle.getV2().getClipSpacePosition();
            Vector4f p3 = triangle.getV3().getClipSpacePosition();

            // Perspective division
            p1 = p1.scale(1 / p1.getW());
            p2 = p2.scale(1 / p2.getW());
            p3 = p3.scale(1 / p3.getW());

            // Viewport transform
            p1 = viewport.multiply(p1);
            p2 = viewport.multiply(p2);
            p3 = viewport.multiply(p3);

            // Apply changes
            triangle.getV1().setScreenSpacePosition(new Vector3f(p1));
            triangle.getV2().setScreenSpacePosition(new Vector3f(p2));
            triangle.getV3().setScreenSpacePosition(new Vector3f(p3));
        });
    }

    /**
     * A helper method used to draw the outlines of a {@linkplain List} of {@linkplain Triangle Triangles} with the
     * specified color.
     *
     * @param triangles the {@code List} of {@code Triangles} to draw.
     * @param color     the color.
     */
    private void drawTriangles(List<Triangle> triangles, int color) {
        triangles.parallelStream().forEach(triangle -> graphics.drawTriangle(triangle, color));
    }

    /**
     * Renders the framebuffer {@linkplain Bitmap} to the displayed {@linkplain Canvas}.
     * <p>
     * The rendering is performed by drawing the framebuffer {@code Bitmap} to the {@linkplain BufferStrategy} of the
     * {@code Canvas} using its {@linkplain Graphics} instance.
     * <p>
     * If needed, multiple renders will be performed, as described in {@linkplain BufferStrategy}.
     */
    private void updateCanvas() {
        // Render single frame
        do {
            // The following loop ensures that the contents of the drawing buffer
            // are consistent in case the underlying surface was recreated
            do {
                // Get a new graphics context every time through the loop
                // to make sure the strategy is validated
                Graphics imageGraphics = bufferStrategy.getDrawGraphics();

//                imageGraphics.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                // Render checker pattern (highlights transparent areas)
                for (int i = 0; i < frame.getWidth() - 8; i += 8) {
                    for (int j = 0; j < frame.getHeight() - 8; j += 8) {
                        if ((j + i) % 16 == 0) {
                            imageGraphics.setColor(Color.LIGHT_GRAY);
                        } else {
                            imageGraphics.setColor(Color.GRAY);
                        }
                        imageGraphics.fillRect(i, j, 8, 8);
                    }
                }
                // Render to graphics
                imageGraphics.drawImage(
                        displayImage, 0, 0,
                        varWidth.getValue() * varScale.getValue(), varHeight.getValue() * varScale.getValue(),
                        null);

                // Dispose the graphics
                imageGraphics.dispose();

                // Repeat the rendering if the drawing buffer contents
                // were restored
            } while (bufferStrategy.contentsRestored());

            // Display the buffer
            bufferStrategy.show();

            // Repeat the rendering if the drawing buffer was lost
        } while (bufferStrategy.contentsLost());
    }

    /**
     * Resizes the window. Should be called when changes to the width, height or scale are made.
     */
    private void resizeWindow() {
        // Update camera
        if (camera != null) {
            // Update the aspect ratio
            camera.setAspectRatio((float) varWidth.getValue() / varHeight.getValue());
        }

        // Update the viewport matrix
        viewport = Matrices.getViewport(varWidth.getValue(), varHeight.getValue());

        // Resize canvas
        Dimension size = new Dimension(
                varWidth.getValue() * varScale.getValue(),
                varHeight.getValue() * varScale.getValue());
        canvas.setSize(size);
        canvas.setMinimumSize(size);
        canvas.setPreferredSize(size);
        canvas.setMaximumSize(size);

        // Resize frame
        frame.pack();
        frame.setLocationRelativeTo(null);

        // Update displayImage
        displayImage = new BufferedImage(varWidth.getValue(), varHeight.getValue(), BufferedImage.TYPE_INT_ARGB);
        int[] pixels = ((DataBufferInt) displayImage.getRaster().getDataBuffer()).getData();

        // Update BufferStrategy
        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();

        // Create the framebuffer Bitmap
        frameBuffer = new Bitmap(varWidth.getValue(), varHeight.getValue(), pixels);

        // Reference BitmapGraphics of the framebuffer
        graphics = (BitmapGraphics3D) frameBuffer.getGraphics();

        // Enable depth testing
        graphics.enableDepthTesting(true);
    }
}