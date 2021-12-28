package org.goodiemania.nzcp4j.impl.entities;

import java.util.List;

public record VerifiableClaims(
    List<String> Context,
    String version,
    List<String> type,
    CredentialSubject credentials
) {
}
