package org.goodiemania.nzcp4j.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.dataformat.cbor.databind.CBORMapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Locale;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.DERSequenceGenerator;
import org.goodiemania.nzcp4j.exceptions.Nzcp4JException;
import org.goodiemania.nzcp4j.exceptions.BadSignatureException;
import org.goodiemania.nzcp4j.impl.entities.NewZealandCovidPass;
import org.goodiemania.nzcp4j.impl.entities.PublicKeysDetails;
import org.goodiemania.nzcp4j.impl.issuer.IssuerExtractor;
import org.goodiemania.nzcp4j.impl.key.KeyProcessor;
import org.goodiemania.nzcp4j.KeySupplier;

public class SignatureValidator {
    private final IssuerExtractor ISSUER_EXTRACTOR = new IssuerExtractor();
    private static final CBORMapper CBOR_MAPPER = new CBORMapper();

    private final KeyProcessor keySupplier;

    public SignatureValidator(final KeySupplier keySupplier) {
        this.keySupplier = new KeyProcessor(keySupplier);
    }

    public void validate(final NewZealandCovidPass covidPass) throws Nzcp4JException {
        if (!this.verifySignature(covidPass)) {
            throw new BadSignatureException();
        }
    }

    private boolean verifySignature(final NewZealandCovidPass covidPass) throws Nzcp4JException {
        try {
            PublicKey publicKey = extractPublicKey(covidPass);
            byte[] messageHash = buildMessageHash(covidPass);
            byte[] signature = buildSignature(covidPass.signatureValue());

            printByteArray("messageHash", messageHash);
            printByteArray("signature", signature);
            printByteArray("publicKey", publicKey.getEncoded());

            Signature ecdsaSign = Signature.getInstance("SHA256withECDSA");
            ecdsaSign.initVerify(publicKey);
            ecdsaSign.update(messageHash);
            return ecdsaSign.verify(signature);
        } catch (Exception e) {
            throw new BadSignatureException(e);
        }
    }

    private PublicKey extractPublicKey(NewZealandCovidPass covidPass) throws NoSuchAlgorithmException, InvalidParameterSpecException, InvalidKeySpecException, Nzcp4JException {
        String issuer = ISSUER_EXTRACTOR.extractIssuer(covidPass);
        PublicKeysDetails publicKeyDetails = keySupplier.getPublicKeyDetails(issuer, covidPass.headers().kid());

        byte[] xBytes = Base64.getUrlDecoder().decode(publicKeyDetails.x());
        byte[] yBytes = Base64.getUrlDecoder().decode(publicKeyDetails.y());
        printByteArray("xbytes", xBytes);
        printByteArray("ybytes", yBytes);
        BigInteger x = new BigInteger(1, xBytes);
        BigInteger y = new BigInteger(1, yBytes);
        printBigInteger("x", x);
        printBigInteger("y", y);

        ECPoint ecPoint = new ECPoint(x, y);

        ECGenParameterSpec parameterSpec = new ECGenParameterSpec("secp256r1");//publicKeyDetails.crv() Should always come from the endpoint as "P-256", java wants to know exactly secp256r1
        AlgorithmParameters parameters = AlgorithmParameters.getInstance("EC");//publicKeyDetails.kty() Should always come from the endpoint as "EC"
        parameters.init(parameterSpec);
        ECParameterSpec ecParameters = parameters.getParameterSpec(ECParameterSpec.class);
        ECPublicKeySpec pubSpec = new ECPublicKeySpec(ecPoint, ecParameters);

        KeyFactory kf = KeyFactory.getInstance("EC");
        return kf.generatePublic(pubSpec);
    }

    public byte[] buildMessageHash(NewZealandCovidPass covidPass) throws IOException, NoSuchAlgorithmException {
        ArrayNode objectNode = CBOR_MAPPER.createArrayNode();
        objectNode.add("Signature1");
        objectNode.add(covidPass.headerValue());
        objectNode.add(new byte[]{});
        objectNode.add(covidPass.payloadValue());

        return CBOR_MAPPER.writeValueAsBytes(objectNode);
    }

    private byte[] buildSignature(byte[] concat) throws BadSignatureException {
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
            throw new IllegalStateException("Error encountered while building signature", e);
        }
    }

    private void printBigInteger(final String name, BigInteger integer) {
        NumberFormat formatter = new DecimalFormat("0.###########E000", DecimalFormatSymbols.getInstance(Locale.ROOT));
        System.out.println(name + ": " + formatter.format(integer).replace("E", "E+"));
    }

    private void printByteArray(final String name, byte[] bytes) {
        System.out.print(name + ": ");
        for (final byte aByte : bytes) {
            System.out.print(aByte);
        }
        System.out.println();
    }
}
