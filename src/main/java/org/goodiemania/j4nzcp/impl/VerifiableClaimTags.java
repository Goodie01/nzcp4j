package org.goodiemania.j4nzcp.impl;

public enum VerifiableClaimTags {
    ALG("1"),
    KID("4"),
    CTI("7"),
    ISS("1"),
    NBF("5"),
    EXP("4"),
    JTI("7"),
    ;

    private String keyValue;

    VerifiableClaimTags(final String keyValue) {
        this.keyValue = keyValue;
    }

    public String get() {
        return keyValue;
    }
}
