package com.bartodelini.pixel.modules.terminal.commands;

import com.bartodelini.pixel.environment.Command;
import com.bartodelini.pixel.modules.terminal.Terminal;

import java.util.List;
import java.util.Objects;

/**
 * A <i>CmdToggleTerminal</i> is used to toggle the visibility of a {@linkplain Terminal}.
 *
 * @author Bartolini
 * @version 1.0
 */
public class CmdToggleTerminal extends Command {

    private final Terminal terminal;

    /**
     * Allocates a new {@code CmdToggleTerminal} by passing in a {@linkplain Terminal}.
     *
     * @param terminal the {@code Terminal} used by this command.
     * @throws NullPointerException if the specified {@code Terminal} is {@code null}.
     */
    public CmdToggleTerminal(Terminal terminal) {
        super("toggle_terminal", "Shows/hides the terminal window.");
        this.terminal = Objects.requireNonNull(terminal, "terminal must not be null");
    }

    @Override
    public int execute(StringBuilder stringBuilder, List<String> args) {
        terminal.setVisible(!terminal.isVisible());
        return 0;
    }
}