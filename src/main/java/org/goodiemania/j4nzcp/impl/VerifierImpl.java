package org.goodiemania.j4nzcp.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.cbor.databind.CBORMapper;
import java.io.IOException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import org.goodiemania.j4nzcp.J4NzcpException;
import org.goodiemania.j4nzcp.Verifier;
import org.goodiemania.j4nzcp.exception.InvalidFormatException;
import org.goodiemania.j4nzcp.exception.InvalidVersionException;
import org.goodiemania.j4nzcp.exception.UnsupportedAlgorithmException;
import org.goodiemania.j4nzcp.impl.entities.CwtPayload;
import org.goodiemania.j4nzcp.impl.entities.ProtectedHeaders;

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
            String kid = new String(headerObject.get(VerifiableClaimTags.KID.get()).binaryValue());
            String alg;
            long algorithmTagValue = headerObject.get(VerifiableClaimTags.ALG.get()).longValue();
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
            String iss = payloadBody.get(VerifiableClaimTags.ISS.get()).textValue();
            long notBefore = payloadBody.get(VerifiableClaimTags.NBF.get()).longValue();
            long expiry = payloadBody.get(VerifiableClaimTags.EXP.get()).longValue();
            var uuid = payloadBody.get(VerifiableClaimTags.JTI.get()).binaryValue();
            String s = Hex.encodeHexString(uuid);
            String jti = new String(payloadBody.get(VerifiableClaimTags.JTI.get()).binaryValue()); //TODO decoding this way is bad

            return new CwtPayload(iss, null, null, jti, null);
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
}
