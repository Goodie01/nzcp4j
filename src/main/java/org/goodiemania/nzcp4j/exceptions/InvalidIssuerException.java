package org.goodiemania.nzcp4j.exceptions;

public class InvalidIssuerException extends InvalidPassException {
    public InvalidIssuerException(final String iss) {
        super("Invalid ISS detected: " + iss);
    }
}
