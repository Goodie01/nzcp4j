package org.goodiemania.j4nzcp.exception;

import org.goodiemania.j4nzcp.Nzcp4JException;

public class InvalidFormatException extends Nzcp4JException {
    public InvalidFormatException() {
        super("Invalid format detected", "https://nzcp.covid19.health.nz/#2d-barcode-encoding");
    }
}
