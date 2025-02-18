package com.bartodelini.pixel.modules.terminal.shell;

import com.bartodelini.pixel.modules.terminal.shell.dispatcher.Dispatcher;
import com.bartodelini.pixel.modules.terminal.shell.parser.Parser;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A <i>SimpleShell</i> is used to execute command strings.
 *
 * @author Bartolini
 * @version 1.0
 */
public class SimpleShell implements Shell {

    private final ShellEnvironment shellEnvironment;
    private final Parser parser;
    private final Dispatcher dispatcher;

    /**
     * Allocates a new {@code SimpleShell} object by passing in a {@linkplain ShellEnvironment}, {@linkplain Parser} and
     * {@linkplain Dispatcher}.
     *
     * @param parser     the {@code Parser} to be used by this {@code Shell}.
     * @param dispatcher the {@code Dispatcher} to be used by this {@code Shell}.
     * @throws NullPointerException if the specified {@code ShellEnvironment}, {@code Parser} or {@code Dispatcher} are
     *                              {@code null}.
     */
    public SimpleShell(ShellEnvironment shellEnvironment, Parser parser, Dispatcher dispatcher) {
        this.shellEnvironment = Objects.requireNonNull(shellEnvironment, "shellEnvironment must not be null");
        this.parser = Objects.requireNonNull(parser, "parser must not be null");
        this.dispatcher = Objects.requireNonNull(dispatcher, "dispatcher must not be null");
    }

    @Override
    public Collection<ExecuteResult> execute(String input) {
        return parser.parseInput(input).stream().map(dispatcher::dispatch).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public ShellEnvironment getShellEnvironment() {
        return shellEnvironment;
    }
}