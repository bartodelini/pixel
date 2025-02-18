package com.bartodelini.pixel.modules.terminal.ui;

import com.bartodelini.pixel.environment.Command;
import com.bartodelini.pixel.environment.EnvironmentManager;
import com.bartodelini.pixel.environment.Variable;

import java.util.LinkedList;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * An <i>Autocomplete</i> provides autocompletion for an input, based on the content of an
 * {@linkplain EnvironmentManager}.
 *
 * @author Bartolini
 * @version 1.1
 */
public class Autocomplete {

    private final EnvironmentManager environmentManager;
    private final LinkedList<String> inputHistory = new LinkedList<>();
    private final int maxHistorySize;

    private LinkedList<String> completions = new LinkedList<>();
    private String lastInput = "";
    private int completionIndex = 0;
    private int historyIndex = -1;

    /**
     * Allocates a new {@code Autocomplete} object by passing in an {@linkplain EnvironmentManager} and specifying the
     * number of maximum history entries.
     *
     * @param environmentManager the {@code EnvironmentManager} used by this {@code Autocomplete}.
     * @param maxHistorySize     the number of maximum history entries.
     * @throws NullPointerException if the specified {@code EnvironmentManager} is {@code null}.
     */
    public Autocomplete(EnvironmentManager environmentManager, int maxHistorySize) {
        this.environmentManager = environmentManager;
        this.maxHistorySize = maxHistorySize;
    }

    /**
     * Returns the next available completion.
     *
     * @return the next available completion.
     */
    public String getNextCompletion() {
        completionIndex = truncateIndex(++completionIndex, 0, completions.size() - 1);
        return completions.get(completionIndex);
    }

    /**
     * Returns the previous available completion.
     *
     * @return the previous available completion.
     */
    public String getPreviousCompletion() {
        completionIndex = truncateIndex(--completionIndex, 0, completions.size() - 1);
        return completions.get(completionIndex);
    }

    /**
     * Sets the input string.
     *
     * @param input the input string.
     */
    public void setInput(String input) {
        this.completionIndex = 0;
        this.completions = environmentManager.getAllEnvironments()
                .stream()
                .flatMap(env -> Stream.concat(
                        env.getAllCommands().stream().map(Command::getName),
                        env.getAllVariables().stream().map(Variable::getName)))
                .filter(name -> name.startsWith(input))
                .distinct()
                .sorted()
                .collect(Collectors.toCollection(LinkedList::new));
        this.completions.addFirst(input);
    }

    /**
     * Adds a new entry to the history.
     *
     * @param entry the new entry to be added to the history.
     * @throws NullPointerException if the specified entry is {@code null}.
     */
    public void addHistoryEntry(String entry) {
        Objects.requireNonNull(entry, "entry must not be null");
        if (inputHistory.isEmpty() || !inputHistory.getFirst().trim().equals(entry.trim())) {
            inputHistory.addFirst(entry);
            historyIndex = -1;
            truncateInputHistory();
        }
    }

    /**
     * Prepares the history for the specified input.
     *
     * @param input the input entry.
     */
    public void prepareHistory(String input) {
        lastInput = input;
        historyIndex = -1;
    }

    /**
     * Returns the next entry in the history.
     *
     * @return the next entry in the history.
     */
    public String getNextHistoryEntry() {
        historyIndex = truncateIndex(++historyIndex, -1, inputHistory.size() - 1);
        if (historyIndex == -1) {
            return lastInput;
        }
        return inputHistory.get(historyIndex);
    }

    /**
     * Returns the previous entry in the history.
     *
     * @return the previous entry in the history.
     */
    public String getPreviousHistoryEntry() {
        historyIndex = truncateIndex(--historyIndex, -1, inputHistory.size() - 1);
        if (historyIndex == -1) {
            return lastInput;
        }
        return inputHistory.get(historyIndex);
    }

    /**
     * Helper method used to truncate an index to a given range.
     *
     * @param index    the index to truncate.
     * @param minValue the minimum value for the index.
     * @param maxValue the maximum value for the index.
     * @return the index truncated in the specified range.
     */
    private int truncateIndex(int index, int minValue, int maxValue) {
        return Math.min(Math.max(index, minValue), maxValue);
    }

    /**
     * Helper method used to truncate the input history to the maximum size.
     */
    private void truncateInputHistory() {
        if (inputHistory.size() > maxHistorySize) {
            inputHistory.removeLast();
        }
    }
}