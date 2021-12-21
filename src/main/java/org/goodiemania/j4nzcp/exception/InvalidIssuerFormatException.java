package org.goodiemania.j4nzcp.exception;

import org.goodiemania.j4nzcp.InvalidPassException;

public class InvalidIssuerFormatException extends InvalidPassException {
    public InvalidIssuerFormatException(final String givenIss) {
        super("Invalid issuer provided as part of payload: " + givenIss);
    }
}
