package com.bartodelini.pixel.modules.rendering.components.shader;

import com.bartodelini.pixel.math.matrix.Matrix3f;
import com.bartodelini.pixel.math.matrix.Matrix4f;
import com.bartodelini.pixel.math.vector.Vector2f;
import com.bartodelini.pixel.math.vector.Vector3f;
import com.bartodelini.pixel.math.vector.Vector4f;
import com.bartodelini.pixel.modules.rendering.bitmap.Colors;
import com.bartodelini.pixel.modules.rendering.bitmap.blending.BlendFunction;
import com.bartodelini.pixel.modules.rendering.bitmap.primitive.Vertex;
import com.bartodelini.pixel.modules.rendering.bitmap.shader.FragmentShader;
import com.bartodelini.pixel.modules.rendering.bitmap.shader.ShaderUniforms;
import com.bartodelini.pixel.modules.rendering.bitmap.shader.VertexShader;
import com.bartodelini.pixel.modules.rendering.components.camera.Camera;
import com.bartodelini.pixel.modules.rendering.components.light.DirectionalLight;
import com.bartodelini.pixel.modules.rendering.components.light.PointLight;
import com.bartodelini.pixel.modules.rendering.components.light.Spotlight;
import com.bartodelini.pixel.modules.rendering.components.model.Mesh;
import com.bartodelini.pixel.modules.rendering.components.model.Transform;
import com.bartodelini.pixel.modules.rendering.components.shader.textures.CubeMap;
import com.bartodelini.pixel.modules.rendering.components.shader.textures.Texture;

import java.util.ArrayList;
import java.util.List;

/**
 * A <i>PhongShader</i> is a {@linkplain Shader} which implements the Phong reflection model.
 *
 * @author Bartolini
 * @version 1.0
 */
public class PhongShader extends Shader {

    private static final VertexAssembly vertexAssembly = mesh -> {
        List<Vertex> vertices = new ArrayList<>();
        for (Mesh.Face face : mesh.faces()) {
            Vertex v1 = new Vertex(Vector4f.ZERO, null);
            v1.setVec3("position", mesh.positions()[face.p1().positionIndex()]);
            v1.setVec2("uv", mesh.uvs()[face.p1().uvIndex()]);
            v1.setVec3("normal", mesh.normals()[face.p1().normalIndex()]);
            v1.setVec3("tangent", face.p1().tangent());
            vertices.add(v1);

            Vertex v2 = new Vertex(Vector4f.ZERO, null);
            v2.setVec3("position", mesh.positions()[face.p2().positionIndex()]);
            v2.setVec2("uv", mesh.uvs()[face.p2().uvIndex()]);
            v2.setVec3("normal", mesh.normals()[face.p2().normalIndex()]);
            v2.setVec3("tangent", face.p2().tangent());
            vertices.add(v2);

            Vertex v3 = new Vertex(Vector4f.ZERO, null);
            v3.setVec3("position", mesh.positions()[face.p3().positionIndex()]);
            v3.setVec2("uv", mesh.uvs()[face.p3().uvIndex()]);
            v3.setVec3("normal", mesh.normals()[face.p3().normalIndex()]);
            v3.setVec3("tangent", face.p3().tangent());
            vertices.add(v3);
        }
        return vertices;
    };

    private static final VertexShader vertexShader = (shaderUniforms, vertex) -> {
        // Calculate the position in view space
        Matrix4f viewModel = (Matrix4f) shaderUniforms.getUniform("viewModel");
        Vector3f passPositionCamera = new Vector3f(viewModel.multiply(vertex.getVec3("position")));
        vertex.setVec3("passPositionCamera", passPositionCamera);

        // Calculate the normal position in view space
        Matrix4f viewModelTransposeInverse = (Matrix4f) shaderUniforms.getUniform("viewModelTransposeInverse");
        Vector3f passNormalCamera = new Vector3f(viewModelTransposeInverse.multiply(vertex.getVec3("normal"), 0));
        vertex.setVec3("passNormalCamera", passNormalCamera);

        // Calculate the tangent in view space
        Vector3f passTangentCamera = new Vector3f(viewModelTransposeInverse.multiply(vertex.getVec3("tangent"), 0));
        vertex.setVec3("passTangentCamera", passTangentCamera);

        // Set the position
        Matrix4f projectionViewModel = (Matrix4f) shaderUniforms.getUniform("projectionViewModel");
        Vector4f clipSpacePosition = projectionViewModel.multiply(vertex.getVec3("position"));
        vertex.setClipSpacePosition(clipSpacePosition);
    };

