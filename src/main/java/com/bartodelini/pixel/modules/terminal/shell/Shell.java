package com.bartodelini.pixel.modules.terminal.shell;

import java.util.Collection;

/**
 * A <i>Shell</i> is used to execute command strings.
 *
 * @author Bartolini
 * @version 1.0
 */
public interface Shell {

    /**
     * Returns an {@linkplain ExecuteResult} from executing the passed in command.
     *
     * @param input the command to execute.
     * @return the {@code ExecuteResult} from executing the specified command.
     */
    Collection<ExecuteResult> execute(String input);

    /**
     * Returns the {@linkplain ShellEnvironment} of this {@code Shell}.
     *
     * @return the {@code ShellEnvironment} of this {@code Shell}.
     */
    ShellEnvironment getShellEnvironment();
}