package com.bartodelini.pixel.modules.terminal.ui;

import java.util.function.Consumer;

public interface TerminalUI {

    void addInputConsumer(Consumer<String> inputConsumer);

    void removeInputLConsumer(Consumer<String> inputConsumer);

    void setVisible(boolean show);

    boolean isVisible();

    TerminalUI append(String str);

    void print(String str);

    void println(String str);

    void println();

    void error(String str);

    void warning(String str);

    void setPrompt(String prompt);

    void clear();

    void setInputable(boolean inputable);

    boolean isInputable();

    void awaitInput();
}