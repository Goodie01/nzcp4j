package org.goodiemania.nzcp4j.exceptions;

import java.time.LocalDateTime;

public class InactivePassException extends InvalidPassException {
    public InactivePassException(LocalDateTime localDateTime) {
        super("Inactive pass:" + localDateTime);
    }
}
