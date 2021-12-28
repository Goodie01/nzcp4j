package org.goodiemania.nzcp4j.exceptions;

public class InvalidFormatException extends Nzcp4JException {
    public InvalidFormatException() {
        super("Invalid format detected", "https://nzcp.covid19.health.nz/#2d-barcode-encoding");
    }
}
