package org.goodiemania.j4nzcp.exception;

import org.goodiemania.j4nzcp.InvalidPassException;

public class BadSignatureException extends InvalidPassException {
    public BadSignatureException() {
        super("Signature did not match");
    }

    public BadSignatureException(Exception e) {
        super(e);
    }
}
