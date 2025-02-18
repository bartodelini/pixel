package com.bartodelini.pixel.modules.terminal.shell.commands;

import com.bartodelini.pixel.environment.Command;
import com.bartodelini.pixel.modules.terminal.shell.ShellEnvironment;

import java.util.List;
import java.util.Objects;

/**
 * A <i>CmdWhoami</i> is used to display the current username of a {@linkplain ShellEnvironment}.
 *
 * @author Bartolini
 * @version 1.1
 */
public class CmdWhoami extends Command {

    private final ShellEnvironment shellEnvironment;

    /**
     * Allocates a new {@code CmdWhoami} object py passing in a {@linkplain ShellEnvironment}.
     *
     * @param shellEnvironment the {@code ShellEnvironment} to be used by this command.
     * @throws NullPointerException if the specified {@code ShellEnvironment} is {@code null}.
     */
    public CmdWhoami(ShellEnvironment shellEnvironment) {
        super("whoami", "Displays current user name.");
        this.shellEnvironment = Objects.requireNonNull(shellEnvironment, "shellEnvironment must not be null");
    }

    @Override
    public int execute(StringBuilder stringBuilder, List<String> args) {
        stringBuilder.append(shellEnvironment.getUsername());
        return 0;
    }
}