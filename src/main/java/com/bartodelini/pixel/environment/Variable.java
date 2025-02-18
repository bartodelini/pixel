package com.bartodelini.pixel.environment;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

/**
 * A <i>Variable</i> is used to store a {@linkplain Comparable} value, which can be altered and queried through
 * convenience methods. Also provides a way of listening to the changes made to the value.
 *
 * @author Bartolini
 * @version 1.0
 */
public class Variable<T extends Comparable<T>> implements Comparable<Variable<?>> {

    /**
     * The values of {@code Variables} marked as {@code CHEAT} can only be altered when cheats are enabled.
     */
    public static final int CHEAT = 1;

    /**
     * The changes to {@code Variables} marked as {@code NOTIFY} should be displayed to the user.
     */
    public static final int NOTIFY = 1 << 1;

    private final String name;
    private final String description;
    private final T defaultValue;
    private final T minVal, maxVal;
    private final boolean hasMin, hasMax;
    private final int flags;
    private final Set<Consumer<T>> changeHookSet = new HashSet<>();

    private T value;

    /**
     * Allocates a new {@code Variable} object by passing in its name, value, minimum and maximum constraints,
     * as well as flags and its description.
     *
     * @param name        the name of this {@code Variable}.
     * @param value       the value of this {@code Variable}.
     * @param hasMin      the boolean determining whether this {@code Variable} has a minimum.
     * @param minVal      the minimum value of this {@code Variable}.
     * @param hasMax      the boolean determining whether this {@code Variable} has a maximum.
     * @param maxVal      the maximum value of this {@code Variable}.
     * @param flags       the flags to mark this {@code Variable} with.
     * @param description the description of this {@code Variable}.
     * @throws NullPointerException if the specified name, value or description are {@code null}.
     */
    public Variable(String name, T value, boolean hasMin, T minVal, boolean hasMax, T maxVal, int flags,
                    String description) {
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.description = Objects.requireNonNull(description, "description must not be null");
        this.value = Objects.requireNonNull(value, "value must not be null");
        this.defaultValue = value;
        this.hasMin = hasMin;
        this.hasMax = hasMax;
        this.minVal = minVal;
        this.maxVal = maxVal;
        this.flags = flags;
    }

    /**
     * Allocates a new {@code Variable} object by passing in its name, value, minimum and maximum constraints,
     * as well as its description. This constructor has the same effect as
     * {@linkplain #Variable(String, T, boolean, T, boolean, T, int, String) Variable}
     * {@code (name, value, hasMin, minVal, hasMax, maxVal, 0, description)}.
     *
     * @param name        the name of this {@code Variable}.
     * @param value       the value of this {@code Variable}.
     * @param hasMin      the boolean determining whether this {@code Variable} has a minimum.
     * @param minVal      the minimum value of this {@code Variable}.
     * @param hasMax      the boolean determining whether this {@code Variable} has a maximum.
     * @param maxVal      the maximum value of this {@code Variable}.
     * @param description the description of this {@code Variable}.
     * @throws NullPointerException if the specified name, value or description are {@code null}.
     */
    public Variable(String name, T value, boolean hasMin, T minVal, boolean hasMax, T maxVal, String description) {
        this(name, value, hasMin, minVal, hasMax, maxVal, 0, description);
    }

    /**
     * Allocates a new {@code Variable} object by passing in its name, value, flags and description. This constructor
     * has the same effect as {@linkplain #Variable(String, T, boolean, T, boolean, T, int, String) Variable}
     * {@code (name, value, false, null, false, null, flags, description)}.
     *
     * @param name        the name of this {@code Variable}.
     * @param value       the value of this {@code Variable}.
     * @param flags       the flags to mark this {@code Variable} with.
     * @param description the description of this {@code Variable}.
     * @throws NullPointerException if the specified name, value or description are {@code null}.
     */
    public Variable(String name, T value, int flags, String description) {
        this(name, value, false, null, false, null, flags, description);
    }

    /**
     * Allocates a new {@code Variable} object by passing in its name, value and description.
     * This constructor has the same effect as {@linkplain #Variable(String, T, boolean, T, boolean, T, String)
     * Variable} {@code (name, value, false, null, false, null, description)}.
     *
     * @param name        the name of this {@code Variable}.
     * @param value       the value of this {@code Variable}.
     * @param description the description of this {@code Variable}.
     * @throws NullPointerException if the passed name, value or description are {@code null}.
     */
    public Variable(String name, T value, String description) {
        this(name, value, false, null, false, null, description);
    }

    /**
     * Returns the name of this {@code Variable}.
     *
     * @return the name of this {@code Variable}.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the description of this {@code Variable}.
     *
     * @return the description of this {@code Variable}.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the value of this {@code Variable}.
     *
     * @return the value of this {@code Variable}.
     */
    public T getValue() {
        return value;
    }