    private final FragmentShader fragmentShader;

    private Texture diffuseTexture;
    private Texture normalMap;
    private Texture specularMap;
    private Texture reflectionMap;
    private CubeMap environmentMap;
    private int ambientLightColor = 0;
    private int diffuseMaterialColor = 0;
    private int specularMaterialColor = 0;
    private float shininess = 1;
    private float normalMapStrength = 1;
    private float specularIntensity = 1;
    private float reflectionIntensity = 1;
    private boolean opaque = true;
    private float transparency = 1;
    private boolean cullBackFaces = true;
    private BlendFunction blendFunction = BlendFunction.DEFAULT;

    /**
     * Allocates a new {@code PhongShader}.
     */
    public PhongShader() {
        this.fragmentShader = createFragmentShader();
    }

    /**
     * Allocates a new {@code PhongShader} by passing in its diffuse, normal, specular and reflection
     * {@linkplain Texture Textures}, as well as the {@linkplain CubeMap} used as its environment map.
     *
     * @param diffuseTexture the diffuse {@code Texture}.
     * @param normalMap      the normal map {@code Texture}.
     * @param specularMap    the specular map {@code Texture}.
     * @param reflectionMap  the reflection map {@code Texture}.
     * @param environmentMap the environment map {@code CubeMap}.
     */
    public PhongShader(Texture diffuseTexture, Texture normalMap, Texture specularMap, Texture reflectionMap,
                       CubeMap environmentMap) {
        this.diffuseTexture = diffuseTexture;
        this.normalMap = normalMap;
        this.specularMap = specularMap;
        this.reflectionMap = reflectionMap;
        this.environmentMap = environmentMap;
        this.fragmentShader = createFragmentShader();
    }

    /**
     * Returns the diffuse {@linkplain Texture} used by this {@code PhongShader}.
     *
     * @return the diffuse {@code Texture} used by this {@code PhongShader}.
     */
    public Texture getDiffuseTexture() {
        return diffuseTexture;
    }

    /**
     * Sets the diffuse {@linkplain Texture} for this {@code PhongShader}.
     *
     * @param texture the new diffuse {@code Texture} for this {@code PhongShader}.
     * @return this {@code PhongShader} for method chaining.
     */
    public PhongShader setDiffuseTexture(Texture texture) {
        this.diffuseTexture = texture;
        return this;
    }

    /**
     * Returns the normal map {@linkplain Texture} used by this {@code PhongShader}.
     *
     * @return the normal map {@code Texture} used by this {@code PhongShader}.
     */
    public Texture getNormalMap() {
        return normalMap;
    }

    /**
     * Sets the normal map {@linkplain Texture} for this {@code PhongShader}.
     *
     * @param texture the new normal map {@code Texture} for this {@code PhongShader}.
     * @return this {@code PhongShader} for method chaining.
     */
    public PhongShader setNormalMap(Texture texture) {
        this.normalMap = texture;
        return this;
    }

    /**
     * Returns the specular map {@linkplain Texture} used by this {@code PhongShader}.
     *
     * @return the specular map {@code Texture} used by this {@code PhongShader}.
     */
    public Texture getSpecularMap() {
        return specularMap;
    }

    /**
     * Sets the specular map {@linkplain Texture} for this {@code PhongShader}.
     *
     * @param texture the new specular map {@code Texture} for this {@code PhongShader}.
     * @return this {@code PhongShader} for method chaining.
     */
    public PhongShader setSpecularMap(Texture texture) {
        this.specularMap = texture;
        return this;
    }

    /**
     * Returns the reflection map {@linkplain Texture} used by this {@code PhongShader}.
     *
     * @return the reflection map {@code Texture} used by this {@code PhongShader}.
     */
    public Texture getReflectionMap() {
        return reflectionMap;
    }

