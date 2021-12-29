package org.goodiemania.nzcp4j.exceptions;

/**
 * Indicates that the covid pass was in the incorrect format
 */
public class InvalidFormatException extends InvalidPassException {
    /**
     * Constructs a new instance of InvalidFormatException with a link to the relevant section of documentation
     */
    public InvalidFormatException() {
        super("Invalid format detected", "https://nzcp.covid19.health.nz/#2d-barcode-encoding");
    }
}
