package com.bartodelini.pixel.logging;

import java.util.*;

/**
 * A <i>Logger</i> is used to create and store {@linkplain LogRecord LogRecords}. By default, the {@linkplain LogLevel}
 * is set to {@linkplain LogLevel#ALL}.
 *
 * @author Bartolini
 * @version 1.2
 */
public class Logger {

    private final List<LogRecord> logRecordList = new ArrayList<>();
    private final Set<LogRecordHandler> logRecordHandlerSet = new HashSet<>();
    private final Class<?> source;

    private LogLevel logLevel = LogLevel.ALL;

    /**
     * Allocates a new {@code Logger} object by passing in its source {@linkplain Class}. By default, the
     * {@linkplain LogLevel} is set to {@linkplain LogLevel#ALL}.
     *
     * @param source the source {@code Class} of this {@code Logger}.
     * @throws NullPointerException if the specified {@code Class} is {@code null}.
     */
    public Logger(Class<?> source) {
        this.source = Objects.requireNonNull(source, "source must not be null");
    }

    /**
     * Logs a message by creating a new {@linkplain LogRecord} from the passed in {@linkplain LogLevel}, source
     * {@linkplain Class}, message as well as the {@linkplain Throwable} associated with it.
     *
     * @param level     the {@code LogLevel} of the message.
     * @param source    the source {@code Class} of the message.
     * @param msg       the message.
     * @param throwable the {@code Throwable} associated with the message.
     * @throws NullPointerException if the specified {@code LogLevel}, source {@code Class} or message are {@code null}.
     */
    public void log(LogLevel level, Class<?> source, String msg, Throwable throwable) {
        addLog(new LogRecord(level, source, msg, throwable));
    }

    /**
     * Logs a message by creating a new {@linkplain LogRecord} from the passed in {@linkplain LogLevel}, source
     * {@linkplain Class} and message. This method has the same effect as
     * {@linkplain #log(LogLevel, Class, String, Throwable) log} {@code (level, source, msg, null)}.
     *
     * @param level  the {@code LogLevel} of the message.
     * @param source the source {@code Class} of the message.
     * @param msg    the message.
     * @throws NullPointerException if the specified {@code LogLevel}, source {@code Class} or message are {@code null}.
     */
    public void log(LogLevel level, Class<?> source, String msg) {
        log(level, source, msg, null);
    }

    /**
     * Logs a message by creating a new {@linkplain LogRecord} from the passed in {@linkplain LogLevel}, message
     * as well as the {@linkplain Throwable} associated with it. This method has the same effect as
     * {@linkplain #log(LogLevel, Class, String, Throwable) log} {@code (level, this.source, msg, throwable)}.
     *
     * @param level     the {@code LogLevel} of the message.
     * @param msg       the message.
     * @param throwable the {@code Throwable} associated with the message.
     * @throws NullPointerException if the specified {@code LogLevel} or message are {@code null}.
     */
    public void log(LogLevel level, String msg, Throwable throwable) {
        log(level, this.source, msg, throwable);
    }

    /**
     * Logs a message by creating a new {@linkplain LogRecord} from the passed in {@linkplain LogLevel} and
     * message. This method has the same effect as {@linkplain #log(LogLevel, Class, String, Throwable) log}
     * {@code (level, this.source, msg, null)}.
     *
     * @param level the {@code LogLevel} of the message.
     * @param msg   the message.
     * @throws NullPointerException if the specified {@code LogLevel} or message are {@code null}.
     */
    public void log(LogLevel level, String msg) {
        log(level, this.source, msg, null);
    }

    /**
     * Logs a message by creating a new {@linkplain LogRecord} of type {@linkplain LogLevel#DEBUG} from the passed in
     * source {@linkplain Class}, message as well as the {@linkplain Throwable} associated with it. This method has the
     * same effect as {@linkplain #log(LogLevel, Class, String, Throwable) log}
     * {@code (LogLevel.DEBUG, source, msg, throwable)}.
     *
     * @param source    the source {@code Class} of the message.
     * @param msg       the message.
     * @param throwable the {@code Throwable} associated with the message.
     * @throws NullPointerException if the specified source {@code Class} or message are {@code null}.
     */
    public void debug(Class<?> source, String msg, Throwable throwable) {
        log(LogLevel.DEBUG, source, msg, throwable);
    }

