package com.bartodelini.pixel.modules.rendering.bitmap;

import com.bartodelini.pixel.math.vector.Vector3f;
import com.bartodelini.pixel.modules.rendering.bitmap.blending.BlendFunction;
import com.bartodelini.pixel.modules.rendering.bitmap.font.BitmapFont;
import com.bartodelini.pixel.modules.rendering.bitmap.font.StringIndexedBitmapFont;
import com.bartodelini.pixel.modules.rendering.bitmap.primitive.Line;
import com.bartodelini.pixel.modules.rendering.bitmap.primitive.Triangle;
import com.bartodelini.pixel.modules.rendering.bitmap.primitive.Vertex;
import com.bartodelini.pixel.modules.rendering.bitmap.shader.FragmentShader;
import com.bartodelini.pixel.modules.rendering.bitmap.shader.ShaderUniforms;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * A <i>BitmapGraphics3D</i> is a {@linkplain BitmapGraphics}, extended by functions allowing for the processing of 3d
 * objects.
 *
 * @author Bartolini
 * @version 1.0
 */
public class BitmapGraphics3D extends BitmapGraphics {

    private float[] depthBuffer;
    private Object[] monitorBuffer;
    private boolean depthTestingEnabled = false;
    private boolean depthBufferReadOnly = false;
    private float depthBufferClearValue = 1;

    /**
     * Allocates a new {@code BitmapGraphics3D} by passing in its {@linkplain Bitmap} and sets the
     * {@linkplain BitmapFont} of this {@code BitmapGraphics} to {@linkplain StringIndexedBitmapFont#DEFAULT_FONT}.
     *
     * @param bitmap the {@code Bitmap} to be altered by this {@code BitmapGraphics}.
     * @throws NullPointerException if the specified {@code Bitmap} is {@code null}.
     */
    public BitmapGraphics3D(Bitmap bitmap) {
        super(bitmap);
    }

    /**
     * Returns whether the depth testing is enabled for this {@code BitmapGraphics3D}.
     *
     * @return {@code true} if depth testing is enabled for this {@code BitmapGraphics3D}; {@code false} otherwise.
     */
    public boolean isDepthTestingEnabled() {
        return depthTestingEnabled;
    }

    /**
     * Sets the depth testing for this {@code BitmapGraphics3D}.
     *
     * @param enable if {@code true}, enables the depth testing for this {@code BitmapGraphics3D}; disables otherwise.
     */
    public void enableDepthTesting(boolean enable) {
        if (enable) {
            if (depthBuffer == null) {
                depthBuffer = new float[bitmap.getPixels().length];
                monitorBuffer = new Object[bitmap.getPixels().length];
                for (int i = 0; i < monitorBuffer.length; i++) {
                    monitorBuffer[i] = new Object();
                }
            }
        }
        this.depthTestingEnabled = enable;
    }

    /**
     * Returns whether the depth buffer is set to read-only for this {@code BitmapGraphics3D}.
     *
     * @return {@code true} if the depth buffer is set to read-only; {@code false} otherwise.
     */
    public boolean isDepthBufferReadOnly() {
        return depthBufferReadOnly;
    }

    /**
     * Sets whether the depth buffer of this {@code BitmapGraphics3D} should be set to read-only.
     *
     * @param readOnly if {@code true}, sets the depth buffer to read-only.
     */
    public void setDepthBufferReadOnly(boolean readOnly) {
        depthBufferReadOnly = readOnly;
    }

    /**
     * Returns the depth buffer clear value used by this {@code BitmapGraphics} when using
     * {@linkplain #clearDepthBuffer()}.
     *
     * @return the depth buffer clear value used by this {@code BitmapGraphics} when using {@code #clearDepthBuffer()}.
     */
    public float getDepthBufferClearValue() {
        return depthBufferClearValue;
    }

