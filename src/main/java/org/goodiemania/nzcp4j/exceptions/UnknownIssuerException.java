package org.goodiemania.nzcp4j.exceptions;

public class UnknownIssuerException extends InvalidPassException {
    public UnknownIssuerException(final String iss) {
        super("Unknown ISS: " + iss);
    }
}
