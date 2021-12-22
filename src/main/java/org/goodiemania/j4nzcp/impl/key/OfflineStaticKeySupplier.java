package org.goodiemania.j4nzcp.impl.key;

import org.goodiemania.j4nzcp.Nzcp4JException;
import org.goodiemania.j4nzcp.exception.KeySupplierException;
import org.goodiemania.j4nzcp.impl.entities.PublicKeysDetails;

public class OfflineStaticKeySupplier implements KeySupplier {
    private static final String TEST_KEY_NAME = "nzcp.covid19.health.nz";
    private static final String LIVE_KEY_NAME = "nzcp.identity.health.nz";
    private static final PublicKeysDetails TEST_KEY = new PublicKeysDetails( //key-1
            "EC",
            "P-256",
            "zRR-XGsCp12Vvbgui4DD6O6cqmhfPuXMhi1OxPl8760",
            "Iv5SU6FuW-TRYh5_GOrJlcV_gpF_GpFQhCOD8LSk3T0"
    );
    private static final PublicKeysDetails LIVE_KEY = new PublicKeysDetails( //z12Kf7UQ
            "EC",
            "P-256",
            "DQCKJusqMsT0u7CjpmhjVGkHln3A3fS-ayeH4Nu52tc",
            "lxgWzsLtVI8fqZmTPPo9nZ-kzGs7w7XO8-rUU68OxmI"
    );

    @Override
    public PublicKeysDetails getPublicKeyDetails(String issuer) throws Nzcp4JException {
        if (TEST_KEY_NAME.equals(issuer)) {
            return TEST_KEY;
        } else if(LIVE_KEY_NAME.equals(issuer)) {
            return LIVE_KEY;
        } else {
            throw new KeySupplierException("ISS does not match offline key");
        }
    }
}
