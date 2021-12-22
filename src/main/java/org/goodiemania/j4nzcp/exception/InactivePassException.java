package org.goodiemania.j4nzcp.exception;

import org.goodiemania.j4nzcp.InvalidPassException;

import java.time.LocalDateTime;

public class InactivePassException extends InvalidPassException {
    public InactivePassException(LocalDateTime localDateTime) {
        super("Inactive pass:" + localDateTime);
    }
}
