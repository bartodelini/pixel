package com.bartodelini.pixel.environment;

import java.util.*;

/**
 * An <i>Environment</i> is a container for {@linkplain Variable Variables} and {@linkplain Command Commands}.
 *
 * @author Bartolini
 * @version 1.0
 */
public class Environment implements Comparable<Environment> {

    private final String prefix;
    private final Map<String, Variable<?>> variableMap = new HashMap<>();
    private final Map<String, Command> commandMap = new HashMap<>();

    /**
     * Allocates a new {@code Environment} object by passing its prefix.
     * The prefix can be later used to resolve name conflicts of {@linkplain Variable Variables} and
     * {@linkplain Command Commands} of the same name in different {@code Environments}.
     *
     * @param prefix the prefix used for this {@code Environment}.
     * @throws NullPointerException if the specified prefix is {@code null}.
     */
    public Environment(String prefix) {
        this.prefix = Objects.requireNonNull(prefix, "prefix must not be null");
    }

    /**
     * Returns the prefix of this {@code Environment}.
     *
     * @return the prefix of this {@code Environment}.
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Adds a {@linkplain Variable} in this {@code Environment}. More formally adds the passed in {@code Variable} to
     * the {@linkplain HashMap} of this {@code Environment} used to store {@code Variables}.
     *
     * @param variable the {@code Variable} to be registered in this {@code Environment}.
     * @throws NullPointerException if the specified {@code Variable} is {@code null}.
     */
    public void addVariable(Variable<?> variable) {
        Objects.requireNonNull(variable, "variable must not be null");
        variableMap.put(variable.getName(), variable);
    }

    /**
     * Returns the {@linkplain Variable} associated with the specified name.
     *
     * @param name the name of the {@code Variable} to be returned.
     * @return the {@code Variable} associated with the specified name, or {@code null} if there was no mapping for such
     * name.
     * @throws NullPointerException if the specified name is {@code null}.
     */
    public Variable<?> getVariable(String name) {
        Objects.requireNonNull(name, "name must not be null");
        return variableMap.get(name);
    }

    /**
     * Removes the {@linkplain Variable} associated with the specified name from this {@code Environment}. More
     * formally removes the {@code Variable} from the {@linkplain HashMap} of this {@code Environment} used to store
     * {@code Variables}.
     *
     * @param name the name of the {@code Variable} to be removed.
     * @throws NullPointerException if the specified name is {@code null}.
     */
    public void removeVariable(String name) {
        Objects.requireNonNull(name, "name must not be null");
        variableMap.remove(name);
    }

    /**
     * Returns whether this {@code Environment} contains a {@linkplain Variable} with the specified name.
     *
     * @param name the name of the {@code Variable} whose presence is to be checked.
     * @return {@code true} if this {@code Environment} contains a {@code Variable} with the specified name,
     * {@code false} otherwise.
     * @throws NullPointerException if the specified name is {@code null}.
     */
    public boolean containsVariable(String name) {
        Objects.requireNonNull(name, "name must not be null");
        return variableMap.containsKey(name);
    }

    /**
     * Returns an unmodifiable collection of all {@linkplain Variable Variables} stored in this {@code Environment}.
     *
     * @return an unmodifiable collection of all {@code Variables} stored in this {@code Environment}.
     */
    public Collection<Variable<?>> getAllVariables() {
        return Collections.unmodifiableCollection(variableMap.values());
    }

    /**
     * Adds a {@linkplain Command} in this {@code Environment}. More formally registers the passed
     * {@code Command} in the {@linkplain HashMap} of this {@code Environment} used to store {@code Commands}.
     *
     * @param command the {@code Command} to be registered in this {@code Environment}.
     * @throws NullPointerException if the specified {@code Command} is {@code null}.
     */
    public void addCommand(Command command) {
        Objects.requireNonNull(command, "command must not be null");
        commandMap.put(command.getName(), command);
    }

    /**
     * Returns the {@linkplain Command} associated with the specified name.
     *
     * @param name the name of the {@code Command} to be returned.
     * @return the {@code Command} associated with the specified name, or {@code null} if there was no mapping for such
     * name.
     * @throws NullPointerException if the specified name is {@code null}.
     */
    public Command getCommand(String name) {
        Objects.requireNonNull(name, "name must not be null");
        return commandMap.get(name);
    }

    /**
     * Removes the {@linkplain Command} associated with the specified name from this {@code Environment}. More formally
     * removes the {@code Command} from the {@linkplain HashMap} of this {@code Environment} used to store
     * {@code Commands}.
     *
     * @param name the name of the {@code Command} to be removed.
     * @throws NullPointerException if the specified name is {@code null}.
     */
    public void removeCommand(String name) {
        Objects.requireNonNull(name, "name must not be null");
        commandMap.remove(name);
    }

    /**
     * Returns whether this {@code Environment} contains a {@linkplain Command} with the specified name.
     *
     * @param name the name of the {@code Command} whose presence is to be checked.
     * @return {@code true} if this {@code Environment} contains a {@code Command} with the specified name,
     * {@code false} otherwise.
     * @throws NullPointerException if the specified name is {@code null}.
     */
    public boolean containsCommand(String name) {
        Objects.requireNonNull(name, "name must not be null");
        return commandMap.containsKey(name);
    }

    /**
     * Returns an unmodifiable collection of all {@linkplain Command Commands} stored in this {@code Environment}.
     *
     * @return an unmodifiable collection of all {@code Commands} stored in this {@code Environment}.
     */
    public Collection<Command> getAllCommands() {
        return Collections.unmodifiableCollection(commandMap.values());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Environment that = (Environment) o;
        return getPrefix().equals(that.getPrefix());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPrefix());
    }

    @Override
    public int compareTo(Environment o) {
        return this.prefix.compareTo(o.getPrefix());
    }
}