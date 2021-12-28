package org.goodiemania.nzcp4j.exception;

import org.goodiemania.nzcp4j.Nzcp4JException;

public class InvalidVersionException extends Nzcp4JException {
    public InvalidVersionException(final String version) {
        super("Unsupported version detected: " + version, "https://nzcp.covid19.health.nz/#2d-barcode-encoding");
    }
}
