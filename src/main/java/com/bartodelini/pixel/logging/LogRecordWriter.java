package com.bartodelini.pixel.logging;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;

/**
 * A <i>LogRecordWriter</i> is used to write a {@linkplain List} of {@linkplain LogRecord LogRecords} to a specified
 * location on the hard drive.
 *
 * @author Bartolini
 * @version 1.1
 */
public class LogRecordWriter {

    private final List<LogRecord> logRecordList;
    private final LogRecordFormatter formatter;

    /**
     * Allocates a new {@code StreamHandler} object by passing in a {@linkplain List} of
     * {@linkplain LogRecord LogRecords} and a {@linkplain LogRecordFormatter}.
     *
     * @param logRecordList the {@code List} containing {@code LogRecords}.
     * @param formatter     the {@code LogRecordFormatter} which will be used for formatting the messages.
     * @throws NullPointerException if the specified {@code List} or {@code LogRecordFormatter} are {@code null}.
     */
    public LogRecordWriter(List<LogRecord> logRecordList, LogRecordFormatter formatter) {
        this.logRecordList = Objects.requireNonNull(logRecordList, "logRecordList must not be null");
        this.formatter = Objects.requireNonNull(formatter, "formatter must not be null");
    }

    /**
     * Writes the {@linkplain List} of {@linkplain LogRecord LogRecords} to the specified path after formatting each
     * with the specified {@linkplain LogRecordFormatter}.
     *
     * @param path the path to write the file to.
     * @throws NullPointerException if the specified path is {@code null}.
     */
    public void writeToFile(String path) {
        Objects.requireNonNull(path, "path must not be null");
        try (PrintWriter writer = new PrintWriter(path)) {
            logRecordList.forEach(r -> writer.println(formatter.format(r)));
        } catch (FileNotFoundException ignored) {
        }
    }
}