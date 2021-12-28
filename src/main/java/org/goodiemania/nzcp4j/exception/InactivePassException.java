package org.goodiemania.nzcp4j.exception;

import java.time.LocalDateTime;
import org.goodiemania.nzcp4j.InvalidPassException;

public class InactivePassException extends InvalidPassException {
    public InactivePassException(LocalDateTime localDateTime) {
        super("Inactive pass:" + localDateTime);
    }
}
