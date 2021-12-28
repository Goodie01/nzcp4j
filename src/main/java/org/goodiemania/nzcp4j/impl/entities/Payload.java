package org.goodiemania.nzcp4j.impl.entities;

import java.time.LocalDateTime;

public record Payload(
    String iss,
    LocalDateTime notBefore,
    LocalDateTime expiry,
    String jti,
    VerifiableClaims vc
) {
}
