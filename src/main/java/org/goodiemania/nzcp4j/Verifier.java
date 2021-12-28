package org.goodiemania.nzcp4j;

public interface Verifier {
    static VerifierBuilder builder() {
        return new VerifierBuilder();
    }

    VerificationResult verify(final String nzcpCode) throws Nzcp4JException;
}
