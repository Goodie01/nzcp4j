package org.goodiemania.j4nzcp.exception;

import org.goodiemania.j4nzcp.InvalidPassException;
import org.goodiemania.j4nzcp.Nzcp4JException;

public class InvalidIssException extends InvalidPassException {
    public InvalidIssException(final String iss) {
        super("Unsupported ISS detected: " + iss);
    }
}
