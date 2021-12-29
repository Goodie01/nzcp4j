package org.goodiemania.nzcp4j.exceptions;

/**
 * Indicates an issue was found while attempting to validate the signature.
 * <p>
 * Depending on if there is a wrapped exception, and what the wrapped exception is may indicate an issue with the underlying JVM and available crypto algorithms etc.
 */
public class BadSignatureException extends InvalidPassException {
    /**
     * Constructs a new BadSignatureException with a simple message
     */
    public BadSignatureException() {
        super("Signature did not match");
    }

    /**
     * Constructs a new BadSignatureException with the specified message and cause
     * @param message a detailed message
     * @param cause the cause
     */
    public BadSignatureException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new BadSignatureException with the specified cause
     * @param cause the cause
     */
    public BadSignatureException(Exception cause) {
        super(cause);
    }
}
