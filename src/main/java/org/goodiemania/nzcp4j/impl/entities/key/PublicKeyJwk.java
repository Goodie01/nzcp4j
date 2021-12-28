package org.goodiemania.nzcp4j.impl.entities.key;

public record PublicKeyJwk(
    String kty,
    String crv,
    String x,
    String y
) {
}
