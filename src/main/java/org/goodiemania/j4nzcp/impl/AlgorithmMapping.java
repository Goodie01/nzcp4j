package org.goodiemania.j4nzcp.impl;

public enum AlgorithmMapping {
    ES256(-7L, "ES256");

    private final long tag;
    private final String algorithmName;

    AlgorithmMapping(final long tag, final String algorithmName) {
        this.tag = tag;
        this.algorithmName = algorithmName;
    }

    public long tag() {
        return tag;
    }

    public String algorithmName() {
        return algorithmName;
    }
}
