package com.bartodelini.pixel.modules.rendering.bitmap.primitive;

import com.bartodelini.pixel.math.MathUtils;
import com.bartodelini.pixel.math.vector.Vector2f;
import com.bartodelini.pixel.math.vector.Vector3f;
import com.bartodelini.pixel.math.vector.Vector4f;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * A <i>Vertex</i> is the main building block for {@linkplain Primitive Primitives}. Each {@code Vertex} contains a
 * {@code clip space position} and a {@code screen space position}, as well as any number of user-defined attributes
 * (such as {@code floats}, {@linkplain Vector2f}, {@linkplain Vector3f} and {@linkplain Vector4f}), which can be
 * interpolated between two {@code vertices}.
 *
 * @author Bartolini
 * @version 1.0
 */
public class Vertex {

    /**
     * A helper class used to store a value together with an {@linkplain Interpolation}, which indicates how it should
     * be interpolated.
     *
     * @param <T> the type stored by this {@code Value}.
     * @author Bartolini
     * @version 1.0
     */
    private static class Value<T> {

        private final Interpolation interpolationMode;
        private T value;

        /**
         * Allocates a new {@code Value} by passing in its {@linkplain Interpolation} and its {@code value}.
         *
         * @param interpolationMode the {@code Interpolation} indicating how the {@code value} should be interpolated.
         * @param value             the {@code value} to be stored.
         */
        public Value(Interpolation interpolationMode, T value) {
            this.interpolationMode = interpolationMode;
            this.value = value;
        }
    }

    private Vector4f clipSpacePosition;
    private Vector3f screenSpacePosition;

    private final Map<String, Value<Float>> floatMap = new HashMap<>();
    private final Map<String, Value<Vector2f>> vec2Map = new HashMap<>();
    private final Map<String, Value<Vector3f>> vec3Map = new HashMap<>();
    private final Map<String, Value<Vector4f>> vec4Map = new HashMap<>();

    /**
     * The reciprocal of the depth component for perspective correct (smooth) interpolation.
     */
    private float depthReciprocal = 1;

    /**
     * Allocates a new {@code Vertex} by passing in its {@code clip space position} as well as its
     * {@code screen space position}.
     *
     * @param clipSpacePosition   the {@code clip space position} of this {@code Vertex}.
     * @param screenSpacePosition ths {@code screen space position} of this {@code Vertex}.
     */
    public Vertex(Vector4f clipSpacePosition, Vector3f screenSpacePosition) {
        this.clipSpacePosition = clipSpacePosition;
        this.screenSpacePosition = screenSpacePosition;
    }

    /**
     * Returns the {@code clip space position} of this {@code Vertex}.
     *
     * @return the {@code clip space position} of this {@code Vertex}.
     */
    public Vector4f getClipSpacePosition() {
        return clipSpacePosition;
    }

    /**
     * Sets the {@code clip space position} of this {@code Vertex}.
     *
     * @param clipSpacePosition the {@code clip space position} of this {@code Vertex}.
     */
    public void setClipSpacePosition(Vector4f clipSpacePosition) {
        this.clipSpacePosition = clipSpacePosition;
    }

    /**
     * Returns the {@code screen space position} of this {@code Vertex}.
     *
     * @return the {@code screen space position} of this {@code Vertex}.
     */
    public Vector3f getScreenSpacePosition() {
        return screenSpacePosition;
    }

    /**
     * Sets the {@code screen space position} of this {@code Vertex}.
     * <p>
     * Note: This method should only be called when the {@code clip space position} of this {@code Vertex} has been
     * set, as it will premultiply each {@code Vertex} attribute of this {@code Vertex}, which has its
     * {@linkplain Interpolation} set to {@linkplain Interpolation#SMOOTH}, with the reciprocal of the depth value
     * found in the {@code clip space position}. Furthermore, this method returns if the {@code screen space position}
     * of this {@code Vertex} has already been set.
     *
     * @param screenSpacePosition the {@code screen space position} of this {@code Vertex}.
     * @throws IllegalStateException if the {@code clip space position} of this {@code Vertex} has not been set yet.
     * @throws NullPointerException  if the specified {@code screenSpacePosition} is {@code null}.
     */
    public synchronized void setScreenSpacePosition(Vector3f screenSpacePosition) {
        if (this.clipSpacePosition == null) {
            throw new IllegalStateException("the clip space position has not been set yet");
        }
        Objects.requireNonNull(screenSpacePosition, "screenSpacePosition must not be null");
        if (this.screenSpacePosition != null) {
            return;
        }
        this.screenSpacePosition = screenSpacePosition;
        depthReciprocal = 1 / getClipSpacePosition().getW();
        multiplyValuesWithReciprocal();
    }

