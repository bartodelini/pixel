package com.bartodelini.pixel.modules.terminal.shell.commands;

import com.bartodelini.pixel.environment.Command;
import com.bartodelini.pixel.modules.terminal.shell.ShellEnvironment;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/**
 * A <i>CmdPWD</i> is used to display the current {@linkplain Path working path} of a {@linkplain ShellEnvironment}.
 *
 * @author Bartolini
 * @version 1.1
 */
public class CmdPWD extends Command {

    private final ShellEnvironment shellEnvironment;

    /**
     * Allocates a new {@code CmdPWD} object py passing in a {@linkplain ShellEnvironment}.
     *
     * @param shellEnvironment the {@code ShellEnvironment} to be used by this command.
     * @throws NullPointerException if the specified {@code ShellEnvironment} is {@code null}.
     */
    public CmdPWD(ShellEnvironment shellEnvironment) {
        super("pwd", "Displays current working directory.");
        this.shellEnvironment = Objects.requireNonNull(shellEnvironment, "shellEnvironment must not be null");
    }

    @Override
    public int execute(StringBuilder stringBuilder, List<String> args) {
        stringBuilder.append(shellEnvironment.getWorkingPath().toAbsolutePath());
        return 0;
    }
}