    /**
     * Sets the reflection map {@linkplain Texture} for this {@code PhongShader}.
     *
     * @param texture the new reflection map {@code Texture} for this {@code PhongShader}.
     * @return this {@code PhongShader} for method chaining.
     */
    public PhongShader setReflectionMap(Texture texture) {
        this.reflectionMap = texture;
        return this;
    }

    /**
     * Returns the environment map {@linkplain CubeMap} used by this {@code PhongShader}.
     *
     * @return the environment map {@code CubeMap} used by this {@code PhongShader}.
     */
    public CubeMap getEnvironmentMap() {
        return environmentMap;
    }

    /**
     * Sets the environment map {@linkplain CubeMap} for this {@code PhongShader}.
     *
     * @param texture the new environment map {@code CubeMap} for this {@code PhongShader}.
     * @return this {@code PhongShader} for method chaining.
     */
    public PhongShader setEnvironmentMap(CubeMap texture) {
        this.environmentMap = texture;
        return this;
    }

    /**
     * Returns the color of the ambient light used by this {@code PhongShader}.
     *
     * @return the color of the ambient light used by this {@code PhongShader}.
     */
    public int getAmbientLightColor() {
        return ambientLightColor;
    }

    /**
     * Sets the ambient light color for this {@code PhongShader}.
     *
     * @param color the new ambient light color for this {@code PhongShader}.
     * @return this {@code PhongShader} for method chaining.
     */
    public PhongShader setAmbientLightColor(int color) {
        this.ambientLightColor = color;
        return this;
    }

    /**
     * Returns the diffuse color used by this {@code PhongShader}.
     *
     * @return the diffuse color used by this {@code PhongShader}.
     */
    public int getDiffuseColor() {
        return diffuseMaterialColor;
    }

    /**
     * Sets the diffuse color for this {@code PhongShader}.
     *
     * @param color the new diffuse color for this {@code PhongShader}.
     * @return this {@code PhongShader} for method chaining.
     */
    public PhongShader setDiffuseColor(int color) {
        this.diffuseMaterialColor = color;
        return this;
    }

    /**
     * Returns the specular color used by this {@code PhongShader}.
     *
     * @return the specular color used by this {@code PhongShader}.
     */
    public int getSpecularColor() {
        return specularMaterialColor;
    }

    /**
     * Sets the specular color for this {@code PhongShader}.
     *
     * @param color the new specular color for this {@code PhongShader}.
     * @return this {@code PhongShader} for method chaining.
     */
    public PhongShader setSpecularColor(int color) {
        this.specularMaterialColor = color;
        return this;
    }

    /**
     * Returns the shininess value used by this {@code PhongShader}.
     *
     * @return the shininess value used by this {@code PhongShader}.
     */
    public float getShininess() {
        return shininess;
    }

    /**
     * Sets the shininess value for this {@code PhongShader}.
     *
     * @param shininess the new shininess value for this {@code PhongShader}.
     * @return this {@code PhongShader} for method chaining.
     */
    public PhongShader setShininess(float shininess) {
        this.shininess = shininess;
        return this;
    }

    /**
     * Returns the normal map strength used by this {@code PhongShader}.
     *
     * @return the normal map strength used by this {@code PhongShader}.
     */
    public float getNormalMapStrength() {
        return normalMapStrength;
    }

    /**
     * Sets the normal map strength for this {@code PhongShader}.
     *
     * @param strength the new normal map strength for this {@code PhongShader}.
     * @return this {@code PhongShader} for method chaining.
     */
    public PhongShader setNormalMapStrength(float strength) {
        this.normalMapStrength = strength;
        return this;
    }

    /**
     * Returns the specular intensity used by this {@code PhongShader}.
     *
     * @return the specular intensity used by this {@code PhongShader}.
     */
    public float getSpecularIntensity() {
        return specularIntensity;
    }

    /**
     * Sets the specular intensity for this {@code PhongShader}.
     *
     * @param intensity the new specular intensity for this {@code PhongShader}.
     * @return this {@code PhongShader} for method chaining.
     */
    public PhongShader setSpecularIntensity(float intensity) {
        this.specularIntensity = intensity;
        return this;
    }

