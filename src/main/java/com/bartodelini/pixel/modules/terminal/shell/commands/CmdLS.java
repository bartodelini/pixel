package com.bartodelini.pixel.modules.terminal.shell.commands;

import com.bartodelini.pixel.environment.Command;
import com.bartodelini.pixel.modules.terminal.shell.ShellEnvironment;

import java.io.IOException;
import java.nio.file.*;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

/**
 * A <i>CmdLS</i> is used to list all files in the current {@linkplain Path working path} of a
 * {@linkplain ShellEnvironment}.
 *
 * @author Bartolini
 * @version 1.1
 */
public class CmdLS extends Command {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy  HH:mm");
    private final ShellEnvironment shellEnvironment;

    /**
     * Allocates a new {@code CmdLS} object py passing in a {@linkplain ShellEnvironment}.
     *
     * @param shellEnvironment the {@code ShellEnvironment} to be used by this command.
     * @throws NullPointerException if the specified {@code ShellEnvironment} is {@code null}.
     */
    public CmdLS(ShellEnvironment shellEnvironment) {
        super("ls", "Lists the content of the current working directory.");
        this.shellEnvironment = Objects.requireNonNull(shellEnvironment, "shellEnvironment must not be null");
    }

    @Override
    public int execute(StringBuilder stringBuilder, List<String> args) {
        if (args == null || args.isEmpty()) {
            try {
                // Find the longest entry name
                AtomicInteger longestEntry = new AtomicInteger();
                int padding = 2;
                try (Stream<Path> files = Files.list(shellEnvironment.getWorkingPath().toAbsolutePath())) {
                    files.forEach(path -> {
                        // Ignore hidden files
                        if (path.toFile().isHidden()) {
                            return;
                        }
                        longestEntry.set(Math.max(longestEntry.get(), path.getFileName().toString().length()));
                    });
                }

                // Create output
                try (Stream<Path> files = Files.list(shellEnvironment.getWorkingPath().toAbsolutePath())) {
                    files.forEach(path -> {
                        // Ignore hidden files
                        if (path.toFile().isHidden()) {
                            return;
                        }
                        // Pad the string to an equal length
                        String name = String.format("%1$-" + (longestEntry.get() + padding) + "s", path.getFileName().toString());
                        // Color the filename blue if it is a directory
                        if (path.toFile().isDirectory()) {
                            name = "[color=blue]" + name + "[/color]";
                        }
                        stringBuilder.append(name);
                    });
                }
            } catch (NotDirectoryException e) {
                stringBuilder.append("the current working path is not a directory.");
                return -1;
            } catch (IOException e) {
                stringBuilder.append("the directory could not be opened.");
                return -1;
            } catch (SecurityException e) {
                stringBuilder.append("this operation was denied by the security manager.");
                return -1;
            }
            return 0;
        }

        if (args.get(0).equals("-l")) {
            try {
                // Find the largest file size
                AtomicLong largestFileSize = new AtomicLong();
                try (Stream<Path> files = Files.list(shellEnvironment.getWorkingPath().toAbsolutePath())) {
                    files.forEach(path -> {
                        // Ignore hidden files
                        if (path.toFile().isHidden()) {
                            return;
                        }
                        if (path.toFile().isFile()) {
                            try {
                                largestFileSize.set(Math.max(largestFileSize.get(), Files.size(path)));
                            } catch (IOException ignored) {
                            }
                        }
                    });
                }

                // Create output
                try (Stream<Path> files = Files.list(shellEnvironment.getWorkingPath().toAbsolutePath())) {
                    long largestFileSizeLength = String.valueOf(largestFileSize).length();
                    files.forEach(path -> {
                        // Ignore hidden files
                        if (path.toFile().isHidden()) {
                            return;
                        }
                        String name = path.getFileName().toString();
                        // Color the filename blue if it is a directory
                        if (path.toFile().isDirectory()) {
                            name = "[color=blue]" + name + "[/color]";
                        }
                        try {
                            String lastModifiedTime = Files.getLastModifiedTime(path)
                                    .toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDateTime()
                                    .format(dateTimeFormatter);
                            stringBuilder
                                    .append("[color=light_gray]").append(lastModifiedTime).append("[/color]")
                                    .append((largestFileSize.get() == 0 ? "" : "  "))
                                    .append(String.format("%1$" + largestFileSizeLength + "s", (path.toFile().isDirectory() ? "" : Files.size(path))))
                                    .append((largestFileSize.get() == 0 ? "" : " "))
                                    .append(" ")
                                    .append(name)
                                    .append("\n");
                        } catch (IOException ignored) {
                        }
                    });
                    // trim last new line character
                    stringBuilder.setLength(Math.max(stringBuilder.length() - 1, 0));
                }
            } catch (NotDirectoryException e) {
                stringBuilder.append("the current working path is not a directory.");
                return -1;
            } catch (IOException e) {
                stringBuilder.append("the directory could not be opened.");
                return -1;
            } catch (SecurityException e) {
                stringBuilder.append("this operation was denied by the security manager.");
                return -1;
            }
            return 0;
        }

        return 0;
    }
}