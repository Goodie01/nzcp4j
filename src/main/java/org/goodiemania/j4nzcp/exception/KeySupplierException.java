package org.goodiemania.j4nzcp.exception;

import org.goodiemania.j4nzcp.Nzcp4JException;
import org.goodiemania.j4nzcp.Nzcp4jInternalException;

public class KeySupplierException extends Nzcp4jInternalException {
    public KeySupplierException(final Exception e) {
        super("Library error encountered", e);
    }

    public KeySupplierException(final String message) {
        super(message);
    }
}
