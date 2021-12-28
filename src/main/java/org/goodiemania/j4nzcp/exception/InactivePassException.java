package org.goodiemania.j4nzcp.exception;

import java.time.LocalDateTime;
import org.goodiemania.j4nzcp.InvalidPassException;

public class InactivePassException extends InvalidPassException {
    public InactivePassException(LocalDateTime localDateTime) {
        super("Inactive pass:" + localDateTime);
    }
}
