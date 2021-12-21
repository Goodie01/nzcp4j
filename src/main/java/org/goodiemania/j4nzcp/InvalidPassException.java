package org.goodiemania.j4nzcp;

/**
 * This exception is the root exception for everything related to an invalid pass
 *
 * eg bad expiry date, bad issuer, etc
 */
public class InvalidPassException extends Nzcp4JException{
    public InvalidPassException(String message, String url) {
        super(message, url);
    }

    public InvalidPassException(String message, String url, Throwable cause) {
        super(message, url, cause);
    }

    public InvalidPassException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidPassException(String message) {
        super(message);
    }
}
