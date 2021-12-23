package org.goodiemania.j4nzcp.exception;

import org.goodiemania.j4nzcp.InvalidPassException;

public class UnknownIssuerException extends InvalidPassException {
    public UnknownIssuerException(final String iss) {
        super("Unknown ISS: " + iss);
    }
}
