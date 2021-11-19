package org.goodiemania.j4nzcp.impl;

import org.goodiemania.j4nzcp.Nzcp4JException;
import org.goodiemania.j4nzcp.impl.entities.ExtractedCovidPassDetails;
import org.goodiemania.j4nzcp.impl.entities.NewZealandCovidPass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SignatureValidatorTest {
    private static final RawStringExtractor RAW_STRING_EXTRACTOR = new RawStringExtractor();
    private static final CovidPassExtractor COVID_PASS_EXTRACTOR = new CovidPassExtractor();

    private NewZealandCovidPass pass;

    @BeforeEach
    void setup() throws Nzcp4JException {
        String nzcpCode = "NZCP:/1/2KCEVIQEIVVWK6JNGEASNICZAEP2KALYDZSGSZB2O5SWEOTOPJRXALTDN53GSZBRHEXGQZLBNR2GQLTOPICRUYMBTIFAIGTUKBAAUYTWMOSGQQDDN5XHIZLYOSBHQJTIOR2HA4Z2F4XXO53XFZ3TGLTPOJTS6MRQGE4C6Y3SMVSGK3TUNFQWY4ZPOYYXQKTIOR2HA4Z2F4XW46TDOAXGG33WNFSDCOJONBSWC3DUNAXG46RPMNXW45DFPB2HGL3WGFTXMZLSONUW63TFGEXDALRQMR2HS4DFQJ2FMZLSNFTGSYLCNRSUG4TFMRSW45DJMFWG6UDVMJWGSY2DN53GSZCQMFZXG4LDOJSWIZLOORUWC3CTOVRGUZLDOSRWSZ3JOZSW4TTBNVSWISTBMNVWUZTBNVUWY6KOMFWWKZ2TOBQXE4TPO5RWI33CNIYTSNRQFUYDILJRGYDVAYFE6VGU4MCDGK7DHLLYWHVPUS2YIDJOA6Y524TD3AZRM263WTY2BE4DPKIF27WKF3UDNNVSVWRDYIYVJ65IRJJJ6Z25M2DO4YZLBHWFQGVQR5ZLIWEQJOZTS3IQ7JTNCFDX";
        ExtractedCovidPassDetails extract = RAW_STRING_EXTRACTOR.extract(nzcpCode);
        pass = COVID_PASS_EXTRACTOR.extract(extract.payload());
    }

    @Test
    void test1() throws Exception{
        new SignatureValidator().validate(pass);
    }
}