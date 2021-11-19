package org.goodiemania.j4nzcp.impl.key;

import org.goodiemania.j4nzcp.Nzcp4JException;
import org.goodiemania.j4nzcp.exception.KeySupplierException;
import org.goodiemania.j4nzcp.impl.entities.NewZealandCovidPass;
import org.goodiemania.j4nzcp.impl.entities.PublicKeysDetails;

public class OfflineStaticKeySupplier implements KeySupplier {
    @Override
    public PublicKeysDetails getPublicKeyDetails(NewZealandCovidPass covidPass) throws Nzcp4JException {
        if (covidPass.payload().iss().equals("did:web:nzcp.covid19.health.nz")) {
            return new PublicKeysDetails(
                "EC",
                "P-256",
                "zRR-XGsCp12Vvbgui4DD6O6cqmhfPuXMhi1OxPl8760",
                "Iv5SU6FuW-TRYh5_GOrJlcV_gpF_GpFQhCOD8LSk3T0"
            );
        } else {
            throw new KeySupplierException("ISS does not match offline key");
        }
    }
}