    /**
     * Returns the reflection intensity used by this {@code PhongShader}.
     *
     * @return the reflection intensity used by this {@code PhongShader}.
     */
    public float getReflectionIntensity() {
        return reflectionIntensity;
    }

    /**
     * Sets the reflection intensity for this {@code PhongShader}.
     *
     * @param intensity the new reflection intensity for this {@code PhongShader}.
     * @return this {@code PhongShader} for method chaining.
     */
    public PhongShader setReflectionIntensity(float intensity) {
        this.reflectionIntensity = intensity;
        return this;
    }

    @Override
    public boolean isOpaque() {
        return opaque;
    }

    /**
     * Sets whether this {@code PhongShader} should be opaque.
     *
     * @param opaque the new opacity for this {@code PhongShader}.
     * @return this {@code PhongShader} for method chaining.
     */
    public PhongShader setOpaque(boolean opaque) {
        this.opaque = opaque;
        return this;
    }

    /**
     * Returns the transparency of this {@code PhongShader}.
     *
     * @return the transparency of this {@code PhongShader}.
     */
    public float getTransparency() {
        return transparency;
    }

    /**
     * Sets the transparency of this {@code PhongShader} should be opaque.
     *
     * @param alpha the new transparency for this {@code PhongShader}.
     * @return this {@code PhongShader} for method chaining.
     */
    public PhongShader setTransparency(float alpha) {
        transparency = alpha;
        return this;
    }

    @Override
    public boolean isBackFaceCullingEnabled() {
        return cullBackFaces;
    }

    /**
     * Sets whether the back-face culling should be enabled for this {@code PhongShader}.
     *
     * @param enable the new value for the back-face culling.
     * @return this {@code PhongShader} for method chaining.
     */
    public PhongShader enableBackFaceCulling(boolean enable) {
        cullBackFaces = enable;
        return this;
    }

    @Override
    public BlendFunction getBlendFunction() {
        return blendFunction;
    }

    /**
     * Sets the {@linkplain BlendFunction} for this {@code PhongShader}.
     *
     * @param blendFunction the new {@code BlendFunction} for this {@code PhongShader}.
     * @return this {@code PhongShader} for method chaining.
     */
    public PhongShader setBlendFunction(BlendFunction blendFunction) {
        this.blendFunction = blendFunction;
        return this;
    }

