package com.bartodelini.pixel.core;

import java.util.Objects;

/**
 * A <i>ModuleInitializeException</i> is thrown by an {@linkplain EngineModule} during its initialization stage if
 * something goes wrong.
 *
 * @author Bartolini
 * @version 1.0
 */
public class ModuleInitializeException extends Exception {

    private final EngineModule engineModule;

    /**
     * Allocates a new {@code ModuleInitializeException} by passing in its {@linkplain EngineModule}, message and the
     * {@linkplain Throwable} which caused it.
     *
     * @param module  the {@code EngineModule} in which the exception occurred.
     * @param message the message of the exception.
     * @param cause   the cause of the exception.
     * @throws NullPointerException if the specified {@code EngineModule} is {@code null}.
     */
    public ModuleInitializeException(EngineModule module, String message, Throwable cause) {
        super(message, cause);
        this.engineModule = Objects.requireNonNull(module, "module must not be null");
    }

    /**
     * Allocates a new {@code ModuleInitializeException} by passing in its {@linkplain EngineModule} and message.
     *
     * @param module  the {@code EngineModule} in which the exception occurred.
     * @param message the message of the exception.
     * @throws NullPointerException if the specified {@code EngineModule} is {@code null}.
     */
    public ModuleInitializeException(EngineModule module, String message) {
        super(message);
        this.engineModule = Objects.requireNonNull(module, "module must not be null");
    }

    /**
     * Allocates a new {@code ModuleInitializeException} by passing in its {@linkplain EngineModule}.
     *
     * @param module the {@code EngineModule} in which the exception occurred.
     * @throws NullPointerException if the specified {@code EngineModule} is {@code null}.
     */
    public ModuleInitializeException(EngineModule module) {
        this.engineModule = module;
    }

    /**
     * Returns the {@linkplain EngineModule} which caused the exception.
     *
     * @return the {@code EngineModule} which caused the exception.
     */
    public EngineModule getEngineModule() {
        return engineModule;
    }
}