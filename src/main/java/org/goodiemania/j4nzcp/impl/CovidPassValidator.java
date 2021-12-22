package org.goodiemania.j4nzcp.impl;

import org.goodiemania.j4nzcp.Nzcp4JException;
import org.goodiemania.j4nzcp.exception.ExpiredPassException;
import org.goodiemania.j4nzcp.exception.InactivePassException;
import org.goodiemania.j4nzcp.exception.InvalidIssuerException;
import org.goodiemania.j4nzcp.exception.UnsupportedAlgorithmException;
import org.goodiemania.j4nzcp.impl.entities.NewZealandCovidPass;

import java.time.LocalDateTime;

public class CovidPassValidator {
    public void validate(final NewZealandCovidPass covidPass) throws Nzcp4JException {
        if(!covidPass.headers().alg().equals(AlgorithmMapping.ES256.algorithmName())) {
            throw new UnsupportedAlgorithmException(covidPass.headers().alg());
        }

        if(!covidPass.payload().iss().startsWith("did:web:")) {
            throw new InvalidIssuerException(covidPass.payload().iss());
        }

        LocalDateTime currentLocalDateTime = LocalDateTime.now();
        LocalDateTime notBefore = covidPass.payload().notBefore();
        if(notBefore.isAfter(currentLocalDateTime)) {
            throw new InactivePassException(notBefore);
        }

        LocalDateTime expiry = covidPass.payload().expiry();
        if(expiry.isBefore(currentLocalDateTime)) {
            throw new ExpiredPassException(expiry);
        }
    }
}
