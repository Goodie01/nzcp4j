package org.goodiemania.j4nzcp.impl.entities.key;

public record PublicKeyJwk(
    String kty,
    String crv,
    String x,
    String y
) {
}