    /**
     * Logs a message by creating a new {@linkplain LogRecord} of type {@linkplain LogLevel#DEBUG} from the passed in
     * source {@linkplain Class} and message. This method has the same effect as
     * {@linkplain #debug(Class, String, Throwable) debug} {@code (source, msg, null)}.
     *
     * @param source the source {@code Class} of the message.
     * @param msg    the message.
     * @throws NullPointerException if the specified source {@code Class} or message are {@code null}.
     */
    public void debug(Class<?> source, String msg) {
        debug(source, msg, null);
    }

    /**
     * Logs a message by creating a new {@linkplain LogRecord} of type {@linkplain LogLevel#DEBUG} from the passed in
     * message as well as the {@linkplain Throwable} associated with it. This method has the same effect as
     * {@linkplain #debug(Class, String, Throwable) debug} {@code (this.source, msg, throwable)}.
     *
     * @param msg       the message.
     * @param throwable the {@code Throwable} associated with the message.
     * @throws NullPointerException if the specified message is {@code null}.
     */
    public void debug(String msg, Throwable throwable) {
        debug(this.source, msg, throwable);
    }

    /**
     * Logs a message by creating a new {@linkplain LogRecord} of type {@linkplain LogLevel#DEBUG} from the passed in
     * message. This method has the same effect as {@linkplain #debug(Class, String) debug} {@code (this.source, msg)}.
     *
     * @param msg the message.
     * @throws NullPointerException if the specified message is {@code null}.
     */
    public void debug(String msg) {
        debug(this.source, msg);
    }

    /**
     * Logs a message by creating a new {@linkplain LogRecord} of type {@linkplain LogLevel#FATAL} from the passed in
     * source {@linkplain Class}, message as well as the {@linkplain Throwable} associated with it. This method has the
     * same effect as {@linkplain #log(LogLevel, Class, String, Throwable) log}
     * {@code (LogLevel.FATAL, source, msg, throwable)}.
     *
     * @param source    the source {@code Class} of the message.
     * @param msg       the message.
     * @param throwable the {@code Throwable} associated with the message.
     * @throws NullPointerException if the specified source {@code Class} or message are {@code null}.
     */
    public void fatal(Class<?> source, String msg, Throwable throwable) {
        log(LogLevel.FATAL, source, msg, throwable);
    }

    /**
     * Logs a message by creating a new {@linkplain LogRecord} of type {@linkplain LogLevel#FATAL} from the passed in
     * source {@linkplain Class} and message. This method has the same effect as
     * {@linkplain #fatal(Class, String, Throwable) fatal} {@code (source, msg, null)}.
     *
     * @param source the source {@code Class} of the message.
     * @param msg    the message.
     * @throws NullPointerException if the specified source {@code Class} or message are {@code null}.
     */
    public void fatal(Class<?> source, String msg) {
        fatal(source, msg, null);
    }

    /**
     * Logs a message by creating a new {@linkplain LogRecord} of type {@linkplain LogLevel#FATAL} from the passed in
     * message as well as the {@linkplain Throwable} associated with it. This method has the same effect as
     * {@linkplain #fatal(Class, String, Throwable) fatal} {@code (this.source, msg, throwable)}.
     *
     * @param msg       the message.
     * @param throwable the {@code Throwable} associated with the message.
     * @throws NullPointerException if the specified message is {@code null}.
     */
    public void fatal(String msg, Throwable throwable) {
        fatal(this.source, msg, throwable);
    }

    /**
     * Logs a message by creating a new {@linkplain LogRecord} of type {@linkplain LogLevel#FATAL} from the passed in
     * message. This method has the same effect as {@linkplain #fatal(Class, String) fatal} {@code (this.source, msg)}.
     *
     * @param msg the message.
     * @throws NullPointerException if the specified message is {@code null}.
     */
    public void fatal(String msg) {
        fatal(this.source, msg);
    }

    /**
     * Logs a message by creating a new {@linkplain LogRecord} of type {@linkplain LogLevel#ERROR} from the passed in
     * source {@linkplain Class}, message as well as the {@linkplain Throwable} associated with it. This method has the
     * same effect as {@linkplain #log(LogLevel, Class, String, Throwable) log}
     * {@code (LogLevel.ERROR, source, msg, throwable)}.
     *
     * @param source    the source {@code Class} of the message.
     * @param msg       the message.
     * @param throwable the {@code Throwable} associated with the message.
     * @throws NullPointerException if the specified source {@code Class} or message are {@code null}.
     */
    public void error(Class<?> source, String msg, Throwable throwable) {
        log(LogLevel.ERROR, source, msg, throwable);
    }

