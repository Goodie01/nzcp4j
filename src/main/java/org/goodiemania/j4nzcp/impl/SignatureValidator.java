package org.goodiemania.j4nzcp.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.dataformat.cbor.databind.CBORMapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;
import java.util.Base64;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.DERSequenceGenerator;
import org.goodiemania.j4nzcp.Nzcp4JException;
import org.goodiemania.j4nzcp.exception.BadSignatureException;
import org.goodiemania.j4nzcp.impl.entities.NewZealandCovidPass;
import org.goodiemania.j4nzcp.impl.entities.PublicKeysDetails;
import org.goodiemania.j4nzcp.impl.key.KeySupplier;
import org.goodiemania.j4nzcp.impl.key.UnirestKeySupplier;

public class SignatureValidator {
    private static final CBORMapper CBOR_MAPPER = new CBORMapper();

    private final MessageDigest MESSAGE_DIGEST;
    private final KeySupplier keySupplier = new UnirestKeySupplier();/*covidPass -> new PublicKeysDetails(
        "EC",
        "P-256",
        "zRR-XGsCp12Vvbgui4DD6O6cqmhfPuXMhi1OxPl8760",
        "Iv5SU6FuW-TRYh5_GOrJlcV_gpF_GpFQhCOD8LSk3T0"
    );*/

    public SignatureValidator() throws NoSuchAlgorithmException {
        MESSAGE_DIGEST = MessageDigest.getInstance("SHA-256");
    }

    public void validate(final NewZealandCovidPass covidPass) throws Nzcp4JException {
        if (!this.verifySignature(covidPass)) {
            throw new BadSignatureException();
        }
    }

    private boolean verifySignature(final NewZealandCovidPass covidPass) {
        try {
            PublicKey publicKey = extractPublicKey(keySupplier.getPublicKeyDetails(covidPass));
            byte[] messageHash = buildMessageHash(covidPass);
            byte[] signature = convertConcatToDer(covidPass.signatureValue());

            Signature ecdsaSign = Signature.getInstance("SHA256withECDSA");
            ecdsaSign.initVerify(publicKey);
            ecdsaSign.update(messageHash);
            return ecdsaSign.verify(signature);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private PublicKey extractPublicKey(PublicKeysDetails publicKeyDetails) throws NoSuchAlgorithmException, InvalidParameterSpecException, InvalidKeySpecException {
        byte[] xBytes = Base64.getDecoder().decode(publicKeyDetails.x().replace('-', '+').replace('_', '/'));
        byte[] yBytes = Base64.getDecoder().decode(publicKeyDetails.y().replace('-', '+').replace('_', '/'));
        BigInteger x = new BigInteger(xBytes);
        BigInteger y = new BigInteger(yBytes);
        ECPoint pubPoint = new ECPoint(x, y);
        AlgorithmParameters parameters = AlgorithmParameters.getInstance("EC");//publicKeyDetails.kty() Should always come from the endpoint as "EC"
        parameters.init(new ECGenParameterSpec("secp256r1"));//publicKeyDetails.crv() Should always come from the endpoint as "P-256", java wants to know exactly secp256r1, or NIST P-256
        ECParameterSpec ecParameters = parameters.getParameterSpec(ECParameterSpec.class);
        ECPublicKeySpec pubSpec = new ECPublicKeySpec(pubPoint, ecParameters);
        KeyFactory kf = KeyFactory.getInstance("EC");

        return kf.generatePublic(pubSpec);
    }

    public byte[] buildMessageHash(NewZealandCovidPass covidPass) throws IOException, NoSuchAlgorithmException {
        ArrayNode objectNode = CBOR_MAPPER.createArrayNode();
        objectNode.add("Signature1");
        objectNode.add(covidPass.headerValue());
        objectNode.add(new byte[]{});
        objectNode.add(covidPass.payloadvalue());

        return CBOR_MAPPER.writeValueAsBytes(objectNode);
//        return MESSAGE_DIGEST.digest(CBOR_MAPPER.writeValueAsBytes(objectNode));
    }

    private byte[] convertConcatToDer(byte[] concat) {
        try {
            int len = concat.length / 2;
            byte[] r = Arrays.copyOfRange(concat, 0, len);
            byte[] s = Arrays.copyOfRange(concat, len, concat.length);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            DERSequenceGenerator derSequenceGenerator = new DERSequenceGenerator(outputStream);
            derSequenceGenerator.addObject(new ASN1Integer(r));
            derSequenceGenerator.addObject(new ASN1Integer(s));
            derSequenceGenerator.close();
            outputStream.close();

            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
