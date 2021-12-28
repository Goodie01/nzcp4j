package org.goodiemania.nzcp4j;

import org.goodiemania.nzcp4j.exceptions.Nzcp4JException;

public interface Verifier {
    static VerifierBuilder builder() {
        return new VerifierBuilder();
    }

    VerificationResult verify(final String nzcpCode) throws Nzcp4JException;
}
