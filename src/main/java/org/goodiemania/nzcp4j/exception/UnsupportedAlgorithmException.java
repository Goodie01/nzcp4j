package org.goodiemania.nzcp4j.exception;

import java.text.MessageFormat;
import org.goodiemania.nzcp4j.Nzcp4JException;

public class UnsupportedAlgorithmException extends Nzcp4JException {
    public UnsupportedAlgorithmException(final String algorithmTagValue) {
        super(MessageFormat.format("Invalid Algorithm({0}), only ES256 is currently supported", algorithmTagValue));
    }
}