    /**
     * Sets the depth buffer clear value used by this {@code BitmapGraphics} when using
     * {@linkplain #clearDepthBuffer()}.
     *
     * @param depthBufferClearValue the new depth buffer clear value.
     */
    public void setDepthBufferClearValue(float depthBufferClearValue) {
        this.depthBufferClearValue = depthBufferClearValue;
    }

    /**
     * Draws a line between the two specified {@linkplain Vertex vertices} of the specified color.
     *
     * @param v1    the first {@code Vertex}.
     * @param v2    the second {@code Vertex}.
     * @param color the color of the line.
     * @throws NullPointerException if any of the specified {@code Vertices} is {@code null}.
     */
    public void drawLine(Vertex v1, Vertex v2, int color) {
        Objects.requireNonNull(v1, "v1 must not be null");
        Objects.requireNonNull(v2, "v2 must not be null");

        // Calculate the differences along the x and y axes
        float dx = v2.getScreenSpacePosition().getX() - v1.getScreenSpacePosition().getX();
        float dy = v2.getScreenSpacePosition().getY() - v1.getScreenSpacePosition().getY();
        float dd = v2.getScreenSpacePosition().getZ() - v1.getScreenSpacePosition().getZ();

        // Don't draw points
        if (dx == 0 && dy == 0) return;

        // Get the number of steps (the larger delta)
        float step = Math.max(Math.abs(dx), Math.abs(dy));
        dx /= step;
        dy /= step;
        dd /= step;

        float x = v1.getScreenSpacePosition().getX();
        float y = v1.getScreenSpacePosition().getY();
        float d = v1.getScreenSpacePosition().getZ();

        // Draw the line
        for (int i = 0; i <= step; i++) {
            final int pixelIndex = (int) x + ((int) y) * bitmap.getWidth();

            if (depthTestingEnabled) {
                synchronized (monitorBuffer[pixelIndex]) {
                    if (depthBuffer[pixelIndex] > d) {
                        if (!depthBufferReadOnly) {
                            depthBuffer[pixelIndex] = d;
                        }
                        bitmap.getPixels()[pixelIndex] = color;
                    }
                }
            } else {
                bitmap.getPixels()[pixelIndex] = color;
            }

            x += dx;
            y += dy;
            d += dd;
        }
    }

    /**
     * Draws the specified {@linkplain Line} with the specified color.
     *
     * @param line  the {@code Line} to draw.
     * @param color the color of the {@code Line}.
     * @throws NullPointerException if the specified {@code Line} is {@code null}.
     */
    public void drawLine(Line line, int color) {
        Objects.requireNonNull(line, "line must not be null");
        drawLine(line.getV1(), line.getV2(), color);
    }

    /**
     * Draws the specified {@linkplain Triangle} with the specified color.
     *
     * @param triangle the {@code Triangle} to draw.
     * @param color    the color of the {@code Triangle}.
     * @throws NullPointerException if the specified {@code Triangle} is {@code null}.
     */
    public void drawTriangle(Triangle triangle, int color) {
        Objects.requireNonNull(triangle, "triangle must not be null");
        drawLine(triangle.getV1(), triangle.getV2(), color);
        drawLine(triangle.getV2(), triangle.getV3(), color);
        drawLine(triangle.getV1(), triangle.getV3(), color);
    }

