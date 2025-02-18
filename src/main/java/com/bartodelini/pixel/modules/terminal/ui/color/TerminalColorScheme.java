package com.bartodelini.pixel.modules.terminal.ui.color;

import java.awt.*;

/**
 * A <i>TerminalColorScheme</i> is used to assign a {@linkplain Color} to a specific {@linkplain TerminalColor}.
 *
 * @author Bartolini
 * @version 1.0
 */
public interface TerminalColorScheme {

    /**
     * Returns a {@linkplain Color} depending on the passed in {@linkplain TerminalColor}.
     *
     * @param color the specified {@code TerminalColor}.
     * @return a {@code Color} depending on the specified {@code TerminalColor}.
     */
    Color getColor(TerminalColor color);
}