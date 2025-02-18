package com.bartodelini.pixel.modules.terminal.shell.commands;

import com.bartodelini.pixel.environment.Command;
import com.bartodelini.pixel.modules.terminal.shell.ShellEnvironment;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/**
 * A <i>CmdCD</i> is used to change the {@linkplain Path working path} of a {@linkplain ShellEnvironment}.
 *
 * @author Bartolini
 * @version 1.1
 */
public class CmdCD extends Command {

    private final ShellEnvironment shellEnvironment;

    /**
     * Allocates a new {@code CmdCD} object py passing in a {@linkplain ShellEnvironment}.
     *
     * @param shellEnvironment the {@code ShellEnvironment} to be used by this command.
     * @throws NullPointerException if the specified {@code ShellEnvironment} is {@code null}.
     */
    public CmdCD(ShellEnvironment shellEnvironment) {
        super("cd", "Changes the current working directory.");
        this.shellEnvironment = Objects.requireNonNull(shellEnvironment, "shellEnvironment must not be null");
    }

    @Override
    public int execute(StringBuilder stringBuilder, List<String> args) {
        // Set the working path to the home path
        if (args == null || args.isEmpty() || args.get(0).equals("~")) {
            if (!shellEnvironment.getWorkingPath().equals(shellEnvironment.getHomePath())) {
                shellEnvironment.setWorkingPath(shellEnvironment.getHomePath());
            }
            return 0;
        }

        // Move one directory up in the working path
        if (args.get(0).equals("..")) {
            if (shellEnvironment.getWorkingPath().toAbsolutePath().toFile().getParentFile() != null) {
                shellEnvironment.setWorkingPath(
                        shellEnvironment.getWorkingPath().toAbsolutePath().toFile().getParentFile().toPath());
            }
            return 0;
        }

        // Set the working path to the specified path if the specified path can be resolved
        Path path = shellEnvironment.getWorkingPath().resolve(args.get(0));
        if (path.toFile().exists() && path.toFile().isDirectory()) {
            shellEnvironment.setWorkingPath(path);
        } else {
            // ... otherwise show an error
            stringBuilder.append("'").append(args.get(0)).append("'").append(" is not a directory.");
            return -1;
        }

        return 0;
    }
}