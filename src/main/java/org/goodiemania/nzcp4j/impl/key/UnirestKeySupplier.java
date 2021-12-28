package org.goodiemania.nzcp4j.impl.key;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.goodiemania.nzcp4j.exceptions.KeySupplierException;

public class UnirestKeySupplier implements KeySupplier {
    @Override
    public String get(final String url) throws KeySupplierException {
        try {
            return Unirest.get(url).asString().getBody();
        } catch (UnirestException e) {
            throw new KeySupplierException(e);
        }
    }
}
