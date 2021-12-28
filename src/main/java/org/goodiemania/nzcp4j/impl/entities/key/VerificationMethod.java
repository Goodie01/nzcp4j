package org.goodiemania.nzcp4j.impl.entities.key;

public record VerificationMethod(
    String id,
    String controller,
    String type,
    PublicKeyJwk publicKeyJwk
) {
}
