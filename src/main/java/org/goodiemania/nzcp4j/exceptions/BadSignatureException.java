package org.goodiemania.nzcp4j.exceptions;

public class BadSignatureException extends InvalidPassException {
    public BadSignatureException() {
        super("Signature did not match");
    }

    public BadSignatureException(Exception e) {
        super(e);
    }
}
