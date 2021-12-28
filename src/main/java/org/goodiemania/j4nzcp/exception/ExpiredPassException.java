package org.goodiemania.j4nzcp.exception;

import java.time.LocalDateTime;
import org.goodiemania.j4nzcp.InvalidPassException;

public class ExpiredPassException extends InvalidPassException {
    public ExpiredPassException(LocalDateTime expiry) {
        super("Expired pass:" + expiry);
    }
}
