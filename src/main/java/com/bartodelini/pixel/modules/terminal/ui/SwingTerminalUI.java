package com.bartodelini.pixel.modules.terminal.ui;

import com.bartodelini.pixel.core.Engine;
import com.bartodelini.pixel.logging.Logger;
import com.bartodelini.pixel.logging.LoggerFactory;
import com.bartodelini.pixel.modules.terminal.bbtext.*;
import com.bartodelini.pixel.modules.terminal.ui.color.TerminalColor;
import com.bartodelini.pixel.modules.terminal.ui.color.TerminalColorScheme;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.im.InputContext;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.function.Consumer;

/**
 * A <i>SwingTerminalUI</i> is a {@linkplain TerminalUI} which is implemented using the Swing api.
 *
 * @author Bartolini
 * @version 1.1
 */
public class SwingTerminalUI extends JFrame implements TerminalUI {

    private final Logger logger = LoggerFactory.getLogger(this);
    private final Set<Consumer<String>> inputConsumerSet = new HashSet<>();
    private final BBParser bbParser;
    private final Autocomplete autocomplete;

    private final JTextPane textPane;
    private final JScrollPane scrollPane;
    private final JLabel statusLabel;
    private final JLabel languageLabel;

    private final StyledDocument doc;
    private final TerminalColorScheme terminalColorScheme;
    private final Style defaultStyle;
    private final Style autocompleteStyle;

    private final String fontPath = "fonts/CascadiaMono-Regular.otf";
    private final String iconPath = "/icons/terminal_icon.png";
    private final int maxOutputLength = 50000;

    private Font font = new Font("Courier New", Font.PLAIN, 12);
    private String prompt = "";
    private BBParseResult bbPrompt;
    private int insertPos = 0;
    private int promptEnd = 0;
    private int textPaneZoom = 100;
    private int errorCount = 0;
    private int warningCount = 0;
    private boolean awaitingInput = true;
    private boolean awaitingAutocompleteUpdate = true;
    private boolean awaitingHistoryUpdate = true;

