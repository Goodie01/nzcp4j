package org.goodiemania.nzcp4j;

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
