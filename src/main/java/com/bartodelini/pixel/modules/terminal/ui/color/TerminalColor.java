package com.bartodelini.pixel.modules.terminal.ui.color;

import com.bartodelini.pixel.modules.terminal.ui.TerminalUI;

/**
 * A <i>TerminalColor</i> is an abstraction for a color used in the {@linkplain TerminalUI}.
 *
 * @author Bartolini
 * @version 1.0
 */
public enum TerminalColor {
    BLACK,
    GRAY,
    LIGHT_GRAY,
    BLUE,
    GREEN,
    YELLOW,
    ORANGE,
    CYAN,
    WHITE,
    MAGENTA,
    RED,

    FOREGROUND,
    BACKGROUND,
    SELECTION,

    DEBUG,
    ERROR,
    WARNING,
    INFO,
    FINE
}