    /**
     * Logs a message by creating a new {@linkplain LogRecord} of type {@linkplain LogLevel#ERROR} from the passed in
     * source {@linkplain Class} and message. This method has the same effect as
     * {@linkplain #error(Class, String, Throwable) error} {@code (source, msg, null)}.
     *
     * @param source the source {@code Class} of the message.
     * @param msg    the message.
     * @throws NullPointerException if the specified source {@code Class} or message are {@code null}.
     */
    public void error(Class<?> source, String msg) {
        error(source, msg, null);
    }

    /**
     * Logs a message by creating a new {@linkplain LogRecord} of type {@linkplain LogLevel#ERROR} from the passed in
     * message as well as the {@linkplain Throwable} associated with it. This method has the same effect as
     * {@linkplain #error(Class, String, Throwable) error} {@code (this.source, msg, throwable)}.
     *
     * @param msg       the message.
     * @param throwable the {@code Throwable} associated with the message.
     * @throws NullPointerException if the specified message is {@code null}.
     */
    public void error(String msg, Throwable throwable) {
        error(this.source, msg, throwable);
    }

    /**
     * Logs a message by creating a new {@linkplain LogRecord} of type {@linkplain LogLevel#ERROR} from the passed in
     * message. This method has the same effect as {@linkplain #error(Class, String) error} {@code (this.source, msg)}.
     *
     * @param msg the message.
     * @throws NullPointerException if the specified message is {@code null}.
     */
    public void error(String msg) {
        error(this.source, msg);
    }

    /**
     * Logs a message by creating a new {@linkplain LogRecord} of type {@linkplain LogLevel#WARNING} from the passed in
     * source {@linkplain Class}, message as well as the {@linkplain Throwable} associated with it. This method has the
     * same effect as {@linkplain #log(LogLevel, Class, String, Throwable) log}
     * {@code (LogLevel.WARNING, source, msg, throwable)}.
     *
     * @param source    the source {@code Class} of the message.
     * @param msg       the message.
     * @param throwable the {@code Throwable} associated with the message.
     * @throws NullPointerException if the specified source {@code Class} or message are {@code null}.
     */
    public void warning(Class<?> source, String msg, Throwable throwable) {
        log(LogLevel.WARNING, source, msg, throwable);
    }

    /**
     * Logs a message by creating a new {@linkplain LogRecord} of type {@linkplain LogLevel#WARNING} from the passed in
     * source {@linkplain Class} and message. This method has the same effect as
     * {@linkplain #warning(Class, String, Throwable) warning} {@code (source, msg, null)}.
     *
     * @param source the source {@code Class} of the message.
     * @param msg    the message.
     * @throws NullPointerException if the specified source {@code Class} or message are {@code null}.
     */
    public void warning(Class<?> source, String msg) {
        warning(source, msg, null);
    }

    /**
     * Logs a message by creating a new {@linkplain LogRecord} of type {@linkplain LogLevel#WARNING} from the passed in
     * message as well as the {@linkplain Throwable} associated with it. This method has the same effect as
     * {@linkplain #warning(Class, String, Throwable) warning} {@code (this.source, msg, throwable)}.
     *
     * @param msg       the message.
     * @param throwable the {@code Throwable} associated with the message.
     * @throws NullPointerException if the specified message is {@code null}.
     */
    public void warning(String msg, Throwable throwable) {
        warning(this.source, msg, throwable);
    }

    /**
     * Logs a message by creating a new {@linkplain LogRecord} of type {@linkplain LogLevel#WARNING} from the passed in
     * message. This method has the same effect as {@linkplain #warning(Class, String) warning}
     * {@code (this.source, msg)}.
     *
     * @param msg the message.
     * @throws NullPointerException if the specified message is {@code null}.
     */
    public void warning(String msg) {
        warning(this.source, msg);
    }

    /**
     * Logs a message by creating a new {@linkplain LogRecord} of type {@linkplain LogLevel#INFO} from the passed in
     * source {@linkplain Class}, message as well as the {@linkplain Throwable} associated with it. This method has the
     * same effect as {@linkplain #log(LogLevel, Class, String, Throwable) log}
     * {@code (LogLevel.INFO, source, msg, throwable)}.
     *
     * @param source    the source {@code Class} of the message.
     * @param msg       the message.
     * @param throwable the {@code Throwable} associated with the message.
     * @throws NullPointerException if the specified source {@code Class} or message are {@code null}.
     */
    public void info(Class<?> source, String msg, Throwable throwable) {
        log(LogLevel.INFO, source, msg, throwable);
    }

