package org.goodiemania.j4nzcp;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record VerificationResult(
        String givenName,
        String familyName,
        LocalDate dob,
        LocalDateTime notBefore,
        LocalDateTime expiry
) {
}
