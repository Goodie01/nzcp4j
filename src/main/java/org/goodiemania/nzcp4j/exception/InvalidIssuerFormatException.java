package org.goodiemania.nzcp4j.exception;

import org.goodiemania.nzcp4j.InvalidPassException;

public class InvalidIssuerFormatException extends InvalidPassException {
    public InvalidIssuerFormatException(final String givenIss) {
        super("Invalid issuer provided as part of payload: " + givenIss);
    }
}
