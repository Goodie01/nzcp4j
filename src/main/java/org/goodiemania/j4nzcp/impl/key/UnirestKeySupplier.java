package org.goodiemania.j4nzcp.impl.key;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.goodiemania.j4nzcp.Nzcp4JException;
import org.goodiemania.j4nzcp.exception.KeySupplierException;
import org.goodiemania.j4nzcp.impl.entities.NewZealandCovidPass;
import org.goodiemania.j4nzcp.impl.entities.PublicKeysDetails;
import org.json.JSONObject;

public class UnirestKeySupplier implements KeySupplier {
    @Override
    public PublicKeysDetails getPublicKeyDetails(String issuer) throws Nzcp4JException {
        try {
            String url = "https://" + issuer + "/.well-known/did.json";
            JSONObject jsonObject = Unirest.get(url).asJson().getBody().getObject()
                    .getJSONArray("verificationMethod")
                    .getJSONObject(0)
                    .getJSONObject("publicKeyJwk");
            return new PublicKeysDetails(jsonObject.getString("kty"),
                    jsonObject.getString("crv"),
                    jsonObject.getString("x"),
                    jsonObject.getString("y"));
        } catch (UnirestException e) {
            throw new KeySupplierException(e);
        }
    }
}
