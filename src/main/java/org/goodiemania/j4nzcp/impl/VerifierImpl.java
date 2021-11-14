package org.goodiemania.j4nzcp.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.cbor.databind.CBORMapper;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import org.goodiemania.j4nzcp.J4NzcpException;
import org.goodiemania.j4nzcp.Verifier;
import org.goodiemania.j4nzcp.exception.InvalidFormatException;
import org.goodiemania.j4nzcp.exception.InvalidVersionException;
import org.goodiemania.j4nzcp.exception.UnsupportedAlgorithmException;
import org.goodiemania.j4nzcp.impl.entities.CredentialSubject;
import org.goodiemania.j4nzcp.impl.entities.CwtPayload;
import org.goodiemania.j4nzcp.impl.entities.ProtectedHeaders;
import org.goodiemania.j4nzcp.impl.entities.VerifiableClaims;

import static org.goodiemania.j4nzcp.impl.VerifiableClaimTags.ALG;
import static org.goodiemania.j4nzcp.impl.VerifiableClaimTags.CONTEXT;
import static org.goodiemania.j4nzcp.impl.VerifiableClaimTags.CREDENTIAL_SUBJECT;
import static org.goodiemania.j4nzcp.impl.VerifiableClaimTags.DATE_OF_BIRTH;
import static org.goodiemania.j4nzcp.impl.VerifiableClaimTags.EXP;
import static org.goodiemania.j4nzcp.impl.VerifiableClaimTags.FAMILY_NAME;
import static org.goodiemania.j4nzcp.impl.VerifiableClaimTags.GIVEN_NAME;
import static org.goodiemania.j4nzcp.impl.VerifiableClaimTags.ISS;
import static org.goodiemania.j4nzcp.impl.VerifiableClaimTags.JTI;
import static org.goodiemania.j4nzcp.impl.VerifiableClaimTags.KID;
import static org.goodiemania.j4nzcp.impl.VerifiableClaimTags.NBF;
import static org.goodiemania.j4nzcp.impl.VerifiableClaimTags.TYPE;
import static org.goodiemania.j4nzcp.impl.VerifiableClaimTags.VC;
import static org.goodiemania.j4nzcp.impl.VerifiableClaimTags.VERSION;

public class VerifierImpl implements Verifier {
    private static final String payloadRegex = "(NZCP:\\/)([0-9]+)\\/([A-Za-z2-7=]+)";
    private static final Pattern PATTERN_MATCHER = Pattern.compile(payloadRegex);

    private static final String VERSION_ONE = "1";
    private static final Base32 BASE32_ENCODER = new Base32();
    private CBORMapper CBOR_MAPPER = new CBORMapper();

    @Override
    public void verify(final String nzcpCode) throws J4NzcpException {
        Matcher matcher = PATTERN_MATCHER.matcher(nzcpCode);

        if (!matcher.matches()) {
            throw new InvalidFormatException();
        }

        String version = matcher.group(2);
        if (VERSION_ONE.equals(version)) {
            String cwtValue = addPadding(matcher.group(3));
            System.out.println(cwtValue);
            byte[] decode = BASE32_ENCODER.decode(cwtValue);
            JsonNode cborObject = getCborObject(decode);
            ProtectedHeaders protectedHeaders = decodeProtectedHeaders(cborObject.get(0));
            CwtPayload cwtPayload = decodePayload(cborObject.get(2));
            //TODO next we need to process the cwtPayload.iss() and turn it into https://nzcp.covid19.health.nz/.well-known/did.json
            // Pull down that given key, and check the hash
            System.out.println("Lets do this");
        } else {
            throw new InvalidVersionException(version);
        }
    }

    private JsonNode getCborObject(final byte[] decode) {
        try {
            return CBOR_MAPPER.reader().readTree(decode);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private ProtectedHeaders decodeProtectedHeaders(final JsonNode headerNode) {
        try {
            JsonNode headerObject = CBOR_MAPPER.readTree(headerNode.binaryValue());
            String kid = new String(headerObject.get(KID).binaryValue());
            String alg;
            long algorithmTagValue = headerObject.get(ALG).longValue();
            if (algorithmTagValue == AlgorithmMapping.ES256.tag()) {
                alg = AlgorithmMapping.ES256.algorithmName();
            } else {
                throw new UnsupportedAlgorithmException(algorithmTagValue);
            }

            return new ProtectedHeaders(kid, alg);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private CwtPayload decodePayload(final JsonNode payloadNode) {
        try {
            JsonNode payloadBody = CBOR_MAPPER.readTree(payloadNode.binaryValue());
            String iss = payloadBody.get(ISS).textValue();
            long notBefore = payloadBody.get(NBF).longValue();
            long expiry = payloadBody.get(EXP).longValue();
            var uuid = extractJtiString(payloadBody.get(JTI).binaryValue());

            VerifiableClaims verifiableClaims = parseVerifiableClaims(payloadBody.get(VC));

            return new CwtPayload(iss,
                    LocalDateTime.ofEpochSecond(notBefore, 0, ZoneOffset.ofHours(13)),
                    LocalDateTime.ofEpochSecond(expiry, 0, ZoneOffset.ofHours(13)),
                    uuid,
                    verifiableClaims);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private String extractJtiString(final byte[] bytes) {
        String hexUuid = Hex.encodeHexString(bytes);

        String timeLow = hexUuid.substring(0, 8);
        String timeMid = hexUuid.substring(8, 12);
        String timeHighAndVersion = hexUuid.substring(12, 16);
        String clockSeqAndReserved = hexUuid.substring(16, 18);
        String clockSeqLow = hexUuid.substring(18, 20);
        String node = hexUuid.substring(20, 32);

        return "urn:uuid:" + timeLow + "-" + timeMid + "-" + timeHighAndVersion + "-" + clockSeqAndReserved + clockSeqLow + "-" + node;
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

    private String addPadding(final String base32Input) {
        StringBuilder result = new StringBuilder(base32Input);
        while (result.length() % 8 != 0) {
            result.append("=");
        }

        return result.toString();
    }
}
