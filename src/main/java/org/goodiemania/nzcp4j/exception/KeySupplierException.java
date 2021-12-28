package org.goodiemania.nzcp4j.exception;

import org.goodiemania.nzcp4j.Nzcp4jInternalException;

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
