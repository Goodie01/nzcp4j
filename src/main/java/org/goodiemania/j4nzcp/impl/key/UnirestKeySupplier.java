package org.goodiemania.j4nzcp.impl.key;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.goodiemania.j4nzcp.Nzcp4JException;
import org.goodiemania.j4nzcp.exception.InvalidKeyException;
import org.goodiemania.j4nzcp.exception.KeySupplierException;
import org.goodiemania.j4nzcp.impl.entities.PublicKeysDetails;
import org.json.JSONObject;

public class UnirestKeySupplier implements KeySupplier {
    @Override
    public PublicKeysDetails getPublicKeyDetails(String issuer, String kid) throws Nzcp4JException {
        try {
            String url = "https://" + issuer + "/.well-known/did.json";
            JSONObject jsonKeyObject = Unirest.get(url).asJson().getBody().getObject()
                    .getJSONArray("verificationMethod")
                    .getJSONObject(0);
            if (!jsonKeyObject.getString("id").endsWith("#" + kid)) {
                throw new InvalidKeyException("Unavailable key");
            }
            JSONObject keyObject = jsonKeyObject.getJSONObject("publicKeyJwk");
            return new PublicKeysDetails(
                    keyObject.getString("kty"),
                    keyObject.getString("crv"),
                    keyObject.getString("x"),
                    keyObject.getString("y"));
        } catch (UnirestException e) {
            throw new KeySupplierException(e);
        }
    }
}
