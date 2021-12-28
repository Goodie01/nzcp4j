package org.goodiemania.nzcp4j;

/**
 * This exception is the root exception for everything related to an internal issue
 * <p>
 * Eg unable to contact the identity server to do a live check of a certificate
 */
public class Nzcp4jInternalException extends Nzcp4JException {
    public Nzcp4jInternalException(String message, String url) {
        super(message, url);
    }

    public Nzcp4jInternalException(String message, String url, Throwable cause) {
        super(message, url, cause);
    }

    public Nzcp4jInternalException(String message, Throwable cause) {
        super(message, cause);
    }

    public Nzcp4jInternalException(Throwable cause) {
        super(cause);
    }

    public Nzcp4jInternalException(String message) {
        super(message);
    }
}