    /**
     * Shades the area inside the three specified {@linkplain Vertex Vertices}, using the specified
     * {@linkplain ShaderUniforms} and {@linkplain FragmentShader}. The shading is done using the scan conversion
     * approach.
     *
     * @param v1             the first {@code Vertex}.
     * @param v2             the second {@code Vertex}.
     * @param v3             the third {@code Vertex}.
     * @param shaderUniforms the {@code ShaderUniforms} used for the {@code FragmentShader} invocation.
     * @param fragmentShader the {@code FragmentShader} used to shade the triangle area.
     * @throws NullPointerException if any of the specified parameters is {@code null}.
     */
    public void shadeTriangle(Vertex v1, Vertex v2, Vertex v3,
                              ShaderUniforms shaderUniforms, FragmentShader fragmentShader) {
        Objects.requireNonNull(v1, "v1 must not be null");
        Objects.requireNonNull(v2, "v2 must not be null");
        Objects.requireNonNull(v3, "v3 must not be null");
        Objects.requireNonNull(shaderUniforms, "shaderUniforms must not be null");
        Objects.requireNonNull(fragmentShader, "fragmentShader must not be null");

        // Sort vertices by their y coordinate
        if (v2.getScreenSpacePosition().getY() < v1.getScreenSpacePosition().getY()) {
            Vertex temp = v2;
            v2 = v1;
            v1 = temp;
        }
        if (v3.getScreenSpacePosition().getY() < v1.getScreenSpacePosition().getY()) {
            Vertex temp = v3;
            v3 = v1;
            v1 = temp;
        }
        if (v3.getScreenSpacePosition().getY() < v2.getScreenSpacePosition().getY()) {
            Vertex temp = v3;
            v3 = v2;
            v2 = temp;
        }

        // Determine the facing side of the triangle
        Vector3f a = v2.getScreenSpacePosition().subtract(v1.getScreenSpacePosition());
        Vector3f b = v3.getScreenSpacePosition().subtract(v1.getScreenSpacePosition());
        boolean rightFacing = a.getX() * b.getY() - b.getX() * a.getY() > 0;

        // Determine the y coordinates of the vertices
        int y1 = (int) Math.ceil(v1.getScreenSpacePosition().getY());
        int y2 = (int) Math.ceil(v2.getScreenSpacePosition().getY());
        int y3 = (int) Math.ceil(v3.getScreenSpacePosition().getY());

        int scanHeight = y3 - y1;
        Vertex[][] vertices = new Vertex[scanHeight][2];

        final Vertex fv1 = v1;
        final Vertex fv2 = v2;
        final Vertex fv3 = v3;

        int side1 = rightFacing ? 0 : 1;
        Interpolator interpolator1 = new Interpolator(v1.getScreenSpacePosition().getY(), v3.getScreenSpacePosition().getY());
        IntStream.range(0, scanHeight).parallel().forEach(y -> {
            vertices[y][side1] = fv1.lerp(fv3, interpolator1.getAlphaForStep(y));
        });

        int side2 = rightFacing ? 1 : 0;
        Interpolator interpolator2 = new Interpolator(v1.getScreenSpacePosition().getY(), v2.getScreenSpacePosition().getY());
        IntStream.range(0, y2 - y1).parallel().forEach(y -> {
            vertices[y][side2] = fv1.lerp(fv2, interpolator2.getAlphaForStep(y));
        });

        Interpolator interpolator3 = new Interpolator(v2.getScreenSpacePosition().getY(), v3.getScreenSpacePosition().getY());
        IntStream.range(y2 - y1, scanHeight).parallel().forEach(y -> {
            vertices[y][side2] = fv2.lerp(fv3, interpolator3.getAlphaForStep(y - (y2 - y1)));
        });

        // Traverse the scan lines
        IntStream.range(0, scanHeight).parallel().forEach(y ->
                shadeScanLine(y + y1, vertices[y][0], vertices[y][1], shaderUniforms, fragmentShader));
    }

    /**
     * Shades the specified {@linkplain Triangle}, using the specified {@linkplain ShaderUniforms} and
     * {@linkplain FragmentShader}. The shading is done using the scan conversion approach.
     *
     * @param triangle       the {@code Triangle} to shade.
     * @param shaderUniforms the {@code ShaderUniforms} used for the {@code FragmentShader} invocation.
     * @param fragmentShader the {@code FragmentShader} used to shade the triangle area.
     * @throws NullPointerException if any of the specified parameters is {@code null}.
     */
    public void shadeTriangle(Triangle triangle, ShaderUniforms shaderUniforms, FragmentShader fragmentShader) {
        Objects.requireNonNull(triangle, "triangle must not be null");
        shadeTriangle(triangle.getV1(), triangle.getV2(), triangle.getV3(), shaderUniforms, fragmentShader);
    }

