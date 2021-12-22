package org.goodiemania.j4nzcp.exception;

import org.goodiemania.j4nzcp.InvalidPassException;

public class InvalidKeyException extends InvalidPassException {

    public InvalidKeyException(final String message) {
        super(message);
    }
}
