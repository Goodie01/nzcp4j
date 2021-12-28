package org.goodiemania.nzcp4j.impl.entities;

public record NewZealandCovidPass(
    ProtectedHeaders headers,
    Payload payload,
    byte[] headerValue,
    byte[] payloadValue,
    byte[] signatureValue) {
}