    /**
     * Shades the area inside the three specified {@linkplain Vertex Vertices}, using the specified
     * {@linkplain ShaderUniforms} and {@linkplain FragmentShader}. The shading is done using the in/out-tests approach.
     *
     * @param v1             the first {@code Vertex}.
     * @param v2             the second {@code Vertex}.
     * @param v3             the third {@code Vertex}.
     * @param shaderUniforms the {@code ShaderUniforms} used for the {@code FragmentShader} invocation.
     * @param fragmentShader the {@code FragmentShader} used to shade the triangle area.
     * @throws NullPointerException if any of the specified parameters is {@code null}.
     */
    public void shadeTriangle2(Vertex v1, Vertex v2, Vertex v3,
                               ShaderUniforms shaderUniforms, FragmentShader fragmentShader) {
        Objects.requireNonNull(v1, "v1 must not be null");
        Objects.requireNonNull(v2, "v2 must not be null");
        Objects.requireNonNull(v3, "v3 must not be null");
        Objects.requireNonNull(shaderUniforms, "shaderUniforms must not be null");
        Objects.requireNonNull(fragmentShader, "fragmentShader must not be null");

        // Sort vertices by their y coordinate
        if (v2.getScreenSpacePosition().getY() < v1.getScreenSpacePosition().getY()) {
            Vertex temp = v2;
            v2 = v1;
            v1 = temp;
        }
        if (v3.getScreenSpacePosition().getY() < v1.getScreenSpacePosition().getY()) {
            Vertex temp = v3;
            v3 = v1;
            v1 = temp;
        }
        if (v3.getScreenSpacePosition().getY() < v2.getScreenSpacePosition().getY()) {
            Vertex temp = v3;
            v3 = v2;
            v2 = temp;
        }

        // Determine the facing side of the triangle
        Vector3f a = v2.getScreenSpacePosition().subtract(v1.getScreenSpacePosition());
        Vector3f b = v3.getScreenSpacePosition().subtract(v1.getScreenSpacePosition());
        boolean rightFacing = a.getX() * b.getY() - b.getX() * a.getY() > 0;

        // Calculate the edge vectors
        final Vertex fv1 = v1;
        final Vertex fv2 = rightFacing ? v3 : v2;
        final Vertex fv3 = rightFacing ? v2 : v3;
        Vector3f nv1 = fv1.getScreenSpacePosition();
        Vector3f nv2 = fv2.getScreenSpacePosition();
        Vector3f nv3 = fv3.getScreenSpacePosition();
        Vector3f e1 = nv2.subtract(nv1);
        Vector3f e2 = nv3.subtract(nv2);
        Vector3f e3 = nv1.subtract(nv3);

        // Calculate the bounding box
        int yMin = (int) Math.ceil(v1.getScreenSpacePosition().getY());
        int yMax = (int) Math.ceil(v3.getScreenSpacePosition().getY());
        int x1 = (int) Math.ceil(v1.getScreenSpacePosition().getX());
        int x2 = (int) Math.ceil(v2.getScreenSpacePosition().getX());
        int x3 = (int) Math.ceil(v3.getScreenSpacePosition().getX());
        int xMin = Math.min(x1, Math.min(x2, x3));
        int xMax = Math.max(x1, Math.max(x2, x3));

        // Calculate the triangle area
        final float triangleArea = Math.abs(a.getX() * b.getY() - b.getX() * a.getY()) / 2;

        IntStream.range(yMin, yMax).parallel().forEach(y -> {
            IntStream.range(xMin, xMax).parallel().forEach(x -> {
                // Check if pixel is inside the triangle
                float e1Z = (x - nv1.getX()) * e1.getY() - e1.getX() * (y - nv1.getY());
                boolean inE1 = e1Z >= 0;
                if (!inE1) return;
                float e2Z = (x - nv2.getX()) * e2.getY() - e2.getX() * (y - nv2.getY());
                boolean inE2 = rightFacing ? e2Z > 0 : e2Z >= 0;
                if (!inE2) return;
                float e3Z = (x - nv3.getX()) * e3.getY() - e3.getX() * (y - nv3.getY());
                boolean inE3 = e3Z >= 0;
                if (!inE3) return;

                // Calculate the vertex for fragment shader invocation
                float area1 = Math.abs(e1Z) / 2;
                float area2 = Math.abs(e2Z) / 2;
                float area3 = Math.abs(e3Z) / 2;

                float alpha1 = area3 / (area2 + area3);
                float alpha2 = area1 / triangleArea;

                shadeFragment(x, y, fv1.lerp(fv2, alpha1).lerp(fv3, alpha2), shaderUniforms, fragmentShader);
            });
        });
    }

