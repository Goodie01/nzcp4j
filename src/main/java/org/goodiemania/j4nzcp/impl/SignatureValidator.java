package org.goodiemania.j4nzcp.impl;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import org.goodiemania.j4nzcp.impl.entities.NewZealandCovidPass;
import org.json.JSONObject;

public class SignatureValidator {
    public void validate(final NewZealandCovidPass covidPass) {
        String substring = "http://" + covidPass.payload().iss().substring(8) + "/.well-known/did.json";
        GetRequest getRequest = Unirest.get(substring);
        try {
            Unirest.get(substring).asJson().getBody().getObject()
                    .getJSONArray("verificationMethod")
                    .getJSONObject(0)
                    .getJSONObject("publicKeyJwk")
                    .get("kty");

            System.out.println(substring);
        } catch (UnirestException e) {
            e.printStackTrace();
        }


        //TODO next we need to process the pass.payload().iss() and turn it into https://nzcp.covid19.health.nz/.well-known/did.json
        // Pull down that given key, and check the hash
    }
}
