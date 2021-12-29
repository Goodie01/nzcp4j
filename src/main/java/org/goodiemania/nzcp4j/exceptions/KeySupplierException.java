package org.goodiemania.nzcp4j.exceptions;

/**
 * Indicates an issue specifically from within the key supplier, eg, a problem making a http connection
 */
public class KeySupplierException extends Nzcp4jInternalException {
    /**
     * Constructs a new KeySupplierException with the specified message and cause
     *
     * @param message a detailed message
     * @param cause   the cause
     */
    public KeySupplierException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new KeySupplierException with the specified cause
     *
     * @param cause the cause
     */
    public KeySupplierException(final Throwable cause) {
        super(cause);
    }


    /**
     * Constructs a new KeySupplierException with the specified message
     *
     * @param message the message describing the issue
     */
    public KeySupplierException(final String message) {
        super(message);
    }
}
