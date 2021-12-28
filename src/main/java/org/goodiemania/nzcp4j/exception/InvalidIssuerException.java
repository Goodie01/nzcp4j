package org.goodiemania.nzcp4j.exception;

import org.goodiemania.nzcp4j.InvalidPassException;

public class InvalidIssuerException extends InvalidPassException {
    public InvalidIssuerException(final String iss) {
        super("Invalid ISS detected: " + iss);
    }
}
