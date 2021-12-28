package org.goodiemania.nzcp4j.exceptions;

import java.time.LocalDateTime;

public class ExpiredPassException extends InvalidPassException {
    public ExpiredPassException(LocalDateTime expiry) {
        super("Expired pass:" + expiry);
    }
}
