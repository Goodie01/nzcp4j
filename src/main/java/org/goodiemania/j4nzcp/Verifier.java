package org.goodiemania.j4nzcp;

public interface Verifier {
    static VerifierBuilder builder() {
        return new VerifierBuilder();
    }

    void verify(final String nzcpCode) throws J4NzcpException;
}
