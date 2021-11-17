package org.goodiemania.j4nzcp.impl;

import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.KeyFactory;
import java.security.Signature;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPublicKeySpec;
import java.util.Base64;
import org.goodiemania.j4nzcp.Nzcp4JException;
import org.goodiemania.j4nzcp.impl.entities.NewZealandCovidPass;
import org.goodiemania.j4nzcp.impl.entities.PublicKeysDetails;
import org.goodiemania.j4nzcp.impl.key.KeySupplier;
import org.goodiemania.j4nzcp.impl.key.UnirestKeySupplier;

public class SignatureValidator {
    private final KeySupplier keySupplier = new UnirestKeySupplier();

    public void validate(final NewZealandCovidPass covidPass) throws Nzcp4JException {
        PublicKeysDetails publicKeyDetails = keySupplier.getPublicKeyDetails(covidPass);


        final String signed_data = Base64.getEncoder().encodeToString(covidPass.headerValue())
                + "."
                + Base64.getEncoder().encodeToString(covidPass.payloadvalue());

        final byte[] signature = covidPass.signatureValue();

        try {
            final AlgorithmParameters parameters = AlgorithmParameters.getInstance(publicKeyDetails.kty());
            parameters.init(new ECGenParameterSpec("secp256k1"));
            final ECParameterSpec ecParameterSpec = parameters
                    .getParameterSpec(ECParameterSpec.class);
            final KeyFactory keyFactory = KeyFactory.getInstance(publicKeyDetails.kty());

            final ECPoint ecPoint = new ECPoint(BigInteger.ONE, BigInteger.ZERO);
            final ECPublicKeySpec keySpec = new ECPublicKeySpec(ecPoint,
                    ecParameterSpec);
            final ECPublicKey publicKey = (ECPublicKey) keyFactory
                    .generatePublic(keySpec);


            Signature dataVerifyingInstance = Signature.getInstance("SHA256withECDSA"); //ES256

            dataVerifyingInstance.initVerify(publicKey);
            dataVerifyingInstance.update(signed_data.getBytes());
            final boolean verification = dataVerifyingInstance.verify(signature);
            System.out.println(verification);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}