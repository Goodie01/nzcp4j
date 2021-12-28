package org.goodiemania.nzcp4j.exception;

import org.goodiemania.nzcp4j.InvalidPassException;

public class UnknownIssuerException extends InvalidPassException {
    public UnknownIssuerException(final String iss) {
        super("Unknown ISS: " + iss);
    }
}
