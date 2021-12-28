package org.goodiemania.j4nzcp.impl.key;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.goodiemania.j4nzcp.Nzcp4JException;
import org.goodiemania.j4nzcp.impl.entities.PublicKeysDetails;
import org.goodiemania.j4nzcp.impl.entities.key.PublicKeyJwk;
import org.goodiemania.j4nzcp.impl.entities.key.Root;
import org.goodiemania.j4nzcp.impl.entities.key.VerificationMethod;

public class KeyProcessor {
    private final ObjectMapper mapper;
    private final KeySupplier keySupplier = new UnirestKeySupplier();

    public KeyProcessor() {
        mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    }

    public PublicKeysDetails getPublicKeyDetails(String issuer, String kid) throws Nzcp4JException {
        String url = "https://" + issuer + "/.well-known/did.json";
        String stringHttpResponse = keySupplier.get(url);

        PublicKeyJwk foundVerificationMethod = getRootElement(stringHttpResponse, mapper)
            .verificationMethod()
            .stream()
            .filter(verificationMethod -> verificationMethod.id().endsWith("#" + kid))
            .findFirst()
            .map(VerificationMethod::publicKeyJwk)
            .orElseThrow();

        return new PublicKeysDetails(
            foundVerificationMethod.kty(),
            foundVerificationMethod.crv(),
            foundVerificationMethod.x(),
            foundVerificationMethod.y()
        );
    }

    private Root getRootElement(final String stringHttpResponse, final ObjectMapper objectMapper) {
        try {
            return objectMapper.readValue(stringHttpResponse, Root.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }
}
