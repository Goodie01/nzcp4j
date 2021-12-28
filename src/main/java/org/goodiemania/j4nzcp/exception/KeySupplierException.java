package org.goodiemania.j4nzcp.exception;

import org.goodiemania.j4nzcp.Nzcp4jInternalException;

public class KeySupplierException extends Nzcp4jInternalException {
    public KeySupplierException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public KeySupplierException(final Throwable cause) {
        super(cause);
    }

    public KeySupplierException(final String message) {
        super(message);
    }
}
