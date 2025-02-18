package com.bartodelini.pixel.logging;

import java.io.PrintStream;
import java.util.Objects;

/**
 * A <i>StreamHandler</i> is a {@linkplain LogRecordHandler}, which can be used to print the recieved
 * {@linkplain LogRecord LogRecords} to a {@linkplain PrintStream} after formatting them with the specified
 * {@linkplain LogRecordFormatter}.
 *
 * @author Bartolini
 * @version 1.1
 */
public class StreamHandler implements LogRecordHandler {

    private final PrintStream printStream;
    private final LogRecordFormatter formatter;

    /**
     * Allocates a new {@code StreamHandler} object by passing in a {@linkplain PrintStream} and a
     * {@linkplain LogRecordFormatter}.
     *
     * @param printStream the {@code PrintStream} which this {@code StreamHandler} will print to.
     * @param formatter   the {@code LogRecordFormatter} which will be used for formatting the messages.
     * @throws NullPointerException if the specified {@code PrintStream} or {@code LogRecordFormatter} are {@code null}.
     */
    public StreamHandler(PrintStream printStream, LogRecordFormatter formatter) {
        this.printStream = Objects.requireNonNull(printStream, "printStream must not be null");
        this.formatter = Objects.requireNonNull(formatter, "formatter must not be null");
    }

    @Override
    public void handle(LogRecord record) {
        printStream.println(formatter.format(record));
    }
}