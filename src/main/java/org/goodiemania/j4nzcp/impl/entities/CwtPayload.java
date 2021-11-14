package org.goodiemania.j4nzcp.impl.entities;

import java.time.LocalDateTime;

public record CwtPayload(String iss, LocalDateTime notBefore, LocalDateTime expiry, String jti, VerifiableClaims vc) {
}