    /**
     * Creates a {@linkplain FragmentShader} instance for this {@code PhongShader}.
     *
     * @return the {@code FragmentShader} of this {@code PhongShader}.
     */
    private FragmentShader createFragmentShader() {
        return new FragmentShader() {
            @Override
            public int shade(ShaderUniforms shaderUniforms, Vertex vertex) {
                // Retrieve the uv coordinates
                Vector2f uv = vertex.getVec2("uv");

//                if (true) {
//                    return 0xffc61a27;
//                }

                // Sample the texel color from the diffuse texture
                int diffuseColor;
                if (diffuseTexture != null) {
                    diffuseColor = diffuseTexture.getTexel(uv);
                } else {
                    diffuseColor = diffuseMaterialColor;
                }

                // Save the alpha value
                int alpha;
                if (opaque) {
                    alpha = 0xff;
                } else {
                    alpha = (int) (Colors.getAlpha(diffuseColor) * transparency);
                }

                // Ambient term
                final int ambientTerm = Colors.multiply(diffuseColor, ambientLightColor);

                // Retrieve directional lights information
                Vector3f[] directionalLightDirections = (Vector3f[]) shaderUniforms.getUniform("directionalLightDirectionsCamera");
                Integer[] directionalLightColors = (Integer[]) shaderUniforms.getUniform("directionalLightColors");

                // Retrieve point lights information
                Vector3f[] pointLightPositions = (Vector3f[]) shaderUniforms.getUniform("pointLightPositionsCamera");
                Integer[] pointLightColors = (Integer[]) shaderUniforms.getUniform("pointLightColors");

                // Retrieve spotlights information
                Vector3f[] spotlightPositions = (Vector3f[]) shaderUniforms.getUniform("spotlightPositionsCamera");
                Vector3f[] spotlightDirections = (Vector3f[]) shaderUniforms.getUniform("spotlightDirectionsCamera");
                Spotlight[] spotlights = (Spotlight[]) shaderUniforms.getUniform("spotlights");
                Integer[] spotlightColors = (Integer[]) shaderUniforms.getUniform("spotlightColors");

                // Return just the ambient term, if no lights are present and the environment map is not specified
                final boolean noLightsPresent = directionalLightColors.length == 0 && pointLightColors.length == 0 && spotlightColors.length == 0;
                if (noLightsPresent && environmentMap == null) {
                    return ambientTerm;
                }

                // Retrieve the position of the fragment in camera space (and normalize)
                Vector3f position = vertex.getVec3("passPositionCamera");

                // Calculate the normal
                Vector3f normal = vertex.getVec3("passNormalCamera").normalize();

                // Adjust the normal, if a normal map is specified
                if (normalMap != null) {
                    Vector3f tangent = vertex.getVec3("passTangentCamera").normalize();
                    Vector3f biTangent = normal.cross(tangent);
                    Matrix3f tbn = new Matrix3f(tangent, biTangent, normal);
                    normal = Colors.getVector(normalMap.getTexel(uv)).scale(2).subtract(Vector3f.ONE);
                    // Adjust the intensity of the normal map
                    if (normalMapStrength != 1) {
                        normal = new Vector3f(normal.getX() * normalMapStrength, normal.getY() * normalMapStrength, normal.getZ()).normalize();
                    }
                    normal = tbn.multiply(normal).normalize();
                }

                // Reflective term
                int reflectiveTerm = 0;
                if (environmentMap != null) {
                    Vector3f reflection = position.normalize().reflect(normal);
                    Vector3f reflectionWorld = new Vector3f(((Matrix4f) shaderUniforms.getUniform("viewModel")).inverse().multiply(reflection, 0));
                    if (reflectionMap != null) {
                        reflectiveTerm = Colors.multiply(environmentMap.getTexel(reflectionWorld), Colors.darken(reflectionMap.getTexel(uv), 1 - reflectionIntensity));
                    } else {
                        reflectiveTerm = Colors.darken(environmentMap.getTexel(reflectionWorld), 1 - reflectionIntensity);
                    }
                    reflectiveTerm = Colors.darken(reflectiveTerm, 1 - reflectionIntensity);
                }

                // Return the sum of the ambient and reflective terms, if no lights are present in the scene
                if (noLightsPresent) {
                    return Colors.add(ambientTerm, reflectiveTerm);
                }

                // Calculate the view direction
                Vector3f viewDirection = position.scale(-1).normalize();

                // Sample the specular color from the specular map if specified
                int specularColor;
                if (specularMap != null) {
                    specularColor = specularMap.getTexel(uv);
                } else {
                    specularColor = specularMaterialColor;
                }
                if (specularIntensity != 1) {
                    specularColor = Colors.darken(specularColor, 1 - specularIntensity);
                }

                // Calculate directional light terms
                int directionalLightTerms = 0;
                for (int i = 0; i < directionalLightDirections.length; i++) {
                    final int directionalLightTerm = calculateDirectionalLightTerm(
                            directionalLightColors[i], diffuseColor, specularColor,
                            directionalLightDirections[0], normal, viewDirection);
                    directionalLightTerms = Colors.add(directionalLightTerms, directionalLightTerm);
                }

                // Calculate point light terms
                int pointLightTerms = 0;
                for (int i = 0; i < pointLightPositions.length; i++) {
                    final int pointLightTerm = calculatePointLightTerm(
                            pointLightColors[i], diffuseColor, specularColor,
                            pointLightPositions[i], position, normal, viewDirection);
                    pointLightTerms = Colors.add(pointLightTerms, pointLightTerm);
                }

                // Calculate spotlight terms
                int spotlightTerms = 0;
                for (int i = 0; i < spotlightPositions.length; i++) {
                    final int spotlightTerm = calculateSpotlightTerm(
                            spotlightColors[i], diffuseColor, specularColor,
                            spotlightPositions[i], spotlightDirections[i], spotlights[i],
                            position, normal, viewDirection);
                    spotlightTerms = Colors.add(spotlightTerms, spotlightTerm);
                }

                // Return fragment color
                return Colors.setAlpha(Colors.add(ambientTerm, directionalLightTerms, pointLightTerms, spotlightTerms, reflectiveTerm), alpha);
            }

            private int calculateDirectionalLightTerm(
                    int lightColor, int diffuseColor, int specularColor,
                    Vector3f lightDirection, Vector3f normal, Vector3f viewDirection) {
                // Diffuse term
                final int diffuseTerm = Colors.darken(diffuseColor, 1 - Math.max(0, normal.dot(lightDirection.scale(-1))));

                // Specular term
                int specularTerm = 0;
                if ((diffuseTerm & 0xffffff) > 0) {
                    Vector3f lightReflection = lightDirection.reflect(normal);
                    final float specAngle = Math.max(viewDirection.dot(lightReflection), 0);
                    if (specularMap != null) {
                        specularTerm = Colors.darken(specularColor, 1 - (float) Math.pow(specAngle, shininess));
                    } else {
                        specularTerm = Colors.getGray((int) (255 * Math.pow(specAngle, shininess)));
                    }
                }

                return Colors.multiply(Colors.add(diffuseTerm, specularTerm), lightColor);
            }

            private int calculatePointLightTerm(
                    int lightColor, int diffuseColor, int specularColor,
                    Vector3f lightPosition, Vector3f fragmentPosition, Vector3f normal, Vector3f viewDirection) {
                // Calculate the vector pointing from the fragment to the light position
                Vector3f lightDirection = lightPosition.subtract(fragmentPosition).normalize();

                // Diffuse term
                final int diffuseTerm = Colors.darken(diffuseColor, 1 - Math.max(0, normal.dot(lightDirection)));

                // Specular term
                int specularTerm = 0;
                if ((diffuseTerm & 0xffffff) > 0) {
                    Vector3f lightReflection = lightDirection.scale(-1).reflect(normal);
                    final float specAngle = Math.max(viewDirection.dot(lightReflection), 0);
                    if (specularMap != null) {
                        specularTerm = Colors.darken(specularColor, 1 - (float) Math.pow(specAngle, shininess));
                    } else {
                        specularTerm = Colors.getGray((int) (255 * Math.pow(specAngle, shininess)));
                    }
                }

                return Colors.multiply(Colors.add(diffuseTerm, specularTerm), lightColor);
            }

            private int calculateSpotlightTerm(
                    int lightColor, int diffuseColor, int specularColor,
                    Vector3f lightPosition, Vector3f spotlightDirection, Spotlight spotlight,
                    Vector3f fragmentPosition, Vector3f normal, Vector3f viewDirection) {
                // Calculate the vector pointing from the fragment to the light position
                Vector3f lightDirection = lightPosition.subtract(fragmentPosition).normalize();

                // Diffuse term
                final int diffuseTerm = Colors.darken(diffuseColor, 1 - Math.max(0, normal.dot(lightDirection)));

                // Specular term
                int specularTerm = 0;
                if ((diffuseTerm & 0xffffff) > 0) {
                    Vector3f lightReflection = lightDirection.scale(-1).reflect(normal);
                    final float specAngle = Math.max(viewDirection.dot(lightReflection), 0);
                    if (specularMap != null) {
                        specularTerm = Colors.darken(specularColor, 1 - (float) Math.pow(specAngle, shininess));
                    } else {
                        specularTerm = Colors.getGray((int) (255 * Math.pow(specAngle, shininess)));
                    }
                }

                // Spotlight
                float spot;
                if (spotlight.getCutoff() < 0.001f) {
                    spot = 1;
                } else {
                    float cosPhiSpot = Math.max(0, lightDirection.scale(-1).dot(spotlightDirection));
                    if (cosPhiSpot >= spotlight.getCutoffCos()) {
                        spot = (float) Math.pow(cosPhiSpot, spotlight.getExponent());
                    } else {
                        spot = 0;
                    }
                }

                return Colors.darken(Colors.multiply(Colors.add(diffuseTerm, specularTerm), lightColor), 1 - spot);
            }
        };
    }

