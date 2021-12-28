package org.goodiemania.j4nzcp.impl.issuer;

import org.goodiemania.j4nzcp.exception.InvalidIssuerFormatException;
import org.goodiemania.j4nzcp.impl.entities.NewZealandCovidPass;

/**
 * The goal of this class is to know convert a given pass info into just the domain of the issuer
 * <p>
 * We take something like this "did:web:nzcp.covid19.health.nz"
 * validate it starts with "did:web:"
 * and return "nzcp.covid19.health.nz"
 */
public class IssuerExtractor {
    private static final String ISSUER_TEXT = "did:web:";

    public String extractIssuer(final NewZealandCovidPass covidPass) throws InvalidIssuerFormatException {
        String issuer = covidPass.payload().iss();
        if (!issuer.startsWith(ISSUER_TEXT)) {
            throw new InvalidIssuerFormatException(issuer);
        }

        return issuer.replace(ISSUER_TEXT, "");
    }
}