    /**
     * Shades the specified {@linkplain Triangle}, using the specified {@linkplain ShaderUniforms} and
     * {@linkplain FragmentShader}. The shading is done using the in/out-tests approach.
     *
     * @param triangle       the {@code Triangle} to shade.
     * @param shaderUniforms the {@code ShaderUniforms} used for the {@code FragmentShader} invocation.
     * @param fragmentShader the {@code FragmentShader} used to shade the triangle area.
     * @throws NullPointerException if any of the specified parameters is {@code null}.
     */
    public void shadeTriangle2(Triangle triangle, ShaderUniforms shaderUniforms, FragmentShader fragmentShader) {
        Objects.requireNonNull(triangle, "triangle must not be null");
        shadeTriangle2(triangle.getV1(), triangle.getV2(), triangle.getV3(), shaderUniforms, fragmentShader);
    }

    /**
     * Shades the area inside the three specified {@linkplain Vertex Vertices}, using the specified
     * {@linkplain ShaderUniforms}, {@linkplain FragmentShader}, and {@linkplain BlendFunction}. The shading is done
     * using the scan conversion approach.
     *
     * @param v1             the first {@code Vertex}.
     * @param v2             the second {@code Vertex}.
     * @param v3             the third {@code Vertex}.
     * @param shaderUniforms the {@code ShaderUniforms} used for the {@code FragmentShader} invocation.
     * @param fragmentShader the {@code FragmentShader} used to shade the triangle area.
     * @param blendFunction  the {@code BlendFunction} used to blend the shaded triangle area with the frame buffer.
     * @throws NullPointerException if any of the specified parameters is {@code null}.
     */
    public void shadeTriangle(Vertex v1, Vertex v2, Vertex v3,
                              ShaderUniforms shaderUniforms, FragmentShader fragmentShader,
                              BlendFunction blendFunction) {
        Objects.requireNonNull(v1, "v1 must not be null");
        Objects.requireNonNull(v2, "v2 must not be null");
        Objects.requireNonNull(v3, "v3 must not be null");
        Objects.requireNonNull(shaderUniforms, "shaderUniforms must not be null");
        Objects.requireNonNull(fragmentShader, "fragmentShader must not be null");
        Objects.requireNonNull(blendFunction, "blendFunction must not be null");

        // Sort vertices by their y coordinate
        if (v2.getScreenSpacePosition().getY() < v1.getScreenSpacePosition().getY()) {
            Vertex temp = v2;
            v2 = v1;
            v1 = temp;
        }
        if (v3.getScreenSpacePosition().getY() < v1.getScreenSpacePosition().getY()) {
            Vertex temp = v3;
            v3 = v1;
            v1 = temp;
        }
        if (v3.getScreenSpacePosition().getY() < v2.getScreenSpacePosition().getY()) {
            Vertex temp = v3;
            v3 = v2;
            v2 = temp;
        }

        // Determine the facing side of the triangle
        Vector3f a = v2.getScreenSpacePosition().subtract(v1.getScreenSpacePosition());
        Vector3f b = v3.getScreenSpacePosition().subtract(v1.getScreenSpacePosition());
        boolean rightFacing = a.getX() * b.getY() - b.getX() * a.getY() > 0;

        // Determine the y coordinates of the vertices
        int y1 = (int) Math.ceil(v1.getScreenSpacePosition().getY());
        int y2 = (int) Math.ceil(v2.getScreenSpacePosition().getY());
        int y3 = (int) Math.ceil(v3.getScreenSpacePosition().getY());

        int scanHeight = y3 - y1;
        Vertex[][] vertices = new Vertex[scanHeight][2];

        final Vertex fv1 = v1;
        final Vertex fv2 = v2;
        final Vertex fv3 = v3;

        int side1 = rightFacing ? 0 : 1;
        Interpolator interpolator1 = new Interpolator(v1.getScreenSpacePosition().getY(), v3.getScreenSpacePosition().getY());
        IntStream.range(0, scanHeight).parallel().forEach(y -> {
            vertices[y][side1] = fv1.lerp(fv3, interpolator1.getAlphaForStep(y));
        });

        int side2 = rightFacing ? 1 : 0;
        Interpolator interpolator2 = new Interpolator(v1.getScreenSpacePosition().getY(), v2.getScreenSpacePosition().getY());
        IntStream.range(0, y2 - y1).parallel().forEach(y -> {
            vertices[y][side2] = fv1.lerp(fv2, interpolator2.getAlphaForStep(y));
        });

        Interpolator interpolator3 = new Interpolator(v2.getScreenSpacePosition().getY(), v3.getScreenSpacePosition().getY());
        IntStream.range(y2 - y1, scanHeight).parallel().forEach(y -> {
            vertices[y][side2] = fv2.lerp(fv3, interpolator3.getAlphaForStep(y - (y2 - y1)));
        });

        // Traverse the scan lines
        IntStream.range(0, scanHeight).parallel().forEach(y ->
                shadeScanLine(y + y1, vertices[y][0], vertices[y][1], shaderUniforms, fragmentShader, blendFunction));
    }

