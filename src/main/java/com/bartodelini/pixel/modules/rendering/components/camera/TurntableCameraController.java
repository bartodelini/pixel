package com.bartodelini.pixel.modules.rendering.components.camera;

import com.bartodelini.pixel.math.vector.Vector2f;
import com.bartodelini.pixel.math.vector.Vector3f;
import com.bartodelini.pixel.modules.input.Input;
import com.bartodelini.pixel.modules.input.Key;
import com.bartodelini.pixel.modules.input.MouseButton;
import com.bartodelini.pixel.modules.rendering.components.model.Transform;
import com.bartodelini.pixel.modules.scripting.ScriptableComponent;

/**
 * A <i>TurntableCameraController</i> is a {@linkplain ScriptableComponent} used to control the orientation of a
 * {@linkplain Camera} in a turntable-like manner.
 *
 * @author Bartolini
 * @version 1.0
 */
public class TurntableCameraController extends ScriptableComponent {

    private final float verticalSensitivity;
    private final float horizontalSensitivity;
    private final float zoomSpeed;
    private final Transform upDownPivot;
    private final Transform leftRightPivot;

    private Transform cameraTransform;
    private Vector2f lastMousePosition = Vector2f.ZERO;

    /**
     * Allocates a new {@code TurntableCameraController} by passing in the vertical and horizontal sensitivity, as well
     * as the zoom speed.
     *
     * @param verticalSensitivity   the vertical mouse sensitivity.
     * @param horizontalSensitivity the horizontal mouse sensitivity.
     * @param zoomSpeed             the zoom speed.
     */
    public TurntableCameraController(float verticalSensitivity, float horizontalSensitivity, float zoomSpeed) {
        this.verticalSensitivity = verticalSensitivity;
        this.horizontalSensitivity = horizontalSensitivity;
        this.zoomSpeed = zoomSpeed;
        this.upDownPivot = new Transform();
        this.leftRightPivot = new Transform();
    }

    /**
     * Allocates a new {@code TurntableCameraController} with the default values for the vertical and horizontal
     * sensitivity, as well as the zoom speed. This constructor has the same effect as
     * {@linkplain #TurntableCameraController(float, float, float) TurntableCameraController}
     * {@code (0.0077f, 0.0077f, 0.07f)}.
     */
    public TurntableCameraController() {
        this(0.0077f, 0.0077f, 0.07f);
    }

    @Override
    public void start() {
        cameraTransform = getOwner().getComponent(Transform.class);
        if (cameraTransform == null) {
            throw new IllegalStateException("the camera has no Transform component attached");
        }
        cameraTransform.setParent(upDownPivot);
        upDownPivot.setParent(leftRightPivot);
    }

    @Override
    public void update(double deltaTime) {
        // Retrieve the current mouse position
        Vector2f currentMousePosition = Input.getRelativeMousePosition();

        // Update the camera zoom
        if (Input.isMouseButtonUp(MouseButton.MIDDLE)) {
            float mouseScroll = (float) Input.getMouseWheelPreciseRotation();
            cameraTransform.translate(
                    Vector3f.FRONT.scale(mouseScroll * zoomSpeed * cameraTransform.getPosition().getZ()));
        }

        // Update the camera rotation
        if (Input.isMouseButtonDown(MouseButton.MIDDLE)) {
            // Calculate the change in mouse position
            Vector2f mousePositionChange = currentMousePosition.subtract(lastMousePosition);

            if (!mousePositionChange.equals(Vector2f.ZERO)) {
                if (Input.isKeyDown(Key.SHIFT)) {
                    // Move the camera
                    Vector3f right = new Vector3f(cameraTransform.getTransformMatrix().multiply(Vector3f.RIGHT, 0)).normalize();
                    Vector3f up = new Vector3f(cameraTransform.getTransformMatrix().multiply(Vector3f.UP, 0)).normalize();
                    leftRightPivot.translate(right.scale(-horizontalSensitivity * mousePositionChange.getX() * cameraTransform.getPosition().getZ() / 4));
                    leftRightPivot.translate(up.scale(verticalSensitivity * mousePositionChange.getY() * cameraTransform.getPosition().getZ() / 4));
                } else {
                    // Rotate the camera
                    upDownPivot.rotate(Vector3f.RIGHT.scale(-horizontalSensitivity * mousePositionChange.getY()));
                    leftRightPivot.rotate(Vector3f.UP.scale(-verticalSensitivity * mousePositionChange.getX()));
                }
            }
        }

        if (Input.isKeyDown(Key.SPACE)) {
            leftRightPivot.rotate(Vector3f.UP.scale((float) (deltaTime * 1)));
        }

        // Reset the camera position
        if (Input.isKeyPressed(Key.NUMPAD_DECIMAL)) {
            leftRightPivot.setPosition(Vector3f.ZERO);
        }

        // Update the last mouse position
        lastMousePosition = currentMousePosition;
    }
}