package org.goodiemania.j4nzcp.exception;

import org.goodiemania.j4nzcp.InvalidPassException;
import org.goodiemania.j4nzcp.Nzcp4JException;

public class InvalidIssuerException extends InvalidPassException {
    public InvalidIssuerException(final String iss) {
        super("Unsupported ISS detected: " + iss);
    }
}