    /**
     * A helper method used to premultiply each {@code Vertex} attribute, stored in this {@code Vertex} and marked as
     * {@linkplain Interpolation#SMOOTH}, with the {@linkplain Vertex#depthReciprocal}.
     */
    private void multiplyValuesWithReciprocal() {
        floatMap.values()
                .stream()
                .filter(floatValue -> floatValue.interpolationMode.equals(Interpolation.SMOOTH))
                .forEach(floatValue -> floatValue.value = floatValue.value * depthReciprocal);
        vec2Map.values()
                .stream()
                .filter(vector2fValue -> vector2fValue.interpolationMode.equals(Interpolation.SMOOTH))
                .forEach(vector2fValue -> vector2fValue.value = vector2fValue.value.scale(depthReciprocal));
        vec3Map.values()
                .stream()
                .filter(vector3fValue -> vector3fValue.interpolationMode.equals(Interpolation.SMOOTH))
                .forEach(vector3fValue -> vector3fValue.value = vector3fValue.value.scale(depthReciprocal));
        vec4Map.values()
                .stream()
                .filter(vector4fValue -> vector4fValue.interpolationMode.equals(Interpolation.SMOOTH))
                .forEach(vector4fValue -> vector4fValue.value = vector4fValue.value.scale(depthReciprocal));
    }

    /**
     * Sets a {@code float} of the specified {@linkplain Interpolation}, {@code name} and value.
     *
     * @param interpolationMode the {@code interpolationMode} of the specified {@code float}.
     * @param name              the name of the specified {@code float}.
     * @param f                 the value of the specified {@code float}.
     * @throws NullPointerException if the specified {@code interpolationMode} or {@code name} are {@code null}.
     */
    public void setFloat(Interpolation interpolationMode, String name, float f) {
        Objects.requireNonNull(interpolationMode, "interpolationMode must not be null");
        Objects.requireNonNull(name, "name must not be null");
        floatMap.put(name, new Value<>(interpolationMode, f));
    }

    /**
     * Sets a {@code float} of the specified {@code name} and value, with the {@linkplain Interpolation} set to
     * {@linkplain Interpolation#SMOOTH}.
     *
     * @param name the name of the specified {@code float}.
     * @param f    the value of the specified {@code float}.
     * @throws NullPointerException if the specified {@code name} is {@code null}.
     */
    public void setFloat(String name, float f) {
        setFloat(Interpolation.SMOOTH, name, f);
    }

    /**
     * Returns the {@code float} associated with the specified {@code name}.
     *
     * @param name the name of the {@code float} to be returned.
     * @return the {@code float} associated with the specified {@code name}.
     * @throws NullPointerException if the specified {@code name} is {@code null}.
     */
    public float getFloat(String name) {
        Objects.requireNonNull(name, "name must not be null");
        Value<Float> floatValue = floatMap.get(name);
        if (screenSpacePosition != null && floatValue.interpolationMode.equals(Interpolation.SMOOTH)) {
            return floatValue.value * (1 / depthReciprocal);
        }
        return floatValue.value;
    }

    /**
     * Updates the {@code float} associated with the specified {@code name} using the specified {@linkplain Function}.
     *
     * @param name     the name associated with the {@code float} to be updated.
     * @param function the {@code Function} used to update the value.
     * @throws NullPointerException if the specified {@code name} or {@code function} are {@code null}.
     */
    public void updateFloat(String name, Function<Float, Float> function) {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(function, "function must not be null");
        floatMap.get(name).value = function.apply(floatMap.get(name).value);
    }

    /**
     * Sets a {@linkplain Vector2f} of the specified {@linkplain Interpolation}, {@code name} and value.
     *
     * @param interpolationMode the {@code interpolationMode} of the specified {@code Vector2f}.
     * @param name              the name of the specified {@code Vector2f}.
     * @param vec               the value of the specified {@code Vector2f}.
     * @throws NullPointerException if any of the specified parameters is {@code null}.
     */
    public void setVec2(Interpolation interpolationMode, String name, Vector2f vec) {
        Objects.requireNonNull(interpolationMode, "interpolationMode must not be null");
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(vec, "vec must not be null");
        vec2Map.put(name, new Value<>(interpolationMode, vec));
    }

    /**
     * Sets a {@linkplain Vector2f} of the specified {@code name} and value, with the {@linkplain Interpolation} set to
     * {@linkplain Interpolation#SMOOTH}.
     *
     * @param name the name of the specified {@code Vector2f}.
     * @param vec  the value of the specified {@code Vector2f}.
     * @throws NullPointerException if the specified {@code name} or {@code Vector2f} are {@code null}.
     */
    public void setVec2(String name, Vector2f vec) {
        setVec2(Interpolation.SMOOTH, name, vec);
    }

