package org.goodiemania.j4nzcp.impl;

import org.goodiemania.j4nzcp.Nzcp4JException;
import org.goodiemania.j4nzcp.Verifier;
import org.goodiemania.j4nzcp.exception.InvalidVersionException;
import org.goodiemania.j4nzcp.impl.entities.ExtractedCovidPassDetails;
import org.goodiemania.j4nzcp.impl.entities.NewZealandCovidPass;

public class VerifierImpl implements Verifier {
    private static final String VERSION_ONE = "1";
    private static final RawStringExtractor RAW_STRING_EXTRACTOR = new RawStringExtractor();
    private static final CovidPassExtractor COVID_PASS_EXTRACTOR = new CovidPassExtractor();
    private static final CovidPassValidator COVID_PASS_VALIDATOR = new CovidPassValidator();
    private static final SignatureValidator SIGNATURE_VALIDATOR = new SignatureValidator();

    @Override
    public void verify(final String nzcpCode) throws Nzcp4JException {
        ExtractedCovidPassDetails extract = RAW_STRING_EXTRACTOR.extract(nzcpCode);

        String version = extract.version();
        if (VERSION_ONE.equals(version)) {
            String cwtValue = extract.payload();
            NewZealandCovidPass pass = COVID_PASS_EXTRACTOR.extract(cwtValue);
            COVID_PASS_VALIDATOR.validate(pass);
            SIGNATURE_VALIDATOR.validate(pass);
            System.out.println("Lets do this");
        } else {
            throw new InvalidVersionException(version);
        }
    }
}