    /**
     * Allocates a new {@code SwingTerminalUI} object by passing in a title, {@linkplain Dimension},
     * {@linkplain Autocomplete} as well as a {@linkplain TerminalColorScheme}.
     *
     * @param title        the title of the terminal window.
     * @param size         the size of the terminal window.
     * @param autocomplete the {@code Autocomplete} used when completing commands.
     * @param colorScheme  the {@code TerminalColorScheme} of this {@code TerminalUI}.
     * @throws NullPointerException if any of the specified arguments is {@code null}.
     */
    public SwingTerminalUI(String title, Dimension size, Autocomplete autocomplete, TerminalColorScheme colorScheme) {
        super(Objects.requireNonNull(title, "title must not be null"));

        // Initializing Autocomplete
        this.autocomplete = Objects.requireNonNull(autocomplete, "autocomplete must not be null");

        // Referencing consoleColorScheme
        this.terminalColorScheme = Objects.requireNonNull(colorScheme, "colorScheme must not be null");

        // Setting up the frame
        setLayout(new BorderLayout());
        setMinimumSize(Objects.requireNonNull(size, "size must not be null"));
//        setLocationRelativeTo(null); // Center the frame
        setLocation(50, 50);

        // Loading the font
        try {
            InputStream fontStream = getClass().getClassLoader().getResourceAsStream(fontPath);
            Objects.requireNonNull(fontStream, "could not load terminal font");
            font = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(Font.PLAIN, 15);
        } catch (NullPointerException | IllegalArgumentException | FontFormatException | IOException e) {
            logger.error("Could not load terminal font.", e);
        }

        // Setting the icon
        try {
            URL iconURL = getClass().getResource(iconPath);
            Objects.requireNonNull(iconURL, "could not load terminal icon");
            setIconImage(new ImageIcon(ImageIO.read(iconURL)).getImage());
        } catch (NullPointerException | IllegalArgumentException | IOException e) {
            logger.error("Could not load the icon image.", e);
        }

        // Instantiating bbParser
        this.bbParser = new SimpleBBParser(new SimpleBBTokenizer('[', ']', '/'));

        // Parsing the prompt text
        bbPrompt = bbParser.parse(prompt);

        // Creating the text area
        textPane = new JTextPane();
        textPane.getCaret().setBlinkRate(500);
        textPane.setForeground(terminalColorScheme.getColor(TerminalColor.FOREGROUND));
        textPane.setBackground(terminalColorScheme.getColor(TerminalColor.BACKGROUND));
        textPane.setCaretColor(terminalColorScheme.getColor(TerminalColor.FOREGROUND));
        textPane.setFont(font);
        textPane.setSelectionColor(terminalColorScheme.getColor(TerminalColor.SELECTION));
        textPane.setSelectedTextColor(Color.BLACK);
        textPane.setEditable(true);

        textPane.setBorder(new EmptyBorder(0, 3, 0, 3));

        // Referencing the document
        doc = textPane.getStyledDocument();
        ((AbstractDocument) doc).setDocumentFilter(new Filter());

        // Mapping CTRL+A
        textPane.getInputMap(JComponent.WHEN_FOCUSED)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK), new AbstractAction() {
                    public void actionPerformed(ActionEvent e) {
                        textPane.select(promptEnd, doc.getLength());
                    }
                });

        // Mapping ENTER
        textPane.getInputMap(JComponent.WHEN_FOCUSED)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent arg0) {
                        submit();
                    }
                });

        // Mapping TAB
        textPane.getInputMap(JComponent.WHEN_FOCUSED)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0), new AbstractAction() {
                    @Override
                    public synchronized void actionPerformed(ActionEvent e) {
                        if (!awaitingInput) {
                            return;
                        }

                        if (awaitingAutocompleteUpdate) {
                            try {
                                autocomplete.setInput(doc.getText(promptEnd, doc.getLength() - promptEnd));
                            } catch (BadLocationException ex) {
                                logger.error("Bad location exception when trying to retrieve input.");
                            }
                        }
                        try {
                            doc.remove(promptEnd, doc.getLength() - promptEnd);
                            doc.insertString(promptEnd, autocomplete.getNextCompletion(), autocompleteStyle);
                            awaitingAutocompleteUpdate = false;
                            awaitingHistoryUpdate = true;
                        } catch (BadLocationException ex) {
                            logger.error("Bad location exception when trying to autocomplete input.");
                        }

                        textPane.setCaretPosition(doc.getLength());
                    }
                });

        // Mapping TAB+SHIFT
        textPane.getInputMap(JComponent.WHEN_FOCUSED)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, InputEvent.SHIFT_DOWN_MASK), new AbstractAction() {
                    @Override
                    public synchronized void actionPerformed(ActionEvent e) {
                        if (!awaitingInput) {
                            return;
                        }

                        if (awaitingAutocompleteUpdate) {
                            try {
                                autocomplete.setInput(doc.getText(promptEnd, doc.getLength() - promptEnd));
                            } catch (BadLocationException ex) {
                                logger.error("Bad location exception when trying to retrieve input.");
                            }
                        }
                        try {
                            doc.remove(promptEnd, doc.getLength() - promptEnd);
                            doc.insertString(promptEnd, autocomplete.getPreviousCompletion(), autocompleteStyle);
                            awaitingAutocompleteUpdate = false;
                            awaitingHistoryUpdate = true;
                        } catch (BadLocationException ex) {
                            logger.error("Bad location exception when trying to autocomplete input.");
                        }

                        textPane.setCaretPosition(doc.getLength());
                    }
                });

        // Mapping UP
        textPane.getInputMap(JComponent.WHEN_FOCUSED)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), new AbstractAction() {
                    @Override
                    public synchronized void actionPerformed(ActionEvent e) {
                        if (!awaitingInput) {
                            return;
                        }

                        if (awaitingHistoryUpdate) {
                            try {
                                autocomplete.prepareHistory(doc.getText(promptEnd, doc.getLength() - promptEnd));
                            } catch (BadLocationException ex) {
                                logger.error("Bad location exception when trying to retrieve input.");
                            }
                        }
                        try {
                            doc.remove(promptEnd, doc.getLength() - promptEnd);
                            doc.insertString(promptEnd, autocomplete.getNextHistoryEntry(), autocompleteStyle);
                            awaitingHistoryUpdate = false;
                            awaitingAutocompleteUpdate = true;
                        } catch (BadLocationException ex) {
                            logger.error("Bad location exception when trying to autocomplete input.");
                        }

                        textPane.setCaretPosition(doc.getLength());
                    }
                });

        // Mapping DOWN
        textPane.getInputMap(JComponent.WHEN_FOCUSED)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), new AbstractAction() {
                    @Override
                    public synchronized void actionPerformed(ActionEvent e) {
                        if (!awaitingInput) {
                            return;
                        }

                        if (awaitingHistoryUpdate) {
                            try {
                                autocomplete.prepareHistory(doc.getText(promptEnd, doc.getLength() - promptEnd));
                            } catch (BadLocationException ex) {
                                logger.error("Bad location exception when trying to retrieve input.");
                            }
                        }
                        try {
                            doc.remove(promptEnd, doc.getLength() - promptEnd);
                            doc.insertString(promptEnd, autocomplete.getPreviousHistoryEntry(), autocompleteStyle);
                            awaitingHistoryUpdate = false;
                            awaitingAutocompleteUpdate = true;
                        } catch (BadLocationException ex) {
                            logger.error("Bad location exception when trying to autocomplete input.");
                        }

                        textPane.setCaretPosition(doc.getLength());
                    }
                });

        // Mapping HOME
        textPane.getInputMap(JComponent.WHEN_FOCUSED)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_HOME, 0), new AbstractAction() {
                    @Override
                    public synchronized void actionPerformed(ActionEvent e) {
                        textPane.setCaretPosition(promptEnd);
                    }
                });

        // Adding text area to scroll pane
        scrollPane = new JScrollPane(textPane);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollPane.setBackground(terminalColorScheme.getColor(TerminalColor.BACKGROUND));
        scrollPane.setBorder(new EmptyBorder(4, 4, 4, 4));
        scrollPane.getVerticalScrollBar().setUnitIncrement(3 * textPane.getFontMetrics(font).getHeight());
        scrollPane.getVerticalScrollBar().setBlockIncrement(3 * textPane.getFontMetrics(font).getHeight());
        add(scrollPane, BorderLayout.CENTER);

        // Adding a MouseWheelListener
        MouseWheelListener defaultMWheelListener = scrollPane.getMouseWheelListeners()[0];
        scrollPane.removeMouseWheelListener(defaultMWheelListener);

        // mapping CTRL+MWHEELUP / CTRL+MWHEELDOWN
        scrollPane.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.isControlDown()) {
                    float newFontSize = font.getSize() + (-1) * (int) e.getPreciseWheelRotation();
                    if (newFontSize > 2 && newFontSize < 33) {
                        font = font.deriveFont(newFontSize);
                        textPane.setFont(font);
                        textPaneZoom -= (int) e.getPreciseWheelRotation() * 10;
                        languageLabel.setText(Engine.NAME + " (v." + Engine.VERSION + ")  " + "Zoom: " + (textPaneZoom < 100 ? " " : "") + textPaneZoom
                                + "%  " + System.getProperty("file.encoding") + "  "
                                + InputContext.getInstance().getLocale().getLanguage().toUpperCase() + " ");

                        scrollPane.getVerticalScrollBar().setUnitIncrement(3 * textPane.getFontMetrics(font).getHeight());
                        scrollPane.getVerticalScrollBar().setBlockIncrement(3 * textPane.getFontMetrics(font).getHeight());
                    }
                } else {
                    defaultMWheelListener.mouseWheelMoved(e);
                }
            }
        });

        // Initializing bottom panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(BorderFactory.createEmptyBorder());
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.setBackground(terminalColorScheme.getColor(TerminalColor.BACKGROUND).darker());
        add(bottomPanel, BorderLayout.SOUTH);

        // Initializing status label
        statusLabel = new JLabel("lines: 123  errors: 2  warnings: 2");
        statusLabel.setFont(font);
        statusLabel.setForeground(terminalColorScheme.getColor(TerminalColor.FOREGROUND).darker());
        statusLabel.setOpaque(false);
        bottomPanel.add(statusLabel, BorderLayout.WEST);

        // Creating the language label
        InputContext context = InputContext.getInstance();
        languageLabel = new JLabel(Engine.NAME + " (v." + Engine.VERSION + ")  " + "Zoom: " + (textPaneZoom < 100 ? " " : "") + textPaneZoom
                + "%  " + System.getProperty("file.encoding") + "  "
                + InputContext.getInstance().getLocale().getLanguage().toUpperCase() + " ");
        languageLabel.setFont(font);
        languageLabel.setForeground(terminalColorScheme.getColor(TerminalColor.FOREGROUND).darker());
        languageLabel.setOpaque(false);
        languageLabel.setToolTipText(context.getLocale().getDisplayName());
        bottomPanel.add(languageLabel, BorderLayout.EAST);

        // Initializing default style
        defaultStyle = textPane.addStyle("Plain", null);
        StyleConstants.setForeground(defaultStyle, terminalColorScheme.getColor(TerminalColor.FOREGROUND));

        // Initializing autocomplete style
        autocompleteStyle = textPane.addStyle("Autocomplete", null);
        StyleConstants.setForeground(autocompleteStyle, terminalColorScheme.getColor(TerminalColor.FOREGROUND));
    }

    @Override
    public TerminalUI append(String str) {
        print(str);
        return this;
    }

    @Override
    public synchronized void print(String str) {
        if (!isVisible()) {
            printCall(str);
        } else {
            SwingUtilities.invokeLater(() -> printCall(str));
        }
    }

    /**
     * Helper method used to print the passed in text to the terminal.
     *
     * @param str the text to print.
     */
    private synchronized void printCall(String str) {
        Collection<BBChunk> bbChunks = bbParser.parse(str).tokens();
        try {
            for (BBChunk chunk : bbChunks) {
                doc.insertString(insertPos, chunk.content(), getStyleForBBChunk(defaultStyle, chunk));
                insertPos += chunk.content().length();
                promptEnd += chunk.content().length();
            }

            if (textPane.getCaretPosition() < promptEnd) {
                textPane.setCaretPosition(doc.getLength());
            }

            if (insertPos > maxOutputLength) {
                int trimLength = insertPos - maxOutputLength;
                int newPromptEnd = promptEnd - trimLength;
                promptEnd = 0;
                doc.remove(0, trimLength);
                promptEnd = newPromptEnd;
                insertPos -= trimLength;
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        updateStatusBar();
    }

    @Override
    public void println(String str) {
        print(str + "\n");
    }

    @Override
    public void println() {
        println("");
    }

    @Override
    public void error(String str) {
        println("[color=red]" + str + "[/color]");
        errorCount++;
    }

    @Override
    public void warning(String str) {
        println("[color=yellow]" + str + "[/color]");
        warningCount++;
    }

    @Override
    public synchronized void setPrompt(String prompt) {
        try {
            BBParseResult newbbPrompt = bbParser.parse(prompt);
            if (!awaitingInput) {
                this.prompt = prompt;
                bbPrompt = newbbPrompt;
                return;
            }

            int wishCaretPos = textPane.getCaretPosition() - bbPrompt.clearText().length() + newbbPrompt.clearText().length();
            promptEnd = insertPos;
            doc.remove(insertPos, bbPrompt.clearText().length());
            // print prompt
//            doc.insertString(insertPos, prompt, defaultStyle);
            int promptOffset = 0;
            for (BBChunk chunk : newbbPrompt.tokens()) {
                doc.insertString(insertPos + promptOffset, chunk.content(), getStyleForBBChunk(defaultStyle, chunk));
                promptOffset += chunk.content().length();
            }

            promptEnd = insertPos + newbbPrompt.clearText().length();
            this.prompt = prompt;
            bbPrompt = newbbPrompt;
            textPane.setCaretPosition(wishCaretPos);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper method used to print the prompt.
     */
    private synchronized void printPrompt() {
        try {
            insertPos = doc.getLength();
            promptEnd = doc.getLength() + bbPrompt.clearText().length();
            int promptOffset = 0;
            for (BBChunk chunk : bbPrompt.tokens()) {
                doc.insertString(insertPos + promptOffset, chunk.content(), getStyleForBBChunk(defaultStyle, chunk));
                promptOffset += chunk.content().length();
            }

            textPane.setCaretPosition(doc.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        updateStatusBar();
    }

    @Override
    public synchronized void clear() {
        try {
            promptEnd = 0;
            doc.remove(0, doc.getLength());
            if (awaitingInput) {
                printPrompt();
            }
            errorCount = 0;
            warningCount = 0;
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        updateStatusBar();
    }

    @Override
    public boolean isVisible() {
        return super.isVisible();
    }

    @Override
    public void addInputConsumer(Consumer<String> inputConsumer) {
        inputConsumerSet.add(inputConsumer);
    }

    @Override
    public void removeInputLConsumer(Consumer<String> inputConsumer) {
        inputConsumerSet.remove(inputConsumer);
    }

    @Override
    public void setVisible(boolean show) {
        SwingUtilities.invokeLater(() -> {
            super.setVisible(show);
            if (!show) {
                dispose();
            }
            textPane.setCaretPosition(insertPos);
            textPane.setCaretPosition(promptEnd);
        });
    }

    @Override
    public void setInputable(boolean inputable) {
        if (inputable) {
            textPane.setCaretColor(textPane.getForeground());
        } else {
            textPane.setCaretColor(textPane.getBackground());
        }
        textPane.setEditable(inputable);
    }

    @Override
    public boolean isInputable() {
        return textPane.isEditable();
    }

    @Override
    public void awaitInput() {
        if (!awaitingInput) {
            printPrompt();
            awaitingInput = true;
        }
    }

    /**
     * Helper method used to submit the input.
     */
    private synchronized void submit() {
        if (!isInputable() || !awaitingInput) {
            return;
        }

        try {
            awaitingInput = false;
            awaitingAutocompleteUpdate = true;
            awaitingHistoryUpdate = true;
            String input = textPane.getText(promptEnd, doc.getLength() - promptEnd);
            autocomplete.addHistoryEntry(input);
            insertPos = doc.getLength();
            promptEnd = doc.getLength();
            doc.insertString(insertPos, "\n", defaultStyle);
            textPane.setCaretPosition(insertPos);
            insertPos++;
            promptEnd++;

            inputConsumerSet.forEach(stringConsumer -> stringConsumer.accept(input));
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        updateStatusBar();
    }

    /**
     * Helper method used to update the status bar.
     */
    private void updateStatusBar() {
        statusLabel.setText(" Lines: " + doc.getDefaultRootElement().getElementCount() + "  Errors: " + errorCount + "  Warnings: " + warningCount);
    }

    /**
     * Helper method used to return a matching {@linkplain Style} for a passed in {@linkplain BBChunk}.
     *
     * @param style the {@code Style} to set.
     * @param chunk the {@code BBChunk} to set the {@code Style} by.
     * @return a {@code Style} matching the data from the specified {@code BBChunk}.
     */
    private Style getStyleForBBChunk(Style style, BBChunk chunk) {
        StyleConstants.setBold(style, chunk.properties().containsKey("b"));
        StyleConstants.setItalic(style, chunk.properties().containsKey("i"));
        StyleConstants.setStrikeThrough(style, chunk.properties().containsKey("s"));
        StyleConstants.setUnderline(style, chunk.properties().containsKey("u"));

        TerminalColor foregroundColor, backgroundColor;

        try {
            foregroundColor = Enum.valueOf(TerminalColor.class, chunk.properties().get("color").toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            foregroundColor = TerminalColor.FOREGROUND;
        }

        try {
            backgroundColor = Enum.valueOf(TerminalColor.class, chunk.properties().get("background").toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            backgroundColor = TerminalColor.BACKGROUND;
        }

        StyleConstants.setForeground(style, terminalColorScheme.getColor(foregroundColor));
        StyleConstants.setBackground(style, terminalColorScheme.getColor(backgroundColor));

        return style;
    }

    /**
     * A {@linkplain DocumentFilter} used to achieve the partial edit ability of the terminal window.
     */
    private class Filter extends DocumentFilter {
        public void insertString(final DocumentFilter.FilterBypass fb, final int offset, final String string, final AttributeSet attr)
                throws BadLocationException {
            super.insertString(fb, offset, string, attr);
        }

        public void remove(final FilterBypass fb, final int offset, final int length) throws BadLocationException {
            if (offset >= promptEnd) {
                super.remove(fb, offset, length);
                if (!awaitingAutocompleteUpdate) {
                    awaitingAutocompleteUpdate = true;
                }
                if (!awaitingHistoryUpdate) {
                    awaitingHistoryUpdate = true;
                }
                return;
            }
            if ((length - promptEnd + offset) > 0) {
                super.remove(fb, promptEnd, length - promptEnd + offset);
                textPane.setCaretPosition(promptEnd);
                if (!awaitingAutocompleteUpdate) {
                    awaitingAutocompleteUpdate = true;
                }
                if (!awaitingHistoryUpdate) {
                    awaitingHistoryUpdate = true;
                }
                return;
            }
            Toolkit.getDefaultToolkit().beep();
        }

        public void replace(final FilterBypass fb, int offset, final int length, final String text, final AttributeSet attrs)
                throws BadLocationException {
            if (!awaitingAutocompleteUpdate) {
                awaitingAutocompleteUpdate = true;
            }
            if (!awaitingHistoryUpdate) {
                awaitingHistoryUpdate = true;
            }
            if (!awaitingInput) {
                return;
            }
            if (offset < promptEnd) {
                textPane.setCaretPosition(doc.getLength());
                offset = doc.getLength();
            }
            super.replace(fb, offset, length, text, null);
        }
    }
}