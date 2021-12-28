package org.goodiemania.j4nzcp.exception;

import org.goodiemania.j4nzcp.InvalidPassException;

public class InvalidIssuerException extends InvalidPassException {
    public InvalidIssuerException(final String iss) {
        super("Invalid ISS detected: " + iss);
    }
}
