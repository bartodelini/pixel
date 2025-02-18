package com.bartodelini.pixel.environment;

import java.util.*;
import java.util.stream.Collectors;

/**
 * An <i>EnvironmentManager</i> is a container for {@linkplain Environment Environments}.
 *
 * @author Bartolini
 * @version 1.0
 */
public class EnvironmentManager {

    private final Map<String, Environment> environmentMap = new HashMap<>();

    /**
     * Adds the specified {@linkplain Environment} to this {@code EnvironmentManager}. More formally adds the specified
     * {@code Environment} to the {@linkplain Map} of {@code Environments} stored by this {@code EnvironmentManager}.
     *
     * @param environment the {@code Environment} to be added to this {@code EnvironmentManager}.
     * @throws NullPointerException     if the specified {@code Environment} is {@code null}.
     * @throws IllegalArgumentException if an {@code Environment} with the same prefix as the specified
     *                                  {@code Environment} is already present.
     */
    public void addEnvironment(Environment environment) {
        Objects.requireNonNull(environment, "environment must not be null");
        if (environmentMap.containsKey(environment.getPrefix())) {
            throw new IllegalArgumentException("an Environment with the same prefix as the specified Environment is " +
                    "already present");
        }
        environmentMap.put(environment.getPrefix(), environment);
    }

    /**
     * Removes the specified {@linkplain Environment} from this {@code EnvironmentManager}. More formally removes the
     * specified {@code Environment} from the {@linkplain Map} of {@code Environments} stored by this
     * {@code EnvironmentManager}.
     *
     * @param environment the {@code Environment} to be removed from this {@code EnvironmentManger}.
     * @throws NullPointerException if the specified {@code Environment} is {@code null}.
     */
    public void removeEnvironment(Environment environment) {
        Objects.requireNonNull(environment, "environment must not be null");
        environmentMap.remove(environment.getPrefix());
    }

    /**
     * Returns all {@linkplain Environment Environments} containing a {@linkplain Variable} with the specified name.
     *
     * @param name the name of the {@code Variable} which the returned {@code Environments} must contain.
     * @return a collection of all {@code Environments} containing a {@code Variable} with the specified name.
     * @throws NullPointerException if the specified name is {@code null}.
     */
    public Collection<Environment> getEnvironmentsContainingVariable(String name) {
        Objects.requireNonNull(name, "name must not be null");
        return environmentMap.values().stream().filter(e -> e.containsVariable(name)).collect(Collectors.toSet());
    }

    /**
     * Returns the first occurrence of a {@linkplain Variable} with the specified name. Returns {@code null} if no match
     * was found.
     *
     * @param name the name of the {@code Variable} to be returned.
     * @return the {@code Variable} associated with the specified name, or {@code null} if there was no mapping for such
     * name.
     * @throws NullPointerException if the specified name is {@code null}.
     */
    public Variable<?> getFirstMatchVariable(String name) {
        Objects.requireNonNull(name, "name must not be null");
        for (Environment environment : environmentMap.values()) {
            if (environment.containsVariable(name)) {
                return environment.getVariable(name);
            }
        }
        return null;
    }

    /**
     * Returns the {@linkplain Variable} with the specified name, found in the {@linkplain Environment} with the
     * specified prefix.
     *
     * @param prefix the prefix of the {@code IEnvironment} in which to search for the {@code Variable}.
     * @param name   the name of the {@code Variable}.
     * @return the {@code Variable} associated with the specified name and found in an {@code IEnvironment} with the
     * specified prefix, or {@code null} if no {@code Variable} with the specified name was found.
     * @throws NullPointerException  if the specified prefix or name is {@code null}.
     * @throws IllegalStateException if an {@linkplain Environment} with the specified prefix does not exist.
     */
    public Variable<?> getVariable(String prefix, String name) {
        Objects.requireNonNull(prefix, "prefix must not be null");
        Objects.requireNonNull(name, "name must not be null");

        if (!environmentMap.containsKey(prefix)) {
            throw new IllegalStateException("no Environment with the specified prefix was found");
        }
        return environmentMap.get(prefix).getVariable(name);
    }

    /**
     * Returns all {@linkplain Environment Environments} containing a {@linkplain Command} with the specified name.
     *
     * @param name the name of the {@code Command} which the returned {@code Environments} must contain.
     * @return a collection of all {@code Environments} containing a {@code Command} with the specified name.
     * @throws NullPointerException if the specified name is {@code null}.
     */
    public Collection<Environment> getEnvironmentsContainingCommand(String name) {
        Objects.requireNonNull(name, "name must not be null");
        return environmentMap.values().stream().filter(e -> e.containsCommand(name)).collect(Collectors.toSet());
    }

    /**
     * Returns an unmodifiable collection of all {@linkplain Environment Environments} stored by this
     * {@code EnvironmentManager}.
     *
     * @return an unmodifiable collection of all {@code Environments} stored by this {@code EnvironmentManager}.
     */
    public Collection<Environment> getAllEnvironments() {
        return Collections.unmodifiableCollection(environmentMap.values());
    }
}