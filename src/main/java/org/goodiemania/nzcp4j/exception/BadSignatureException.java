package org.goodiemania.nzcp4j.exception;

import org.goodiemania.nzcp4j.InvalidPassException;

public class BadSignatureException extends InvalidPassException {
    public BadSignatureException() {
        super("Signature did not match");
    }

    public BadSignatureException(Exception e) {
        super(e);
    }
}
