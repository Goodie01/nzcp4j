package org.goodiemania.nzcp4j.exception;

import java.time.LocalDateTime;
import org.goodiemania.nzcp4j.InvalidPassException;

public class ExpiredPassException extends InvalidPassException {
    public ExpiredPassException(LocalDateTime expiry) {
        super("Expired pass:" + expiry);
    }
}
