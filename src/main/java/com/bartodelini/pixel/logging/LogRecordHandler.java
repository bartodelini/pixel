package com.bartodelini.pixel.logging;

/**
 * A <i>LogRecordHandler</i> is used to handle an incoming {@linkplain LogRecord}.
 *
 * @author Bartolini
 * @version 1.0
 */
public interface LogRecordHandler {
    void handle(LogRecord record);
}