    /**
     * Shades the specified {@linkplain Triangle}, using the specified {@linkplain ShaderUniforms},
     * {@linkplain FragmentShader}, and {@linkplain BlendFunction}. The shading is done using the scan conversion
     * approach.
     *
     * @param triangle       the {@code Triangle} to shade.
     * @param shaderUniforms the {@code ShaderUniforms} used for the {@code FragmentShader} invocation.
     * @param fragmentShader the {@code FragmentShader} used to shade the triangle area.
     * @param blendFunction  the {@code BlendFunction} used to blend the shaded triangle area with the frame buffer.
     * @throws NullPointerException if any of the specified parameters is {@code null}.
     */
    public void shadeTriangle(Triangle triangle, ShaderUniforms shaderUniforms, FragmentShader fragmentShader,
                              BlendFunction blendFunction) {
        Objects.requireNonNull(triangle, "triangle must not be null");
        shadeTriangle(triangle.getV1(), triangle.getV2(), triangle.getV3(), shaderUniforms, fragmentShader, blendFunction);
    }

    /**
     * A helper method used to shade a scan line.
     *
     * @param y              the y coordinate of the scan line.
     * @param v1             the left {@code Vertex} of the scan line.
     * @param v2             the right {@code Vertex} of the scan line.
     * @param shaderUniforms the {@code ShaderUniforms} used for the {@code FragmentShader} invocation.
     * @param fragmentShader the {@code FragmentShader} used to shade the scan line.
     */
    private void shadeScanLine(int y, Vertex v1, Vertex v2,
                               ShaderUniforms shaderUniforms, FragmentShader fragmentShader) {
        // Calculate the spans
        final int xMin = (int) Math.ceil(v1.getScreenSpacePosition().getX());
        final int xMax = (int) Math.ceil(v2.getScreenSpacePosition().getX());

        // Create an Interpolator for this scanline
        Interpolator interpolator = new Interpolator(v1.getScreenSpacePosition().getX(), v2.getScreenSpacePosition().getX());

        // Shade fragments
        IntStream.range(xMin, xMax).parallel().forEach(x -> {
            Vertex vertex = v1.lerp(v2, interpolator.getAlphaForStep(x - xMin));
            shadeFragment(x, y, vertex, shaderUniforms, fragmentShader);
        });

//        // Traverse scanline
//        for (int x = xMin; x < xMax; x++) {
//            // Shade fragment
//            Vertex vertex = v1.lerp(v2, interpolator.getCurrentAlpha());
//            shadeFragment(x, y, vertex, shaderUniforms, fragmentShader);
//
//            // Update interpolator
//            interpolator.step();
//        }
    }