    /**
     * Returns the {@linkplain Vector2f} associated with the specified {@code name}.
     *
     * @param name the name of the {@code Vector2f} to be returned.
     * @return the {@code Vector2f} associated with the specified {@code name}.
     * @throws NullPointerException if the specified {@code name} is {@code null}.
     */
    public Vector2f getVec2(String name) {
        Objects.requireNonNull(name, "name must not be null");
        Value<Vector2f> vector2fValue = vec2Map.get(name);
        if (screenSpacePosition != null && vector2fValue.interpolationMode.equals(Interpolation.SMOOTH)) {
            return vector2fValue.value.scale(1 / depthReciprocal);
        }
        return vector2fValue.value;
    }

    /**
     * Updates the {@linkplain Vector2f} associated with the specified {@code name} using the specified
     * {@linkplain Function}.
     *
     * @param name     the name associated with the {@code Vector2f} to be updated.
     * @param function the {@code Function} used to update the value.
     * @throws NullPointerException if the specified {@code name} or {@code function} are {@code null}.
     */
    public void updateVec2(String name, Function<Vector2f, Vector2f> function) {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(function, "function must not be null");
        vec2Map.get(name).value = function.apply(vec2Map.get(name).value);
    }

    /**
     * Sets a {@linkplain Vector3f} of the specified {@linkplain Interpolation}, {@code name} and value.
     *
     * @param interpolationMode the {@code interpolationMode} of the specified {@code Vector3f}.
     * @param name              the name of the specified {@code Vector3f}.
     * @param vec               the value of the specified {@code Vector3f}.
     * @throws NullPointerException if any of the specified parameters is {@code null}.
     */
    public void setVec3(Interpolation interpolationMode, String name, Vector3f vec) {
        Objects.requireNonNull(interpolationMode, "interpolationMode must not be null");
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(vec, "vec must not be null");
        vec3Map.put(name, new Value<>(interpolationMode, vec));
    }

    /**
     * Sets a {@linkplain Vector3f} of the specified {@code name} and value, with the {@linkplain Interpolation} set to
     * {@linkplain Interpolation#SMOOTH}.
     *
     * @param name the name of the specified {@code Vector3f}.
     * @param vec  the value of the specified {@code Vector3f}.
     * @throws NullPointerException if the specified {@code name} or {@code Vector3f} are {@code null}.
     */
    public void setVec3(String name, Vector3f vec) {
        setVec3(Interpolation.SMOOTH, name, vec);
    }

    /**
     * Returns the {@linkplain Vector3f} associated with the specified {@code name}.
     *
     * @param name the name of the {@code Vector3f} to be returned.
     * @return the {@code Vector3f} associated with the specified {@code name}.
     * @throws NullPointerException if the specified {@code name} is {@code null}.
     */
    public Vector3f getVec3(String name) {
        Objects.requireNonNull(name, "name must not be null");
        Value<Vector3f> vector3fValue = vec3Map.get(name);
        if (screenSpacePosition != null && vector3fValue.interpolationMode.equals(Interpolation.SMOOTH)) {
            return vector3fValue.value.scale(1 / depthReciprocal);
        }
        return vector3fValue.value;
    }

    /**
     * Updates the {@linkplain Vector3f} associated with the specified {@code name} using the specified
     * {@linkplain Function}.
     *
     * @param name     the name associated with the {@code Vector3f} to be updated.
     * @param function the {@code Function} used to update the value.
     * @throws NullPointerException if the specified {@code name} or {@code function} are {@code null}.
     */
    public void updateVec3(String name, Function<Vector3f, Vector3f> function) {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(function, "function must not be null");
        vec3Map.get(name).value = function.apply(vec3Map.get(name).value);
    }

    /**
     * Sets a {@linkplain Vector4f} of the specified {@linkplain Interpolation}, {@code name} and value.
     *
     * @param interpolationMode the {@code interpolationMode} of the specified {@code Vector4f}.
     * @param name              the name of the specified {@code Vector4f}.
     * @param vec               the value of the specified {@code Vector4f}.
     * @throws NullPointerException if any of the specified parameters is {@code null}.
     */
    public void setVec4(Interpolation interpolationMode, String name, Vector4f vec) {
        Objects.requireNonNull(interpolationMode, "interpolationMode must not be null");
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(vec, "vec must not be null");
        vec4Map.put(name, new Value<>(interpolationMode, vec));
    }

