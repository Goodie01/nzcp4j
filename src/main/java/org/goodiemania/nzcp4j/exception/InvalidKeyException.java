package org.goodiemania.nzcp4j.exception;

import org.goodiemania.nzcp4j.InvalidPassException;

public class InvalidKeyException extends InvalidPassException {

    public InvalidKeyException(final String message) {
        super(message);
    }
}
