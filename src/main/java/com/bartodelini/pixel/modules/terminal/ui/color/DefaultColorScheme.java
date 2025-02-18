package com.bartodelini.pixel.modules.terminal.ui.color;

import com.bartodelini.pixel.modules.terminal.ui.TerminalUI;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * A <i>DefaultColorScheme</i> implements the default {@linkplain TerminalColorScheme} for the {@linkplain TerminalUI}.
 *
 * @author Bartolini
 * @version 1.0
 */
public class DefaultColorScheme implements TerminalColorScheme {

    private final Map<TerminalColor, Color> colorMap = new HashMap<>();

    /**
     * Allocates a new {@code DefaultColorScheme} object.
     */
    public DefaultColorScheme() {
        colorMap.put(TerminalColor.BLACK, Color.BLACK);
        colorMap.put(TerminalColor.GRAY, Color.GRAY);
        colorMap.put(TerminalColor.LIGHT_GRAY, Color.LIGHT_GRAY);
        colorMap.put(TerminalColor.BLUE, new Color(0x5c9fd8));
        colorMap.put(TerminalColor.GREEN, new Color(0x7cd85c));
        colorMap.put(TerminalColor.YELLOW, new Color(0xb58900));
        colorMap.put(TerminalColor.ORANGE, new Color(0xcb4b16));
        colorMap.put(TerminalColor.CYAN, new Color(0x2aa198));
        colorMap.put(TerminalColor.WHITE, Color.WHITE);
        colorMap.put(TerminalColor.MAGENTA, new Color(0xd33682));
        colorMap.put(TerminalColor.RED, new Color(0xdc322f));

        colorMap.put(TerminalColor.FOREGROUND, Color.WHITE);
        colorMap.put(TerminalColor.BACKGROUND, new Color(0x091016));
        colorMap.put(TerminalColor.SELECTION, new Color(0x1b98e0));

        colorMap.put(TerminalColor.DEBUG, Color.CYAN);
        colorMap.put(TerminalColor.ERROR, Color.RED);
        colorMap.put(TerminalColor.WARNING, Color.ORANGE);
        colorMap.put(TerminalColor.INFO, new Color(0x2aa198));
        colorMap.put(TerminalColor.FINE, Color.GREEN);
    }

    @Override
    public Color getColor(TerminalColor color) {
        Color resultColor = colorMap.get(color);
        if (resultColor == null) {
            resultColor = Color.WHITE;
        }
        return resultColor;
    }
}