    /**
     * Sets a {@linkplain Vector4f} of the specified {@code name} and value, with the {@linkplain Interpolation} set to
     * {@linkplain Interpolation#SMOOTH}.
     *
     * @param name the name of the specified {@code Vector4f}.
     * @param vec  the value of the specified {@code Vector4f}.
     * @throws NullPointerException if the specified {@code name} or {@code Vector4f} are {@code null}.
     */
    public void setVec4(String name, Vector4f vec) {
        setVec4(Interpolation.SMOOTH, name, vec);
    }

    /**
     * Returns the {@linkplain Vector4f} associated with the specified {@code name}.
     *
     * @param name the name of the {@code Vector4f} to be returned.
     * @return the {@code Vector4f} associated with the specified {@code name}.
     * @throws NullPointerException if the specified {@code name} is {@code null}.
     */
    public Vector4f getVec4(String name) {
        Objects.requireNonNull(name, "name must not be null");
        Value<Vector4f> vector4fValue = vec4Map.get(name);
        if (screenSpacePosition != null && vector4fValue.interpolationMode.equals(Interpolation.SMOOTH)) {
            return vector4fValue.value.scale(1 / depthReciprocal);
        }
        return vector4fValue.value;
    }

    /**
     * Updates the {@linkplain Vector4f} associated with the specified {@code name} using the specified
     * {@linkplain Function}.
     *
     * @param name     the name associated with the {@code Vector4f} to be updated.
     * @param function the {@code Function} used to update the value.
     * @throws NullPointerException if the specified {@code name} or {@code function} are {@code null}.
     */
    public void updateVec4(String name, Function<Vector4f, Vector4f> function) {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(function, "function must not be null");
        vec4Map.get(name).value = function.apply(vec4Map.get(name).value);
    }

    /**
     * Returns the result of linear interpolation between this {@code Vertex} and the specified {@code Vertex} by the
     * specified {@code alpha}.
     *
     * @param target the target {@code Vertex} to use for the interpolation.
     * @param alpha  the factor to used for the interpolation.
     * @return the result of linear interpolation between this {@code Vertex} and the specified target {@code Vertex}.
     * @throws NullPointerException if the specified {@code Vertex} is {@code null}.
     */
    public Vertex lerp(Vertex target, float alpha) {
        Objects.requireNonNull(target, "target must not be null");

        // Interpolate the clip space position
        Vector4f clipSpacePositionLerped = null;
        if (clipSpacePosition != null) {
            clipSpacePositionLerped = getClipSpacePosition().lerp(target.getClipSpacePosition(), alpha);
        }

        // Interpolate the screen space position
        float depthReciprocalLerped = 1;
        Vector3f screenSpacePositionLerped = null;
        if (screenSpacePosition != null) {
            screenSpacePositionLerped = getScreenSpacePosition().lerp(target.getScreenSpacePosition(), alpha);
            depthReciprocalLerped = MathUtils.lerpFloat(depthReciprocal, target.depthReciprocal, alpha);
        }

        // Create the resulting vertex
        Vertex result = new Vertex(clipSpacePositionLerped, screenSpacePositionLerped);
        result.depthReciprocal = depthReciprocalLerped;

        // Fill the resulting vertex with interpolated values
        floatMap.forEach((s, floatValue) -> {
            if (floatValue.interpolationMode.equals(Interpolation.FLAT)) {
                result.setFloat(floatValue.interpolationMode, s, floatValue.value);
            } else {
                result.setFloat(floatValue.interpolationMode, s,
                        MathUtils.lerpFloat(floatValue.value, target.floatMap.get(s).value, alpha));
            }
        });
        vec2Map.forEach((s, vector2fValue) -> {
            if (vector2fValue.interpolationMode.equals(Interpolation.FLAT)) {
                result.setVec2(vector2fValue.interpolationMode, s, vector2fValue.value);
            } else {
                result.setVec2(vector2fValue.interpolationMode, s,
                        vector2fValue.value.lerp(target.vec2Map.get(s).value, alpha));
            }
        });
        vec3Map.forEach((s, vector3fValue) -> {
            if (vector3fValue.interpolationMode.equals(Interpolation.FLAT)) {
                result.setVec3(vector3fValue.interpolationMode, s, vector3fValue.value);
            } else {
                result.setVec3(vector3fValue.interpolationMode, s,
                        vector3fValue.value.lerp(target.vec3Map.get(s).value, alpha));
            }
        });
        vec4Map.forEach((s, vector4fValue) -> {
            if (vector4fValue.interpolationMode.equals(Interpolation.FLAT)) {
                result.setVec4(vector4fValue.interpolationMode, s, vector4fValue.value);
            } else {
                result.setVec4(vector4fValue.interpolationMode, s,
                        vector4fValue.value.lerp(target.vec4Map.get(s).value, alpha));
            }
        });

        return result;
    }
}