package org.goodiemania.nzcp4j;

import org.goodiemania.nzcp4j.exceptions.KeySupplierException;

public interface KeySupplier {
    String get(final String url) throws KeySupplierException;
}
