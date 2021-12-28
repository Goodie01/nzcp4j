package org.goodiemania.j4nzcp.impl.entities.key;

public record VerificationMethod(
    String id,
    String controller,
    String type,
    PublicKeyJwk publicKeyJwk
) {
}