    /**
     * Sets the value of this {@code Variable}. More formally keeps the value unchanged if one of the
     * following scenarios occurs:
     * <ul>
     * <li> the value of this {@code Variable} is the same as the specified value
     * <li> the specified value is smaller than the minimum and the value of the {@code Variable} is already set to
     * the minimum
     * <li> the specified value is bigger than the maximum and the value of the {@code Variable} is already set to
     * the maximum
     * </ul>
     * Else the value is updated, either to the passed in value itself, or clamped down to the minimum or maximum
     * value of this {@code Variable}.
     * <p>
     * Finally, if the value was altered, all registered change hooks are executed.
     *
     * @param value the new wished value of this {@code Variable}.
     * @return {@code true} if the value of this {@code Variable} was changed; {@code false} otherwise.
     * @throws NullPointerException     if the passed in value is {@code null}.
     * @throws IllegalArgumentException if the specified value is not of the type of this {@code Variable}.
     */
    public boolean setValue(T value) {
        Objects.requireNonNull(value, "value must not be null");
        if (!defaultValue.getClass().isAssignableFrom(value.getClass())) {
            throw new IllegalArgumentException("value must be of type: " + defaultValue.getClass());
        }

        if (this.value.compareTo(value) == 0 ||
                (hasMin && minVal.compareTo(this.value) == 0 && this.value.compareTo(value) > 0) ||
                (hasMax && maxVal.compareTo(this.value) == 0 && this.value.compareTo(value) < 0)) {
            return false;
        }
        if (hasMin && minVal.compareTo(value) > 0) {
            value = minVal;
        }
        if (hasMax && maxVal.compareTo(value) < 0) {
            value = maxVal;
        }
        if (!changeHookSet.isEmpty()) {
            T preVal = this.value;
            this.value = value;
            changeHookSet.forEach(changeHook -> changeHook.accept(preVal));
        } else {
            this.value = value;
        }
        return true;
    }

    /**
     * Returns the default value of this {@code Variable}. That is, the value this {@code Variable} was instantiated
     * with.
     *
     * @return the default value of this {@code Variable}.
     */
    public T getDefaultValue() {
        return defaultValue;
    }

    /**
     * Resets the value of this {@code Variable} to its default value. If the value was altered, all registered change
     * hooks are executed.
     *
     * @return {@code true} if the value of this {@code Variable} was changed; {@code false} otherwise.
     */
    public boolean resetValue() {
        return setValue(getDefaultValue());
    }

    /**
     * Returns the minimum value if this {@code Variable}.
     *
     * @return the minimum value if this {@code Variable}.
     */
    public T getMin() {
        return minVal;
    }

    /**
     * Returns the maximum value if this {@code Variable}.
     *
     * @return the maximum value if this {@code Variable}.
     */
    public T getMax() {
        return maxVal;
    }

    /**
     * Returns whether this {@code Variable} has a maximum value.
     *
     * @return {@code true} is this {@code Variable} has a maximum value, {@code false} otherwise.
     */
    public boolean hasMin() {
        return hasMin;
    }

    /**
     * Returns whether this {@code Variable} has a maximum value.
     *
     * @return {@code true} is this {@code Variable} has a maximum value, {@code false} otherwise.
     */
    public boolean hasMax() {
        return hasMax;
    }

    /**
     * Returns the flags of this {@code Variable}.
     *
     * @return the flags of this {@code Variable}.
     */
    public int getFlags() {
        return flags;
    }

    /**
     * Returns whether this {@code Variable} is marked with the specified flags.
     *
     * @param flags the flags whose markings are to be checked.
     * @return {@code true} is this {@code Variable} is marked with all the specified flags; {@code false} otherwise.
     */
    public boolean hasFlags(int flags) {
        return (this.flags & flags) != 0;
    }

    /**
     * Returns the {@linkplain Class} representing the type of the value stored by this {@code Variable}.
     *
     * @return the {@code Class} representing the type of the value stored by this {@code Variable}.
     */
    public Class<?> getType() {
        return defaultValue.getClass();
    }

    /**
     * Adds a {@linkplain Consumer changeHook} to this {@code Variable}.
     *
     * @param changeHook the {@code Consumer} to be added as a change hook to this {@code Variable}.
     * @throws NullPointerException if the specified {@code Consumer} is {@code null}.
     */
    public void addChangeHook(Consumer<T> changeHook) {
        Objects.requireNonNull(changeHook, "changeHook must not be null");
        changeHookSet.add(changeHook);
    }

    /**
     * Removes a {@linkplain Consumer changeHook} from this {@code Variable}.
     *
     * @param changeHook the {@code Consumer} to be removed from this {@code Variable}.
     * @throws NullPointerException if the specified {@code Consumer} is {@code null}.
     */
    public void removeChangeHook(Consumer<T> changeHook) {
        Objects.requireNonNull(changeHook, "changeHook must not be null");
        changeHookSet.remove(changeHook);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Variable<?> variable)) return false;
        return getName().equals(variable.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    @Override
    public int compareTo(Variable<?> o) {
        return this.name.compareTo(o.getName());
    }
}