package org.goodiemania.j4nzcp.exception;

import org.goodiemania.j4nzcp.J4NzcpException;

public class InvalidVersionException extends J4NzcpException {
    public InvalidVersionException(final String version) {
        super("Unsupported version detected: " + version, "https://nzcp.covid19.health.nz/#2d-barcode-encoding");
    }
}