    /**
     * Logs a message by creating a new {@linkplain LogRecord} of type {@linkplain LogLevel#INFO} from the passed in
     * source {@linkplain Class} and message. This method has the same effect as
     * {@linkplain #info(Class, String, Throwable) info} {@code (source, msg, null)}.
     *
     * @param source the source {@code Class} of the message.
     * @param msg    the message.
     * @throws NullPointerException if the specified source {@code Class} or message are {@code null}.
     */
    public void info(Class<?> source, String msg) {
        info(source, msg, null);
    }

    /**
     * Logs a message by creating a new {@linkplain LogRecord} of type {@linkplain LogLevel#INFO} from the passed in
     * message as well as the {@linkplain Throwable} associated with it. This method has the same effect as
     * {@linkplain #info(Class, String, Throwable) info} {@code (this.source, msg, throwable)}.
     *
     * @param msg       the message.
     * @param throwable the {@code Throwable} associated with the message.
     * @throws NullPointerException if the specified message is {@code null}.
     */
    public void info(String msg, Throwable throwable) {
        info(this.source, msg, throwable);
    }

    /**
     * Logs a message by creating a new {@linkplain LogRecord} of type {@linkplain LogLevel#INFO} from the passed in
     * message. This method has the same effect as {@linkplain #info(Class, String) info} {@code (this.source, msg)}.
     *
     * @param msg the message.
     * @throws NullPointerException if the specified message is {@code null}.
     */
    public void info(String msg) {
        info(this.source, msg);
    }

    /**
     * Logs a message by creating a new {@linkplain LogRecord} of type {@linkplain LogLevel#FINE} from the passed in
     * source {@linkplain Class}, message as well as the {@linkplain Throwable} associated with it. This method has the
     * same effect as {@linkplain #log(LogLevel, Class, String, Throwable) log}
     * {@code (LogLevel.FINE, source, msg, throwable)}.
     *
     * @param source    the source {@code Class} of the message.
     * @param msg       the message.
     * @param throwable the {@code Throwable} associated with the message.
     * @throws NullPointerException if the specified source {@code Class} or message are {@code null}.
     */
    public void fine(Class<?> source, String msg, Throwable throwable) {
        log(LogLevel.FINE, source, msg, throwable);
    }

    /**
     * Logs a message by creating a new {@linkplain LogRecord} of type {@linkplain LogLevel#FINE} from the passed in
     * source {@linkplain Class} and message. This method has the same effect as
     * {@linkplain #fine(Class, String, Throwable) fine} {@code (source, msg, null)}.
     *
     * @param source the source {@code Class} of the message.
     * @param msg    the message.
     * @throws NullPointerException if the specified source {@code Class} or message are {@code null}.
     */
    public void fine(Class<?> source, String msg) {
        fine(source, msg, null);
    }

    /**
     * Logs a message by creating a new {@linkplain LogRecord} of type {@linkplain LogLevel#FINE} from the passed in
     * message as well as the {@linkplain Throwable} associated with it. This method has the same effect as
     * {@linkplain #fine(Class, String, Throwable) fine} {@code (this.source, msg, throwable)}.
     *
     * @param msg       the message.
     * @param throwable the {@code Throwable} associated with the message.
     * @throws NullPointerException if the specified message is {@code null}.
     */
    public void fine(String msg, Throwable throwable) {
        fine(this.source, msg, throwable);
    }

    /**
     * Logs a message by creating a new {@linkplain LogRecord} of type {@linkplain LogLevel#FINE} from the passed in
     * message. This method has the same effect as {@linkplain #fine(Class, String) fine} {@code (this.source, msg)}.
     *
     * @param msg the message.
     * @throws NullPointerException if the specified message is {@code null}.
     */
    public void fine(String msg) {
        fine(this.source, msg);
    }

    /**
     * Adds the passed in {@linkplain LogRecordHandler} to the {@linkplain Set} of handlers of this {@code Logger}.
     *
     * @param handler the {@code LogRecordHandler} to be added.
     * @throws NullPointerException if the specified {@code LogRecordHandler} is {@code null}.
     */
    public void addLogRecordHandler(LogRecordHandler handler) {
        Objects.requireNonNull(handler, "handler must not be null");
        logRecordHandlerSet.add(handler);
    }

