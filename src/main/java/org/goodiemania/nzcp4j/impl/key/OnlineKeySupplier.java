package org.goodiemania.nzcp4j.impl.key;

import java.io.IOException;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import org.goodiemania.nzcp4j.KeySupplier;
import org.goodiemania.nzcp4j.exceptions.KeySupplierException;

public class OnlineKeySupplier implements KeySupplier {
    private final HttpClient httpClient;
    private final boolean cacheResult;
    private String cachedResult;

    public OnlineKeySupplier(final ProxySelector proxySelector, final int timeout, final boolean cacheResult) {
        this.cacheResult = cacheResult;
        HttpClient.Builder httpClientBuilder = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(timeout));

        if (proxySelector != null) {
            httpClientBuilder.proxy(proxySelector);
        }

        httpClient = httpClientBuilder.build();
    }

    @Override
    public String get(final String url) throws KeySupplierException {
        if (cacheResult && cachedResult != null) {
            return cachedResult;
        }
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(url)).build();
            HttpResponse<String> send = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            String response = send.body();
            if (cacheResult) {
                cachedResult = response;
            }
            return response;
        } catch (IOException | InterruptedException e) {
            throw new KeySupplierException(e);
        }
    }
}
