package com.bartodelini.pixel.modules.asset;

/**
 * An <i>AssetLoadingException</i> is an {@linkplain Exception} thrown whenever an asset could not be loaded.
 *
 * @author Bartolini
 * @version 1.0
 */
public class AssetLoadingException extends Exception {

    /**
     * Allocates a new {@code AssetLoadingException} by passing in a message as well as the {@linkplain Throwable}
     * representing the cause for the exception.
     *
     * @param message the message of the exception.
     * @param cause   the {@code Throwable} representing the cause of the exception.
     */
    public AssetLoadingException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Allocates a new {@code AssetLoadingException} by passing in a message.
     *
     * @param message the message of the exception.
     */
    public AssetLoadingException(String message) {
        super(message);
    }

    /**
     * Allocates a new {@code AssetLoadingException} with {@code null} as its message.
     */
    public AssetLoadingException() {
        super();
    }
}