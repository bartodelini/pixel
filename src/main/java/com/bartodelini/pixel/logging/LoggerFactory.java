package com.bartodelini.pixel.logging;

import java.util.*;

/**
 * A <i>LoggerFactory</i> is used to create instances of {@linkplain Logger Loggers} by specifying a
 * {@linkplain Class}. It is also the default {@linkplain LogRecordHandler} which is added to every {@code Logger}
 * created by it. Additional {@code LogRecordHandlers} can be added to the {@code LoggerFactory} if they want to be
 * notified about every {@linkplain LogRecord} from all {@code Loggers} created by the {@code LoggerFactory}. By
 * default, the {@linkplain LogLevel} is set to {@linkplain LogLevel#ALL}.
 *
 * @author Bartolini
 * @version 1.1
 */
public class LoggerFactory implements LogRecordHandler {

    private static final LoggerFactory instance = new LoggerFactory();
    private static final Map<Class<?>, Logger> logMap = new HashMap<>();
    private static final Set<LogRecordHandler> logRecordHandlerSet = new HashSet<>();
    private static final List<LogRecord> logRecordList = new LinkedList<>();

    private static LogLevel logLevel = LogLevel.ALL;

    /**
     * Private constructor to prevent further instances of this class.
     */
    private LoggerFactory() {
    }

    /**
     * Returns the {@linkplain Logger} associated with the specified {@linkplain Class}. Creates a new {@code Logger}
     * for the specified {@code Class} if none exists; otherwise returns the associated {@code Logger}.
     *
     * @param source the {@code Class} to create the {@code Logger} for.
     * @return the {@code Logger} associated with the specified {@code Class}.
     * @throws NullPointerException if the specified {@code Class} is {@code null}.
     */
    public static Logger getLogger(Class<?> source) {
        Objects.requireNonNull(source, "source must not be null");
        Logger logger = logMap.computeIfAbsent(source, Logger::new);
        logger.addLogRecordHandler(instance);
        return logger;
    }

    /**
     * Returns the {@linkplain Logger} associated with the {@linkplain Class} of the specified {@linkplain Object}. This
     * method has the same effect as {@linkplain #getLogger(Class) getLogger} {@code (source.getClass())}.
     *
     * @param source the {@code Object} to create the {@code Logger} for.
     * @return the {@code Logger} associated with the {@code Class} of the specified {@code Object}.
     * @throws NullPointerException if the specified {@code Object} is {@code null}.
     */
    public static Logger getLogger(Object source) {
        Objects.requireNonNull(source, "source must not be null");
        return getLogger(source.getClass());
    }

    /**
     * Adds the passed in {@linkplain LogRecordHandler} to the {@linkplain Set} of handlers of this
     * {@code LoggerFactory}.
     *
     * @param handler the {@code LogRecordHandler} to be added.
     * @throws NullPointerException if the specified {@code LogRecordHandler} is {@code null}.
     */
    public static void addLogRecordHandler(LogRecordHandler handler) {
        Objects.requireNonNull(handler, "handler must not be null");
        logRecordHandlerSet.add(handler);
    }

    /**
     * Removes the passed in {@linkplain LogRecordHandler} from the {@linkplain Set} of handlers of this
     * {@code LoggerFactory}.
     *
     * @param handler the {@code LogRecordHandler} to be removed.
     * @throws NullPointerException if the specified {@code LogRecordHandler} is {@code null}.
     */
    public static void removeLogRecordHandler(LogRecordHandler handler) {
        Objects.requireNonNull(handler, "handler must not be null");
        logRecordHandlerSet.remove(handler);
    }

    /**
     * Returns an unmodifiable {@linkplain List} of all {@linkplain LogRecord LogRecords} of this {@code LoggerFactory}.
     *
     * @return an unmodifiable {@code List} of all {@code LogRecords} of this {@code LoggerFactory}.
     */
    public static List<LogRecord> getLogList() {
        return Collections.unmodifiableList(logRecordList);
    }

    /**
     * Sets the {@linkplain LogLevel} of this {@code LoggerFactory}. Any {@linkplain LogRecord} below this
     * {@code LogLevel} will not be logged into this {@code LoggerFactory}.
     *
     * @param level the {@code LogLevel} to set this {@code LoggerFactory} to.
     * @throws NullPointerException if the specified {@code LogLevel} is {@code null}.
     */
    public static void setLogLevel(LogLevel level) {
        Objects.requireNonNull(level, "level must not be null");
        logLevel = level;
    }

    /**
     * Returns the {@linkplain LogLevel} of this {@code LoggerFactory}.
     *
     * @return the {@code LogLevel} of this {@code LoggerFactory}.
     */
    public static LogLevel getLogLevel() {
        return logLevel;
    }

    @Override
    public void handle(LogRecord record) {
        Objects.requireNonNull(record, "record must not be null");
        if (logLevel.getPriority() <= record.getLevel().getPriority()) {
            logRecordList.add(record);
            logRecordHandlerSet.forEach(h -> h.handle(record));
        }
    }
}