package org.goodiemania.j4nzcp.exception;

import org.goodiemania.j4nzcp.Nzcp4JException;

public class BadSignatureException extends Nzcp4JException {
    public BadSignatureException() {
        super("Signature did not match");
    }
}
