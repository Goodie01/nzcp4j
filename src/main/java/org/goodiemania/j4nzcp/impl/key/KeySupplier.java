package org.goodiemania.j4nzcp.impl.key;

import org.goodiemania.j4nzcp.exception.KeySupplierException;

public interface KeySupplier {
    String get(final String url) throws KeySupplierException;
}
