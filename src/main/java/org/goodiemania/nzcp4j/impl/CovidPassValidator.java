package org.goodiemania.nzcp4j.impl;

import java.time.LocalDateTime;
import java.util.Set;
import org.goodiemania.nzcp4j.exceptions.Nzcp4JException;
import org.goodiemania.nzcp4j.exceptions.ExpiredPassException;
import org.goodiemania.nzcp4j.exceptions.InactivePassException;
import org.goodiemania.nzcp4j.exceptions.InvalidIssuerException;
import org.goodiemania.nzcp4j.exceptions.UnknownIssuerException;
import org.goodiemania.nzcp4j.exceptions.UnsupportedAlgorithmException;
import org.goodiemania.nzcp4j.impl.entities.NewZealandCovidPass;

public class CovidPassValidator {
    private static final String ISSUER_PREFIX = "did:web:";
    private final Set<String> trustedIssuers;

    public CovidPassValidator(final Set<String> trustedIssuers) {

        this.trustedIssuers = trustedIssuers;
    }

    public void validate(final NewZealandCovidPass covidPass) throws Nzcp4JException {
        if (!covidPass.headers().alg().equals(AlgorithmMapping.ES256.algorithmName())) {
            throw new UnsupportedAlgorithmException(covidPass.headers().alg());
        }

        String passIssuer = covidPass.payload().iss();
        if (!passIssuer.startsWith(ISSUER_PREFIX)) {
            throw new InvalidIssuerException(passIssuer);
        }

        if (!trustedIssuers.contains(passIssuer.replace(ISSUER_PREFIX, ""))) {
            throw new UnknownIssuerException(passIssuer);
        }

        LocalDateTime currentLocalDateTime = LocalDateTime.now();
        LocalDateTime notBefore = covidPass.payload().notBefore();
        if (notBefore.isAfter(currentLocalDateTime)) {
            throw new InactivePassException(notBefore);
        }

        LocalDateTime expiry = covidPass.payload().expiry();
        if (expiry.isBefore(currentLocalDateTime)) {
            throw new ExpiredPassException(expiry);
        }
    }
}
