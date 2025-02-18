package com.bartodelini.pixel.modules.terminal.commands;

import com.bartodelini.pixel.environment.Command;
import com.bartodelini.pixel.modules.terminal.ui.TerminalUI;

import java.util.List;
import java.util.Objects;

/**
 * A <i>CmdClear</i> is responsible for clearing a {@linkplain TerminalUI}.
 *
 * @author Bartolini
 * @version 1.0
 */
public class CmdClear extends Command {

    private final TerminalUI terminalUI;

    /**
     * Allocates a new {@code CmdClear} by passing in a {@linkplain TerminalUI}.
     *
     * @param terminalUI the {@code TerminalUI} used in this command.
     * @throws NullPointerException if the specified {@code TerminalUI} is {@code null}.
     */
    public CmdClear(TerminalUI terminalUI) {
        super("clear", "Clears the console screen.");
        this.terminalUI = Objects.requireNonNull(terminalUI, "terminalUI must not be null");
    }

    @Override
    public int execute(StringBuilder stringBuilder, List<String> args) {
        terminalUI.clear();
        return 1;
    }
}