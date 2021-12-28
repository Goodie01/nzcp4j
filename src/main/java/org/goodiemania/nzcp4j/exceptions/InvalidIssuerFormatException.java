package org.goodiemania.nzcp4j.exceptions;

public class InvalidIssuerFormatException extends InvalidPassException {
    public InvalidIssuerFormatException(final String givenIss) {
        super("Invalid issuer provided as part of payload: " + givenIss);
    }
}
