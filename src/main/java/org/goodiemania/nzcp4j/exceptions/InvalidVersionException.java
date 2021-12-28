package org.goodiemania.nzcp4j.exceptions;

public class InvalidVersionException extends Nzcp4JException {
    public InvalidVersionException(final String version) {
        super("Unsupported version detected: " + version, "https://nzcp.covid19.health.nz/#2d-barcode-encoding");
    }
}
