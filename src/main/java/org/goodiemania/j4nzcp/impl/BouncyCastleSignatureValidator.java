package org.goodiemania.j4nzcp.impl;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.cbor.databind.CBORMapper;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import org.goodiemania.j4nzcp.Nzcp4JException;
import org.goodiemania.j4nzcp.impl.entities.NewZealandCovidPass;
import org.goodiemania.j4nzcp.impl.entities.PublicKeysDetails;
import org.goodiemania.j4nzcp.impl.key.KeySupplier;
import org.goodiemania.j4nzcp.impl.key.UnirestKeySupplier;

public class BouncyCastleSignatureValidator {
    private static final byte[] SEQUENCE_TAG = new byte[]{0x30};
    private static final CBORMapper CBOR_MAPPER = new CBORMapper();
    private final KeySupplier keySupplier = new UnirestKeySupplier();

    public void validate(final NewZealandCovidPass covidPass) throws Nzcp4JException {
        PublicKeysDetails publicKeyDetails = keySupplier.getPublicKeyDetails(covidPass);


        final String message = Base64.getEncoder().encodeToString(covidPass.headerValue())
                + "."
                + Base64.getEncoder().encodeToString(covidPass.payloadvalue());
        final var signature = covidPass.signatureValue();

        try {
            KeyPairGenerator kg = KeyPairGenerator.getInstance("EC");
            ECGenParameterSpec kpgparams = new ECGenParameterSpec("secp256r1");
            kg.initialize(kpgparams);

            KeyPair kp = kg.generateKeyPair();
            PublicKey pubKey = kp.getPublic();
            PrivateKey pvtKey = kp.getPrivate();

            // sign
            Signature ecdsaSign = Signature.getInstance("SHA256withECDSA");

            // Validation
            ecdsaSign.initVerify(pubKey);

            byte[] messageHash = buildMessageHash(covidPass);
            ecdsaSign.update(messageHash);
            boolean result = ecdsaSign.verify(convertConcatToDer(signature));
            System.out.println(result);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public byte[] buildMessageHash(NewZealandCovidPass covidPass) throws IOException, NoSuchAlgorithmException {
        ArrayNode objectNode = CBOR_MAPPER.createArrayNode();
        objectNode.add("Signature1");
        objectNode.add(covidPass.headerValue());
        objectNode.add(covidPass.weirdMiddleValue());
        objectNode.add(covidPass.payloadvalue());

        byte[] bytes = CBOR_MAPPER.writeValueAsBytes(objectNode);

//          const ToBeSigned = encodeCBOR(SigStructure);
//          const messageHash = sha256.digest(ToBeSigned);

        MessageDigest instance = MessageDigest.getInstance("SHA-256");
        return instance.digest(bytes);
    }

    private static byte[] test(byte[] message) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(message);
    }


    private static byte[] convertConcatToDer(byte[] concat) {
        int len = concat.length / 2;
        byte[] r = Arrays.copyOfRange(concat, 0, len);
        byte[] s = Arrays.copyOfRange(concat, len, concat.length);


        ArrayList<byte[]> x = new ArrayList<>();
        x.add(UnsignedInteger(r));
        x.add(UnsignedInteger(s));

        return Sequence(x);
    }


    private static byte[] Sequence(ArrayList<byte[]> members) {
        byte[] y = ToBytes(members);
        ArrayList<byte[]> x = new ArrayList<byte[]>();
        x.add(SEQUENCE_TAG);
        x.add(ComputeLength(y.length));
        x.add(y);

        return ToBytes(x);
    }


    private static byte[] ComputeLength(int x) {
        if (x <= 127) {
            return new byte[]{(byte) x};
        } else if (x < 256) {
            return new byte[]{(byte) 0x81, (byte) x};
        }
        throw new IllegalStateException("Danger will robinson, danger");
    }


    private static byte[] ToBytes(ArrayList<byte[]> x) {
        int l = 0;
        l = x.stream().map((r) -> r.length).reduce(l, Integer::sum);

        byte[] b = new byte[l];
        l = 0;
        for (byte[] r : x) {
            System.arraycopy(r, 0, b, l, r.length);
            l += r.length;
        }

        return b;
    }

    private static byte[] UnsignedInteger(byte[] i) {
        int pad = 0, offset = 0;

        while (offset < i.length && i[offset] == 0) {
            offset++;
        }

        if (offset == i.length) {
            return new byte[]{0x02, 0x01, 0x00};
        }
        if ((i[offset] & 0x80) != 0) {
            pad++;
        }

        // M00BUG if the integer is > 127 bytes long with padding

        int length = i.length - offset;
        byte[] der = new byte[2 + length + pad];
        der[0] = 0x02;
        der[1] = (byte) (length + pad);
        System.arraycopy(i, offset, der, 2 + pad, length);

        return der;
    }
}
