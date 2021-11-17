package org.goodiemania.j4nzcp.exception;

import org.goodiemania.j4nzcp.Nzcp4JException;

public class KeySupplierException extends Nzcp4JException {
    public KeySupplierException(final Exception e) {
        super("Library error encountered", e);
    }

    public KeySupplierException(final String message) {
        super(message);
    }
}
