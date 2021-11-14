package org.goodiemania.j4nzcp;

import org.goodiemania.j4nzcp.impl.VerifierImpl;

public class VerifierBuilder {
    public Verifier build() {
        return new VerifierImpl();
    }
}
