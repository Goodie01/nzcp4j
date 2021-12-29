package org.goodiemania.nzcp4j.exceptions;

import java.text.MessageFormat;

public class UnsupportedAlgorithmException extends InvalidPassException {
    public UnsupportedAlgorithmException(final String algorithmTagValue) {
        super(MessageFormat.format("Invalid Algorithm({0}), only ES256 is currently supported", algorithmTagValue));
    }
}
