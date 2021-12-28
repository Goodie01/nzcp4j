package org.goodiemania.j4nzcp.impl;

import java.time.LocalDateTime;
import java.util.Set;
import org.goodiemania.j4nzcp.Nzcp4JException;
import org.goodiemania.j4nzcp.exception.ExpiredPassException;
import org.goodiemania.j4nzcp.exception.InactivePassException;
import org.goodiemania.j4nzcp.exception.InvalidIssuerException;
import org.goodiemania.j4nzcp.exception.UnknownIssuerException;
import org.goodiemania.j4nzcp.exception.UnsupportedAlgorithmException;
import org.goodiemania.j4nzcp.impl.entities.NewZealandCovidPass;

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
