package org.goodiemania.nzcp4j.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.goodiemania.nzcp4j.exceptions.Nzcp4JException;
import org.goodiemania.nzcp4j.exceptions.InvalidFormatException;
import org.goodiemania.nzcp4j.impl.entities.ExtractedCovidPassDetails;

public class RawStringExtractor {
    private static final String payloadRegex = "(NZCP:\\/)([0-9]+)\\/([A-Za-z2-7=]+)";
    private static final Pattern PATTERN_MATCHER = Pattern.compile(payloadRegex);

    private static final String VERSION_ONE = "1";

    public ExtractedCovidPassDetails extract(final String nzcpCode) throws Nzcp4JException {
        Matcher matcher = PATTERN_MATCHER.matcher(nzcpCode);

        if (!matcher.matches()) {
            throw new InvalidFormatException();
        }

        return new ExtractedCovidPassDetails(matcher.group(2), matcher.group(3));
    }

}
