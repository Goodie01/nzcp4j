package org.goodiemania.nzcp4j.impl.dto;

public record VerificationMethod(
    String id,
    String controller,
    String type,
    PublicKeyJwk publicKeyJwk
) {
}
