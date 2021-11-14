package org.goodiemania.j4nzcp.exception;

import org.goodiemania.j4nzcp.J4NzcpException;

public class InvalidFormatException extends J4NzcpException {
    public InvalidFormatException() {
        super("Invalid format detected", "https://nzcp.covid19.health.nz/#2d-barcode-encoding");
    }
}
