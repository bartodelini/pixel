package com.bartodelini.pixel.logging;

/**
 * A <i>LogLevel</i> specifies the severity of a {@linkplain LogRecord}. Each {@code LogLevel} has an associated
 * priority number by which it can be ordered.
 *
 * @author Bartolini
 * @version 1.1
 */
public enum LogLevel {

    /**
     * The {@code LogLevel} with the highest priority. It should be used to turn off logging.
     */
    OFF(Integer.MAX_VALUE),
    /**
     * The FATAL {@code LogLevel} should indicate a serious error which mostly renders the application unrecoverable.
     */
    FATAL(1000),
    /**
     * The ERROR {@code LogLevel} should indicate an error which the application can possibly recover from.
     */
    ERROR(800),
    /**
     * The WARNING {@code LogLevel} should be used for warning messages.
     */
    WARNING(600),
    /**
     * The INFO {@code LogLevel} should be used for informative messages.
     */
    INFO(400),
    /**
     * The FINE {@code LogLevel} should indicate successful program execution.
     */
    FINE(200),
    /**
     * The DEBUG {@code LogLevel} should be used for frequent debug messages.
     */
    DEBUG(0),
    /**
     * The {@code LogLevel} with the lowest priority. It should be used to log every message.
     */
    ALL(Integer.MIN_VALUE);

    private final int priority;

    /**
     * Allocates a new {@code LogLevel} by specifying its priority.
     *
     * @param priority the priority of the {@code LogLevel}.
     */
    LogLevel(int priority) {
        this.priority = priority;
    }

    /**
     * Returns the priority of this {@code LogLevel}.
     *
     * @return the priority of this {@code LogLevel}.
     */
    public int getPriority() {
        return priority;
    }
}