    /**
     * Removes the passed in {@linkplain LogRecordHandler} from the {@linkplain Set} of handlers of this {@code Logger}.
     *
     * @param handler the {@code LogRecordHandler} to be removed.
     * @throws NullPointerException if the specified {@code LogRecordHandler} is {@code null}.
     */
    public void removeLogRecordHandler(LogRecordHandler handler) {
        Objects.requireNonNull(handler, "handler must not be null");
        logRecordHandlerSet.remove(handler);
    }

    /**
     * Sets the {@linkplain LogLevel} of this {@code Logger}. Any {@linkplain LogRecord} below this {@code LogLevel}
     * will not be logged into this {@code Logger}.
     *
     * @param level the {@code LogLevel} to set this {@code Logger} to.
     * @throws NullPointerException if the specified {@code LogLevel} is {@code null}.
     */
    public void setLogLevel(LogLevel level) {
        this.logLevel = Objects.requireNonNull(level, "level must not be null");
    }

    /**
     * Returns the {@linkplain LogLevel} of this {@code Logger}.
     *
     * @return the {@code LogLevel} of this {@code Logger}.
     */
    public LogLevel getLogLevel() {
        return logLevel;
    }

    /**
     * Returns an unmodifiable {@linkplain List} of all {@linkplain LogRecord LogRecords} of this {@code Logger}.
     *
     * @return an unmodifiable {@code List} of all {@code LogRecords} of this {@code Logger}.
     */
    public List<LogRecord> getAllLogs() {
        return Collections.unmodifiableList(logRecordList);
    }

    /**
     * Returns an unmodifiable {@linkplain List} of all {@linkplain LogRecord LogRecords} of this {@code Logger}, which
     * are of the specified {@linkplain LogLevel}.
     *
     * @param level the {@code LogLevel} which the returned {@code LogRecords} must be of.
     * @return an unmodifiable {@code List} of all {@code LogRecords} of this {@code Logger} which are of the passed in
     * {@code LogLevel}.
     * @throws NullPointerException if the specified {@code LogLevel} is {@code null}.
     */
    public List<LogRecord> getAllLogsOfLevel(LogLevel level) {
        Objects.requireNonNull(level, "level must not be null");
        return logRecordList.stream().filter(e -> e.getLevel() == level).toList();
    }

    /**
     * Returns an unmodifiable {@linkplain List} of all {@linkplain LogRecord LogRecords} of this {@code Logger}, which
     * are of the {@linkplain LogLevel LogLevels} in the specified in {@linkplain EnumSet} of {@code LogLevels}.
     *
     * @param levels the {@code LogLevels} which the returned {@code LogRecords} must be of.
     * @return an unmodifiable {@code List} of all {@code LogRecords} of this {@code Logger} which are of the
     * {@code LogLevels} in the specified in {@code EnumSet} of {@code LogLevels}.
     * @throws NullPointerException if the specified {@code EnumSet} is {@code null}.
     */
    public List<LogRecord> getAllLogsOfLevels(EnumSet<LogLevel> levels) {
        Objects.requireNonNull(levels, "levels must not be null");
        return logRecordList.stream().filter(e -> levels.contains(e.getLevel())).toList();
    }

    /**
     * Returns the source {@linkplain Class} of this {@code Logger}.
     *
     * @return the source {@code Class} of this {@code Logger}.
     */
    public Class<?> getSource() {
        return source;
    }

    /**
     * Helper method used to add the passed in {@linkplain LogRecord} to the {@linkplain List} of {@code LogRecords} if
     * the {@linkplain LogLevel} of this {@code Logger} is less or equal to the {@code LogLevel} of the passed in
     * {@code LogRecord}. Finally, calls the appropriate method on all {@linkplain LogRecordHandler LogRecordHandlers}
     * of this {@code Logger}.
     *
     * @param logRecord the {@code LogRecord} to add.
     * @throws NullPointerException if the specified {@code LogRecord} is {@code null}.
     */
    private void addLog(LogRecord logRecord) {
        Objects.requireNonNull(logRecord, "logRecord must not be null");
        if (this.logLevel.getPriority() <= logRecord.getLevel().getPriority()) {
            logRecordList.add(logRecord);
            logRecordHandlerSet.forEach(h -> h.handle(logRecord));
        }
    }
}