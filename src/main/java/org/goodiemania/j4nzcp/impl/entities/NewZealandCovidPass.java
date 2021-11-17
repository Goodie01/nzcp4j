package org.goodiemania.j4nzcp.impl.entities;

public record NewZealandCovidPass(
        ProtectedHeaders headers,
        Payload payload,
        byte[] headerValue,
        byte[] weirdMiddleValue,
        byte[] payloadvalue,
        byte[] signatureValue) {
}
