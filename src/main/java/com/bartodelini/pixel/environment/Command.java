package com.bartodelini.pixel.environment;

import java.util.List;
import java.util.Objects;

/**
 * A <i>Command</i> is used to define special executable behaviour.
 *
 * @author Bartolini
 * @version 1.0
 */
public abstract class Command implements Comparable<Command> {

    /**
     * The {@code Commands} marked as {@code CHEAT} can only be executed when cheats are enabled.
     */
    public static final int CHEAT = 1;

    private final String name;
    private final String description;
    private final int flags;

    /**
     * Allocates a new {@code Command} object by passing in its name, flags and description.
     *
     * @param name        the name of this {@code Command}.
     * @param flags       the flags of this {@code Command}.
     * @param description the description of this {@code Command}.
     * @throws NullPointerException if the specified name or description are {@code null}.
     */
    public Command(String name, int flags, String description) {
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.flags = flags;
        this.description = Objects.requireNonNull(description, "description must not be null");
    }

    /**
     * Allocates a new {@code Command} object by passing in its name and description.
     * This constructor has the same effect as {@linkplain #Command(String, int, String) Command}
     * {@code (name, 0, description)}.
     *
     * @param name        the name of this {@code Command}.
     * @param description the description of this {@code Command}.
     * @throws NullPointerException if the specified name or description are {@code null}.
     */
    public Command(String name, String description) {
        this(name, 0, description);
    }

    /**
     * Defines the behaviour of this {@code Command}.
     * This method has to be overridden by the subclasses of {@code Command}.
     *
     * @param stringBuilder the {@linkplain StringBuilder} used as the output of this {@code Command}.
     * @param args          the arguments passed into this {@code Command}.
     * @return the exit code of this {@code Command}.
     */
    public abstract int execute(StringBuilder stringBuilder, List<String> args);

    /**
     * Convenience method for {@linkplain #execute(StringBuilder, List) execute} {@code (stringBuilder, null)}.
     *
     * @param stringBuilder the {@linkplain StringBuilder} used as the output of this {@code Command}.
     * @return the exit code of this {@code Command}.
     */
    public int execute(StringBuilder stringBuilder) {
        return execute(stringBuilder, null);
    }

    /**
     * Returns the name of this {@code Command}.
     *
     * @return the name of this {@code Command}.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the description of this {@code Command}.
     *
     * @return the description of this {@code Command}.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the flags of this {@code Command}.
     *
     * @return the flags of this {@code Command}.
     */
    public int getFlags() {
        return flags;
    }

    /**
     * Returns whether this {@code Command} is marked with the specified flags.
     *
     * @param flags the flags whose markings are to be checked.
     * @return {@code true} is this {@code Command} is marked with all the specified flags; {@code false} otherwise.
     */
    public boolean hasFlags(int flags) {
        return (this.flags & flags) != 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Command command)) return false;
        return getName().equals(command.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    @Override
    public int compareTo(Command o) {
        return this.name.compareTo(o.getName());
    }
}