package com.bartodelini.pixel.modules.time;

import com.bartodelini.pixel.core.EngineModule;

/**
 * A <i>Time</i> is an {@linkplain EngineModule} providing useful methods concerned with time.
 *
 * @author Bartolini
 * @version 1.0
 */
public class Time extends EngineModule {

    private static double time;
    private static int frameCount;

    /**
     * Allocates a new {@code Time}.
     */
    public Time() {
        super("Time", "time");
    }

    /**
     * Returns the time elapsed since engine start in seconds.
     *
     * @return the time elapsed since engine start in seconds.
     */
    public static double getTime() {
        return time;
    }

    /**
     * Returns the number of frames rendered since engine start.
     *
     * @return the number of frames rendered since engine start.
     */
    public static int getFrameCount() {
        return frameCount;
    }

    @Override
    public void update(double deltaTime) {
        time += deltaTime;
        frameCount++;
    }
}