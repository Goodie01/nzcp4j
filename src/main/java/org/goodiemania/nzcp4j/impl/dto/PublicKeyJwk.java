package org.goodiemania.nzcp4j.impl.dto;

public record PublicKeyJwk(
    String kty,
    String crv,
    String x,
    String y
) {
}