    /**
     * A helper method used to shade a scan line.
     *
     * @param y              the y coordinate of the scan line.
     * @param v1             the left {@code Vertex} of the scan line.
     * @param v2             the right {@code Vertex} of the scan line.
     * @param shaderUniforms the {@code ShaderUniforms} used for the {@code FragmentShader} invocation.
     * @param fragmentShader the {@code FragmentShader} used to shade the scan line.
     * @param blendFunction  the {@code BlendFunction} used to blend the shaded scan line with the frame buffer.
     */
    private void shadeScanLine(int y, Vertex v1, Vertex v2,
                               ShaderUniforms shaderUniforms, FragmentShader fragmentShader, BlendFunction blendFunction) {
        // Calculate the spans
        final int xMin = (int) Math.ceil(v1.getScreenSpacePosition().getX());
        final int xMax = (int) Math.ceil(v2.getScreenSpacePosition().getX());

        // Create an Interpolator for this scanline
        Interpolator interpolator = new Interpolator(v1.getScreenSpacePosition().getX(), v2.getScreenSpacePosition().getX());

        // Shade fragments
        IntStream.range(xMin, xMax).parallel().forEach(x -> {
            Vertex vertex = v1.lerp(v2, interpolator.getAlphaForStep(x - xMin));
            shadeFragment(x, y, vertex, shaderUniforms, fragmentShader, blendFunction);
        });

//        // Traverse scanline
//        for (int x = xMin; x < xMax; x++) {
//            // Shade fragment
//            Vertex vertex = v1.lerp(v2, interpolator.getCurrentAlpha());
//            shadeFragment(x, y, vertex, shaderUniforms, fragmentShader);
//
//            // Update interpolator
//            interpolator.step();
//        }
    }

    /**
     * A helper method used to shade a fragment.
     *
     * @param x              the x coordinate of the fragment.
     * @param y              the y coordinate of the fragment.
     * @param vertex         the {@code Vertex} used to shade the fragment.
     * @param shaderUniforms the {@code ShaderUniforms} used for the {@code FragmentShader} invocation.
     * @param fragmentShader the {@code FragmentShader} used to shade the fragment.
     */
    private void shadeFragment(int x, int y, Vertex vertex,
                               ShaderUniforms shaderUniforms, FragmentShader fragmentShader) {
        // Calculate the pixel index
        final int pixelIndex = x + y * bitmap.getWidth();

        if (depthTestingEnabled) {
            // Synchronize this operation on the monitor object of the pixel
            synchronized (monitorBuffer[pixelIndex]) {
                if (depthBuffer[pixelIndex] > vertex.getScreenSpacePosition().getZ()) {
                    if (!depthBufferReadOnly) {
                        depthBuffer[pixelIndex] = vertex.getScreenSpacePosition().getZ();
                    }
                    bitmap.getPixels()[pixelIndex] = fragmentShader.shade(shaderUniforms, vertex);
                }
            }
        } else {
            bitmap.getPixels()[pixelIndex] = fragmentShader.shade(shaderUniforms, vertex);
        }
    }

