package com.bartodelini.pixel.modules.terminal.commands;

import com.bartodelini.pixel.environment.Command;

import java.util.List;

/**
 * A <i>CmdEcho</i> is used to print text to the output.
 *
 * @author Bartolini
 * @version 1.0
 */
public class CmdEcho extends Command {

    /**
     * Allocates a new {@code CmdEcho}.
     */
    public CmdEcho() {
        super("echo", "Prints a given message to the console.");
    }

    @Override
    public int execute(StringBuilder stringBuilder, List<String> args) {
        if (args != null) {
            // Append the arguments to the output
            stringBuilder.append(String.join(" ", args));
        }
        return 1;
    }
}