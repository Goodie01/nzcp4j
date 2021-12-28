package org.goodiemania.nzcp4j.impl.key;

import org.goodiemania.nzcp4j.exception.KeySupplierException;

public class OfflineStaticKeySupplier implements KeySupplier {
    private static final String TEST_URL = "https://nzcp.covid19.health.nz/.well-known/did.json";
    private static final String LIVE_URL = "https://nzcp.identity.health.nz/.well-known/did.json";
    private static final String TEST_PAYLOAD = """
        {
          "@context": "https://w3.org/ns/did/v1",
          "id": "did:web:nzcp.covid19.health.nz",
          "verificationMethod": [
            {
              "id": "did:web:nzcp.covid19.health.nz#key-1",
              "controller": "did:web:nzcp.covid19.health.nz",
              "type": "JsonWebKey2020",
              "publicKeyJwk": {
                "kty": "EC",
                "crv": "P-256",
                "x": "zRR-XGsCp12Vvbgui4DD6O6cqmhfPuXMhi1OxPl8760",
                "y": "Iv5SU6FuW-TRYh5_GOrJlcV_gpF_GpFQhCOD8LSk3T0"
              }
            }
          ],
          "assertionMethod": [
            "did:web:nzcp.covid19.health.nz#key-1"
          ]
        }
        """;
    private static final String LIVE_PAYLOAD = """
            {
                "id": "did:web:nzcp.identity.health.nz",
                "@context": [
                    "https://w3.org/ns/did/v1",
                    "https://w3id.org/security/suites/jws-2020/v1"
                ],
                "verificationMethod": [
                    {
                        "id": "did:web:nzcp.identity.health.nz#z12Kf7UQ",
                        "controller": "did:web:nzcp.identity.health.nz",
                        "type": "JsonWebKey2020",
                        "publicKeyJwk": {
                            "kty": "EC",
                            "crv": "P-256",
                            "x": "DQCKJusqMsT0u7CjpmhjVGkHln3A3fS-ayeH4Nu52tc",
                            "y": "lxgWzsLtVI8fqZmTPPo9nZ-kzGs7w7XO8-rUU68OxmI"
                        }
                    }
                ],
                "assertionMethod": [
                    "did:web:nzcp.identity.health.nz#z12Kf7UQ"
                ]
            }
        """;

    @Override
    public String get(final String url) throws KeySupplierException {
        if (TEST_URL.equals(url)) {
            return TEST_PAYLOAD;
        } else if (LIVE_URL.equals(url)) {
            return LIVE_PAYLOAD;
        } else {
            throw new KeySupplierException("ISS does not match offline key");
        }
    }
}