    /**
     * A helper method used to shade a fragment.
     *
     * @param x              the x coordinate of the fragment.
     * @param y              the y coordinate of the fragment.
     * @param vertex         the {@code Vertex} used to shade the fragment.
     * @param shaderUniforms the {@code ShaderUniforms} used for the {@code FragmentShader} invocation.
     * @param fragmentShader the {@code FragmentShader} used to shade the fragment.
     * @param blendFunction  the {@code BlendFunction} used to blend the shaded fragment with the frame buffer.
     */
    private void shadeFragment(int x, int y, Vertex vertex,
                               ShaderUniforms shaderUniforms, FragmentShader fragmentShader, BlendFunction blendFunction) {
        // Calculate the pixel index
        final int pixelIndex = x + y * bitmap.getWidth();

        if (depthTestingEnabled) {
            // Synchronize this operation on the monitor object of the pixel
            synchronized (monitorBuffer[pixelIndex]) {
                if (depthBuffer[pixelIndex] > vertex.getScreenSpacePosition().getZ()) {
                    if (!depthBufferReadOnly) {
                        depthBuffer[pixelIndex] = vertex.getScreenSpacePosition().getZ();
                    }
                    int source = fragmentShader.shade(shaderUniforms, vertex);
                    int destination = bitmap.getPixels()[pixelIndex];
                    bitmap.getPixels()[pixelIndex] = blendFunction.blend(source, destination);
                }
            }
        } else {
            int source = fragmentShader.shade(shaderUniforms, vertex);
            int destination = bitmap.getPixels()[pixelIndex];
            bitmap.getPixels()[pixelIndex] = blendFunction.blend(source, destination);
        }
    }

    /**
     * Clears the depth buffer of this {@code BitmapGraphics3D} by setting all its entries to the specified value.
     *
     * @param value the value to set all entries of the depth buffer to.
     */
    public void clearDepthBuffer(float value) {
        if (isDepthTestingEnabled()) {
            Arrays.fill(depthBuffer, value);
        }
    }

    /**
     * Clears the depth buffer of this {@code BitmapGraphics3D} by setting all its entries to the depth buffer clear
     * value of this {@code BitmapGraphics3D}.
     */
    public void clearDepthBuffer() {
        clearDepthBuffer(depthBufferClearValue);
    }

    /**
     * An <i>Interpolator</i> is used to facilitate the interpolation between two {@code floats}.
     *
     * @author Bartolini
     * @version 1.0
     */
    private static class Interpolator {

        private float alpha;

        private final float startAlpha;
        private final float deltaAlpha;
        private final int stepCount;
        private final float length;

        /**
         * Allocates a new {@code Interpolator} by passing in its two {@code floats}.
         *
         * @param start the starting {@code float}.
         * @param end   the end {@code float}.
         */
        public Interpolator(float start, float end) {
            // Calculate constants
            final int startCeil = (int) Math.ceil(start);
            final int endCeil = (int) Math.ceil(end);
            final float startOffset = startCeil - start;

            // Set variables
            this.length = end - start;
            this.stepCount = endCeil - startCeil;
            this.startAlpha = startOffset / this.length;
            this.alpha = this.startAlpha;
            this.deltaAlpha = 1 / length;
        }

        /**
         * Increases the current alpha value by one step.
         */
        public void step() {
            alpha += deltaAlpha;
        }

        /**
         * Returns the current alpha value.
         *
         * @return the current alpha value.
         */
        public float getCurrentAlpha() {
            return alpha;
        }

        /**
         * Returns the alpha value for the specified step.
         *
         * @param step the step for which to return the alpha.
         * @return the alpha value for the specified step.
         */
        public float getAlphaForStep(int step) {
            return startAlpha + deltaAlpha * step;
        }

        /**
         * Returns the number of steps between the starting and end {@code floats} of this {@code Interpolator}.
         *
         * @return the number of steps between the starting and end {@code floats}.
         */
        public int getStepCount() {
            return stepCount;
        }

        /**
         * Returns the difference between the end and starting {@code float} of this {@code Interepolator}.
         *
         * @return the difference between the end and starting {@code float}.
         */
        public float getLength() {
            return length;
        }
    }
}