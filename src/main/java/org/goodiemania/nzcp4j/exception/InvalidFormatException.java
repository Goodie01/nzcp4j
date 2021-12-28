package org.goodiemania.nzcp4j.exception;

import org.goodiemania.nzcp4j.Nzcp4JException;

public class InvalidFormatException extends Nzcp4JException {
    public InvalidFormatException() {
        super("Invalid format detected", "https://nzcp.covid19.health.nz/#2d-barcode-encoding");
    }
}