    @Override
    public ShaderUniforms getShaderUniforms(Camera camera, Matrix4f projection, Matrix4f view, Matrix4f
            projectionView, Transform transform) {
        ShaderUniforms shaderUniforms = new ShaderUniforms();
        Matrix4f viewModel = view.multiply(transform.getTransformMatrix());
        Matrix4f viewModelTransposeInverse = viewModel.transpose().inverse();

        shaderUniforms.setUniform("viewModel", viewModel);
        shaderUniforms.setUniform("viewModelTransposeInverse", viewModelTransposeInverse);
        shaderUniforms.setUniform("projectionViewModel", projection.multiply(viewModel));

        // Add directional lights
        List<Vector3f> directionalLightDirectionsCamera = new ArrayList<>();
        List<Integer> directionalLightColors = new ArrayList<>();

        getOwner().getScene().getEntitiesWithComponents(Transform.class, DirectionalLight.class).forEach(entity -> {
            DirectionalLight directionalLight = entity.getComponent(DirectionalLight.class);
            Vector3f directionalLightDirection = new Vector3f(view.multiply(directionalLight.getDirection(), 0));
            directionalLightDirectionsCamera.add(directionalLightDirection.normalize());
            directionalLightColors.add(Colors.getColor(directionalLight.getColor()));
        });

        shaderUniforms.setUniform("directionalLightDirectionsCamera", directionalLightDirectionsCamera.toArray(new Vector3f[0]));
        shaderUniforms.setUniform("directionalLightColors", directionalLightColors.toArray(new Integer[0]));

        // Add point lights
        List<Vector3f> pointLightPositionsCamera = new ArrayList<>();
        List<Integer> pointLightColors = new ArrayList<>();

        getOwner().getScene().getEntitiesWithComponents(Transform.class, PointLight.class).forEach(entity -> {
            Matrix4f mat = view.multiply(entity.getComponent(Transform.class).getTransformMatrix());
            Vector3f pointLightPositionCamera = new Vector3f(mat.multiply(Vector3f.ZERO));
            pointLightPositionsCamera.add(pointLightPositionCamera);
            pointLightColors.add(Colors.getColor(entity.getComponent(PointLight.class).getColor()));
        });

        shaderUniforms.setUniform("pointLightPositionsCamera", pointLightPositionsCamera.toArray(new Vector3f[0]));
        shaderUniforms.setUniform("pointLightColors", pointLightColors.toArray(new Integer[0]));

        // Add spotlights
        List<Vector3f> spotlightPositionsCamera = new ArrayList<>();
        List<Vector3f> spotlightDirectionsCamera = new ArrayList<>();
        List<Integer> spotlightColors = new ArrayList<>();
        List<Spotlight> spotlights = new ArrayList<>();

        getOwner().getScene().getEntitiesWithComponents(Transform.class, Spotlight.class).forEach(entity -> {
            Spotlight spotlight = entity.getComponent(Spotlight.class);
            Matrix4f mat = view.multiply(entity.getComponent(Transform.class).getTransformMatrix());
            Vector3f spotlightPositionCamera = new Vector3f(mat.multiply(Vector3f.ZERO));
            spotlightPositionsCamera.add(spotlightPositionCamera);
            Vector3f spotlightDirectionCamera = new Vector3f(view.multiply(spotlight.getDirection(), 0));
            spotlightDirectionsCamera.add(spotlightDirectionCamera.normalize());
            spotlightColors.add(Colors.getColor(spotlight.getColor()));
            spotlights.add(spotlight);
        });

        shaderUniforms.setUniform("spotlightPositionsCamera", spotlightPositionsCamera.toArray(new Vector3f[0]));
        shaderUniforms.setUniform("spotlightDirectionsCamera", spotlightDirectionsCamera.toArray(new Vector3f[0]));
        shaderUniforms.setUniform("spotlightColors", spotlightColors.toArray(new Integer[0]));
        shaderUniforms.setUniform("spotlights", spotlights.toArray(new Spotlight[0]));

        return shaderUniforms;
    }

    @Override
    public VertexAssembly getVertexAssembly() {
        return vertexAssembly;
    }

    @Override
    public VertexShader getVertexShader() {
        return vertexShader;
    }

    @Override
    public FragmentShader getFragmentShader() {
        return fragmentShader;
    }
}