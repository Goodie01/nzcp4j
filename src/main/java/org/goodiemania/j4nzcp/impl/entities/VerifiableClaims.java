package org.goodiemania.j4nzcp.impl.entities;

import java.util.List;

public record VerifiableClaims(
        List<String> Context,
        String version,
        List<String> type,
        CredentialSubject credentials
) {
}
