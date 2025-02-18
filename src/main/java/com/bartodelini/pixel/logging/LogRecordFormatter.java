package com.bartodelini.pixel.logging;

/**
 * A <i>LogRecordFormatter</i> is used to format a {@linkplain LogRecord} into a string form.
 *
 * @author Bartolini
 * @version 1.0
 */
public interface LogRecordFormatter {
    String format(LogRecord record);
}