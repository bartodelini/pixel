package com.bartodelini.pixel.modules.terminal.shell;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

/**
 * A <i>ShellEnvironment</i> is used to store information of the shell execution environment, such as the current
 * username, working {@linkplain Path} or the home {@code Path}.
 *
 * @author Bartolini
 * @version 1.0
 */
public class ShellEnvironment {

    private final String username;
    private final Path homePath;
    private final Set<Consumer<ShellEnvironment>> changeHookSet = new HashSet<>();

    private Path workingPath;

    /**
     * Allocates a new {@code ShellEnvironment} object by passing in a username, a {@linkplain Path homePath} as well as
     * a {@code homePath}.
     *
     * @param username    the username of the user.
     * @param homePath    the {@code homePath} of the user.
     * @param workingPath the {@code workingPath} of the user.
     * @throws NullPointerException if any of the specified parameters are {@code null}.
     */
    public ShellEnvironment(String username, Path homePath, Path workingPath) {
        this.username = Objects.requireNonNull(username, "username must not be null");
        this.homePath = Objects.requireNonNull(homePath, "homePath must not be null");
        this.workingPath = Objects.requireNonNull(workingPath, "workingPath must not be null");
    }

    /**
     * Returns the username of the active user.
     *
     * @return the username of the active user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the current {@linkplain Path workingPath}.
     *
     * @return the current {@code workingPath}.
     */
    public Path getWorkingPath() {
        return workingPath;
    }

    /**
     * Sets the current {@linkplain Path workingPath}, if the value was altered, executes all registered change hooks.
     *
     * @param workingPath the new {@code workingPath}.
     * @throws NullPointerException if the specified {@code Path} is {@code null}.
     */
    public void setWorkingPath(Path workingPath) {
        Objects.requireNonNull(workingPath, "workingPath must not be null");
        // Return if the paths are equal
        if (this.workingPath.equals(workingPath)) {
            return;
        }
        // Update the workingPath
        this.workingPath = workingPath;

        // Notify all change hooks of the change
        changeHookSet.forEach(shellEnvironmentConsumer -> shellEnvironmentConsumer.accept(this));
    }

    /**
     * Returns the {@linkplain Path homePath}.
     *
     * @return the {@code homePath}.
     */
    public Path getHomePath() {
        return homePath;
    }

    /**
     * Adds a {@linkplain Consumer changeHook} to the {@linkplain Set} of {@code changeHooks}.
     *
     * @param changeHook the {@code changeHook} to be added.
     * @throws NullPointerException if the specified {@code changeHook} is {@code null}.
     */
    public void addChangeHook(Consumer<ShellEnvironment> changeHook) {
        Objects.requireNonNull(changeHook, "changeHook must not be null");
        changeHookSet.add(changeHook);
    }

    /**
     * Removes a {@linkplain Consumer changeHook} from the {@linkplain Set} of {@code changeHooks}.
     *
     * @param changeHook the {@code changeHook} to be removed.
     * @throws NullPointerException if the specified {@code changeHook} is {@code null}.
     */
    public void removeChangeHook(Consumer<ShellEnvironment> changeHook) {
        Objects.requireNonNull(changeHook, "changeHook must not be null");
        changeHookSet.remove(changeHook);
    }
}