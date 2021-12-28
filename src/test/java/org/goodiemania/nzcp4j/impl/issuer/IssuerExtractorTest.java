package org.goodiemania.nzcp4j.impl.issuer;

import org.goodiemania.nzcp4j.exceptions.InvalidIssuerFormatException;
import org.goodiemania.nzcp4j.impl.entities.NewZealandCovidPass;
import org.goodiemania.nzcp4j.impl.entities.Payload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IssuerExtractorTest {
    IssuerExtractor instance;

    @BeforeEach
    void setUp() {
        instance = new IssuerExtractor();
    }

    @Test
    void happyFlowTest() throws InvalidIssuerFormatException {
        NewZealandCovidPass newZealandCovidPass = new NewZealandCovidPass(
                null,
                new Payload("did:web:nzcp.covid19.health.nz", null, null, null, null),
                null,
                null,
                null);
        String result = instance.extractIssuer(newZealandCovidPass);

        assertEquals("nzcp.covid19.health.nz", result);
    }

    @Test
    void invalidFormat() throws InvalidIssuerFormatException {
        NewZealandCovidPass newZealandCovidPass = new NewZealandCovidPass(
                null,
                new Payload("did2:web:nzcp.covid19.health.nz", null, null, null, null),
                null,
                null,
                null);

        assertThrows(InvalidIssuerFormatException.class, () -> instance.extractIssuer(newZealandCovidPass));
    }
}