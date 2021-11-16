package org.goodiemania.j4nzcp.impl;

import org.goodiemania.j4nzcp.Nzcp4JException;
import org.goodiemania.j4nzcp.exception.InvalidIssException;
import org.goodiemania.j4nzcp.exception.UnsupportedAlgorithmException;
import org.goodiemania.j4nzcp.impl.entities.NewZealandCovidPass;

public class CovidPassValidator {
    public void validate(final NewZealandCovidPass covidPass) throws Nzcp4JException {
        if(!covidPass.headers().alg().equals(AlgorithmMapping.ES256.algorithmName())) {
            throw new UnsupportedAlgorithmException(covidPass.headers().alg());
        }

        if(!covidPass.payload().iss().startsWith("did:web:")) {
            throw new InvalidIssException(covidPass.payload().iss());
        }
    }
}
