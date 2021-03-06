package org.goodiemania.nzcp4j.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.cbor.databind.CBORMapper;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.goodiemania.nzcp4j.impl.entities.CredentialSubject;
import org.goodiemania.nzcp4j.impl.entities.NewZealandCovidPass;
import org.goodiemania.nzcp4j.impl.entities.Payload;
import org.goodiemania.nzcp4j.impl.entities.ProtectedHeaders;
import org.goodiemania.nzcp4j.impl.entities.VerifiableClaims;

import static org.goodiemania.nzcp4j.impl.data.VerifiableClaimTags.ALG;
import static org.goodiemania.nzcp4j.impl.data.VerifiableClaimTags.CONTEXT;
import static org.goodiemania.nzcp4j.impl.data.VerifiableClaimTags.CREDENTIAL_SUBJECT;
import static org.goodiemania.nzcp4j.impl.data.VerifiableClaimTags.DATE_OF_BIRTH;
import static org.goodiemania.nzcp4j.impl.data.VerifiableClaimTags.EXP;
import static org.goodiemania.nzcp4j.impl.data.VerifiableClaimTags.FAMILY_NAME;
import static org.goodiemania.nzcp4j.impl.data.VerifiableClaimTags.GIVEN_NAME;
import static org.goodiemania.nzcp4j.impl.data.VerifiableClaimTags.ISS;
import static org.goodiemania.nzcp4j.impl.data.VerifiableClaimTags.JTI;
import static org.goodiemania.nzcp4j.impl.data.VerifiableClaimTags.KID;
import static org.goodiemania.nzcp4j.impl.data.VerifiableClaimTags.NBF;
import static org.goodiemania.nzcp4j.impl.data.VerifiableClaimTags.TYPE;
import static org.goodiemania.nzcp4j.impl.data.VerifiableClaimTags.VC;
import static org.goodiemania.nzcp4j.impl.data.VerifiableClaimTags.VERSION;

public class CovidPassExtractor {
    private static final CBORMapper CBOR_MAPPER = new CBORMapper();

    public NewZealandCovidPass extract(final String cwtValue) {
        try {
            byte[] decode = Base32.decode(addPadding(cwtValue));
            JsonNode cborObject = getInitialJsonNode(decode);
            byte[] headerValue = cborObject.get(0).binaryValue();
            byte[] payloadValue = cborObject.get(2).binaryValue();
            byte[] signatureValue = cborObject.get(3).binaryValue();

            ProtectedHeaders protectedHeaders = decodeProtectedHeaders(headerValue);
            Payload cwtPayload = decodePayload(payloadValue);

            return new NewZealandCovidPass(protectedHeaders, cwtPayload, headerValue, payloadValue, signatureValue);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private String addPadding(final String base32Input) {
        StringBuilder result = new StringBuilder(base32Input);
        while (result.length() % 8 != 0) {
            result.append("=");
        }

        return result.toString();
    }

    private JsonNode getInitialJsonNode(final byte[] decode) {
        try {
            return CBOR_MAPPER.reader().readTree(decode);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private ProtectedHeaders decodeProtectedHeaders(final byte[] headerNode) {
        try {
            JsonNode headerObject = CBOR_MAPPER.readTree(headerNode);
            String kid = new String(headerObject.get(KID).binaryValue());
            String alg;
            long algorithmTagValue = headerObject.get(ALG).longValue();
            if (algorithmTagValue == AlgorithmMapping.ES256.tag()) {
                alg = AlgorithmMapping.ES256.algorithmName();
            } else {
                alg = String.valueOf(algorithmTagValue);
            }

            return new ProtectedHeaders(kid, alg);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private Payload decodePayload(final byte[] payloadValue) {
        try {
            JsonNode payloadBody = CBOR_MAPPER.readTree(payloadValue);
            String iss = payloadBody.get(ISS).textValue();
            long notBefore = payloadBody.get(NBF).longValue();
            long expiry = payloadBody.get(EXP).longValue();
            var uuid = extractJtiString(payloadBody.get(JTI).binaryValue());

            VerifiableClaims verifiableClaims = parseVerifiableClaims(payloadBody.get(VC));

            return new Payload(iss,
                LocalDateTime.ofEpochSecond(notBefore, 0, ZoneOffset.ofHours(13)),
                LocalDateTime.ofEpochSecond(expiry, 0, ZoneOffset.ofHours(13)),
                uuid,
                verifiableClaims);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private String extractJtiString(final byte[] bytes) {
        String hexUuid = encodeHexString(bytes);

        String timeLow = hexUuid.substring(0, 8);
        String timeMid = hexUuid.substring(8, 12);
        String timeHighAndVersion = hexUuid.substring(12, 16);
        String clockSeqAndReserved = hexUuid.substring(16, 18);
        String clockSeqLow = hexUuid.substring(18, 20);
        String node = hexUuid.substring(20, 32);

        return "urn:uuid:" + timeLow + "-" + timeMid + "-" + timeHighAndVersion + "-" + clockSeqAndReserved + clockSeqLow + "-" + node;
    }

    private String encodeHexString(final byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }


    private VerifiableClaims parseVerifiableClaims(final JsonNode verifiableClaimsNode) {
        List<String> contextList = convertToList(verifiableClaimsNode.get(CONTEXT))
            .stream()
            .map(JsonNode::textValue)
            .toList();
        String version = verifiableClaimsNode.get(VERSION).textValue();
        List<String> type = convertToList(verifiableClaimsNode.get(TYPE))
            .stream()
            .map(JsonNode::textValue)
            .toList();
        CredentialSubject credential = parseCredentials(verifiableClaimsNode.get(CREDENTIAL_SUBJECT));

        return new VerifiableClaims(contextList,
            version,
            type,
            credential);
    }

    private CredentialSubject parseCredentials(final JsonNode credentialsNode) {
        String givenName = credentialsNode.get(GIVEN_NAME).textValue();
        String familyName = credentialsNode.get(FAMILY_NAME).textValue();
        LocalDate dateOfBirth = LocalDate.parse(credentialsNode.get(DATE_OF_BIRTH).textValue());

        return new CredentialSubject(givenName, familyName, dateOfBirth);
    }

    private List<JsonNode> convertToList(JsonNode jsonNode) {
        Iterator<JsonNode> iterable = jsonNode.iterator();
        List<JsonNode> list = new ArrayList<>();

        while (iterable.hasNext()) {
            list.add(iterable.next());
        }

        return list;
    }
}
