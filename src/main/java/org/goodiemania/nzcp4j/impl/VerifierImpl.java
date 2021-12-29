package org.goodiemania.nzcp4j.impl;

import java.util.Set;
import org.goodiemania.nzcp4j.exceptions.Nzcp4JException;
import org.goodiemania.nzcp4j.VerificationResult;
import org.goodiemania.nzcp4j.Verifier;
import org.goodiemania.nzcp4j.exceptions.InvalidVersionException;
import org.goodiemania.nzcp4j.impl.entities.ExtractedCovidPassDetails;
import org.goodiemania.nzcp4j.impl.entities.NewZealandCovidPass;
import org.goodiemania.nzcp4j.KeySupplier;

public class VerifierImpl implements Verifier {
    private static final String VERSION_ONE = "1";
    private final RawStringExtractor RAW_STRING_EXTRACTOR;
    private final CovidPassExtractor COVID_PASS_EXTRACTOR;
    private final CovidPassValidator COVID_PASS_VALIDATOR;
    private final SignatureValidator SIGNATURE_VALIDATOR;

    public VerifierImpl(Set<String> trustedIssuers, KeySupplier keySupplier) {
        try {
            RAW_STRING_EXTRACTOR = new RawStringExtractor();
            COVID_PASS_EXTRACTOR = new CovidPassExtractor();
            COVID_PASS_VALIDATOR = new CovidPassValidator(trustedIssuers);
            SIGNATURE_VALIDATOR = new SignatureValidator(keySupplier);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public VerificationResult verify(final String nzcpCode) throws Nzcp4JException {
        ExtractedCovidPassDetails extract = RAW_STRING_EXTRACTOR.extract(nzcpCode);

        String version = extract.version();
        if (VERSION_ONE.equals(version)) {
            String cwtValue = extract.payload();
            NewZealandCovidPass pass = COVID_PASS_EXTRACTOR.extract(cwtValue);
            COVID_PASS_VALIDATOR.validate(pass);
            SIGNATURE_VALIDATOR.validate(pass);

            return new VerificationResult(
                pass.payload().vc().credentials().givenName(),
                pass.payload().vc().credentials().familyName(),
                pass.payload().vc().credentials().dob(),
                pass.payload().notBefore(),
                pass.payload().expiry()
            );
        } else {
            throw new InvalidVersionException(version);
        }
    }
}
