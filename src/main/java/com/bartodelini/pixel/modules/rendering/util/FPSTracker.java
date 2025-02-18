package com.bartodelini.pixel.modules.rendering.util;

/**
 * An <i>FPSTracker</i> is used to update and retrieve the current fps.
 *
 * @author Bartolini
 * @version 1.0
 */
public class FPSTracker {

    private double timer = 0;
    private int fps = 0;
    private int ups = 0;

    /**
     * Update the fps based on the passed in delta time.
     *
     * @param deltaTime the delta time since the last update.
     */
    public void update(double deltaTime) {
        timer += deltaTime;
        if (timer > 1) {
            System.out.println(fps);
            fps = ups;
            timer = 0;
            ups = 0;
        }
        ups++;
    }

    /**
     * Returns the current fps.
     *
     * @return the current fps.
     */
    public int getFPS() {
        return fps;
    }
}