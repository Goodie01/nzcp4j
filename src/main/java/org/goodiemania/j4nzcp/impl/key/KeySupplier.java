package org.goodiemania.j4nzcp.impl.key;

import org.goodiemania.j4nzcp.Nzcp4JException;
import org.goodiemania.j4nzcp.impl.entities.NewZealandCovidPass;
import org.goodiemania.j4nzcp.impl.entities.PublicKeysDetails;

public interface KeySupplier {
    PublicKeysDetails getPublicKeyDetails(String issuer) throws Nzcp4JException;
}
