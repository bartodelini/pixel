package com.bartodelini.pixel.modules.terminal.shell.dispatcher;

import com.bartodelini.pixel.modules.terminal.shell.ExecuteResult;
import com.bartodelini.pixel.modules.terminal.shell.parser.ParseResult;

/**
 * A <i>Dispatcher</i> is used to dispatch parsed commands.
 *
 * @author Bartolini
 * @version 1.0
 */
public interface Dispatcher {

    /**
     * Dispatches a {@linkplain ParseResult} and returns the resulting {@linkplain ExecuteResult}.
     *
     * @param parseResult the {@code ParseResult} to dispatch.
     * @return the {@code ExecuteResult} of the dispatching.
     */
    ExecuteResult dispatch(ParseResult parseResult);
}