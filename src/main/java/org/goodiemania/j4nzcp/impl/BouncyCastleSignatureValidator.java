package org.goodiemania.j4nzcp.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.dataformat.cbor.databind.CBORMapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.DERSequenceGenerator;
import org.bouncycastle.util.encoders.UrlBase64;
import org.goodiemania.j4nzcp.Nzcp4JException;
import org.goodiemania.j4nzcp.impl.entities.NewZealandCovidPass;
import org.goodiemania.j4nzcp.impl.entities.PublicKeysDetails;
import org.goodiemania.j4nzcp.impl.key.KeySupplier;
import org.goodiemania.j4nzcp.impl.key.UnirestKeySupplier;

public class BouncyCastleSignatureValidator {
    private static final CBORMapper CBOR_MAPPER = new CBORMapper();
    private final KeySupplier keySupplier = new UnirestKeySupplier();

    public void validate(final NewZealandCovidPass covidPass) throws Nzcp4JException {
        PublicKeysDetails publicKeyDetails = keySupplier.getPublicKeyDetails(covidPass);

        try {
            PublicKey publicKey = extractPublicKey(publicKeyDetails);
            byte[] messageHash = buildMessageHash(covidPass);
            byte[] signature = convertConcatToDer(covidPass.signatureValue());

            Signature ecdsaSign = Signature.getInstance("SHA256withECDSA");
            ecdsaSign.initVerify(publicKey);
            ecdsaSign.update(messageHash);
            boolean result = ecdsaSign.verify(signature);
            System.out.println(result);
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
        AlgorithmParameters parameters = AlgorithmParameters.getInstance(publicKeyDetails.kty());
        parameters.init(new ECGenParameterSpec("secp256r1"));//publicKeyDetails.crv()));
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

        byte[] bytes = CBOR_MAPPER.writeValueAsBytes(objectNode);

        MessageDigest instance = MessageDigest.getInstance("SHA-256");
        return instance.digest(bytes);
//        return bytes;
    }

    private static byte[] convertConcatToDer(byte[] concat) {
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
