package com.bartodelini.pixel.logging;

import java.time.Instant;
import java.util.Objects;

/**
 * A <i>LogRecord</i> is used to hold information about a log message, such as the time it occurred or its origin class.
 *
 * @author Bartolini
 * @version 1.2
 */
public class LogRecord {

    private final LogLevel level;
    private final Class<?> source;
    private final String msg;
    private final Throwable thrown;
    private final Instant instant;

    /**
     * Allocates a new {@code LogRecord} object by passing in its {@linkplain LogLevel}, origin {@linkplain Class},
     * message as well as the {@linkplain Throwable} associated with it. On creation, a {@linkplain Instant} is saved by
     * calling {@linkplain Instant#now()}
     *
     * @param level  the {@code LogLevel} of this {@code LogRecord}.
     * @param source the source {@code Class} for this {@code LogRecord}.
     * @param msg    the message of this {@code LogRecord}.
     * @param thrown the {@code Throwable} associated with this {@code LogRecord}.
     * @throws NullPointerException if the specified {@code LogLevel}, source {@code Class} or message are {@code null}.
     */
    public LogRecord(LogLevel level, Class<?> source, String msg, Throwable thrown) {
        this.level = Objects.requireNonNull(level, "level must not be null");
        this.source = Objects.requireNonNull(source, "source must not be null");
        this.msg = Objects.requireNonNull(msg, "msg must not be null");
        this.thrown = thrown;
        this.instant = Instant.now();
    }

    /**
     * Allocates a new {@code LogRecord} object by passing in its {@linkplain LogLevel}, origin {@linkplain Class} and
     * message. On creation, a {@linkplain Instant} is saved by calling {@linkplain Instant#now()}. This constructor
     * has the same effect as {@linkplain #LogRecord(LogLevel, Class, String, Throwable) LogRecord}
     * {@code (level, source, msg, null)}.
     *
     * @param level  the {@code LogLevel} of this {@code LogRecord}.
     * @param source the source {@code Class} for this {@code LogRecord}.
     * @param msg    the message of this {@code LogRecord}.
     * @throws NullPointerException if the specified {@code LogLevel}, source {@code Class} or message are {@code null}.
     */
    public LogRecord(LogLevel level, Class<?> source, String msg) {
        this(level, source, msg, null);
    }

    /**
     * Returns the {@linkplain LogLevel} of this {@code LogRecord}.
     *
     * @return the {@code LogLevel} of this {@code LogRecord}.
     */
    public LogLevel getLevel() {
        return level;
    }

    /**
     * Returns the source {@linkplain Class} of this {@code LogRecord}.
     *
     * @return the source {@code Class} of this {@code LogRecord}.
     */
    public Class<?> getSource() {
        return source;
    }

    /**
     * Returns the message of this {@code LogRecord}.
     *
     * @return the message of this {@code LogRecord}.
     */
    public String getMessage() {
        return msg;
    }

    /**
     * Returns the {@linkplain Throwable} associated with this {@code LogRecord}.
     *
     * @return the {@code Throwable} associated with this {@code LogRecord}.
     */
    public Throwable getThrown() {
        return thrown;
    }

    /**
     * The {@linkplain Instant} this {@code LogRecord} was created.
     *
     * @return the {@code Instant} this {@code LogRecord} was created.
     */
    public Instant getInstant() {
        return instant;
    }
}