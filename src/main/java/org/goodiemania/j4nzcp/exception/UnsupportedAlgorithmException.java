package org.goodiemania.j4nzcp.exception;

import java.text.MessageFormat;
import org.goodiemania.j4nzcp.J4NzcpException;

public class UnsupportedAlgorithmException extends J4NzcpException {
    public UnsupportedAlgorithmException(final long algorithmTagValue) {
        super(MessageFormat.format("Invalid Algorithm({0}), only ES256 is currently supported", algorithmTagValue));
    }
}
