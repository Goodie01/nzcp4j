package org.goodiemania.j4nzcp.impl.key;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.goodiemania.j4nzcp.exception.KeySupplierException;

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
