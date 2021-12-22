package org.goodiemania.j4nzcp.exception;

import org.goodiemania.j4nzcp.InvalidPassException;

import java.time.LocalDateTime;

public class ExpiredPassException extends InvalidPassException {
    public ExpiredPassException(LocalDateTime expiry) {
        super("Expired pass:" + expiry);
    }
}
