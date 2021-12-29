package org.goodiemania.nzcp4j;

import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import org.goodiemania.nzcp4j.impl.VerifierImpl;
import org.goodiemania.nzcp4j.impl.key.OfflineStaticKeySupplier;
import org.goodiemania.nzcp4j.impl.key.OnlineKeySupplier;

/**
 * Builder for NZCP verifier
 */
public class VerifierBuilder {
    private Set<String> trustedIssuers = new HashSet<>(Set.of("nzcp.identity.health.nz"));
    private KeySupplier customKeySupplier;
    private RequestedKeySupplier requestedKeySupplier = RequestedKeySupplier.ONLINE;
    private int onlineTimeout = 20;
    private ProxySelector onlineProxy = null;
    private boolean onlineCacheResult = true;

    /**
     * Lets users add a trusted issuer
     * <p>
     * Use with caution, this can result in your pass verifier being insecure
     *
     * @param issuer a new issuer to be added to the set of trusted issuers
     * @return this builder
     */
    public VerifierBuilder addTrustedIssuer(final String issuer) {
        trustedIssuers.add(issuer);
        return this;
    }

    /**
     * Lets users set the full list of trusted issuers
     * <p>
     * Use with caution, this can result in your pass verifier being insecure
     *
     * @param issuers a set of strings representing trusted issuers, to override the current trusted issuers
     * @return this builder
     */
    public VerifierBuilder setTrustedIssuers(final Set<String> issuers) {
        trustedIssuers = issuers;
        return this;
    }

    /**
     * Use the offline verifier, hard coded response
     *
     * @return this builder
     */
    public VerifierBuilder useOfflineKeySupplier() {
        requestedKeySupplier = RequestedKeySupplier.OFFLINE;
        return this;
    }

    /**
     * Use the online certificate verifier
     *
     * @return this builder
     */
    public VerifierBuilder useOnlineKeySupplier() {
        requestedKeySupplier = RequestedKeySupplier.ONLINE;
        return this;
    }

    /**
     * Allows users to pass in a supplier, if they so choose
     * <p>
     * This should take a string representing the domain of the issuer, and return a key details object representing the results
     *
     * @param keySupplier custom key supplier to be used by the verifier
     * @return this builder
     */
    public VerifierBuilder useCustomKeySupplier(final KeySupplier keySupplier) {
        requestedKeySupplier = RequestedKeySupplier.CUSTOM;
        customKeySupplier = keySupplier;
        return this;
    }

    /**
     * When using the online key supplier set the timeout for HTTP requests
     * <p>
     * Defaults to 20.
     *
     * @return this builder
     */
    public VerifierBuilder setOnlineTimeout(final int timeout) {
        this.onlineTimeout = timeout;
        return this;
    }

    /**
     * When using the online key supplier set the proxy host and proxy port for use.
     *
     * @param proxyHost Host of the proxy
     * @param proxyPort Port of the proxy
     * @return this builder
     * @implNote Constructs an instance of proxy selector and calls an overloaded method.
     */
    public VerifierBuilder setOnlineProxy(final String proxyHost, final int proxyPort) {
        return setOnlineProxy(ProxySelector.of(new InetSocketAddress(proxyHost, proxyPort)));
    }

    /**
     * When using the online key supplier set the proxy for use
     * <p>
     * If needed to explicitly say to use no proxy you can pass in either ProxySelector.of(null) or null.
     *
     * @param proxy Proxy selector to use
     * @return this builder
     */
    public VerifierBuilder setOnlineProxy(final ProxySelector proxy) {
        this.onlineProxy = proxy;
        return this;
    }

    /**
     * When using the online key supplier set if we should store and cache the results on first look up.
     * <p>
     * Defaults to true.
     *
     * @param cacheResult Whether to cache the online result
     * @return this builder
     * @implNote Constructs an instance of proxy selector and calls an overloaded method.
     */
    public VerifierBuilder cacheOnlineResult(final boolean cacheResult) {
        onlineCacheResult = cacheResult;
        return this;
    }

    /**
     * Allows for fluent use of the builder, but also including a if statement in a lamba
     * <p>
     * I saw this in kotlin and kinda liked it, can be used as below
     *
     * <pre>
     *     Verifier.builder()
     *      .setOnlineTimeout(60)
     *      .apply(verifierBuilder -> { if(proxy != null) { verifierBuilder.useOnlineProxy(proxy); }})
     *      .build();
     * </pre>
     *
     * @param consumer the consumer to be run now, with this builder
     * @return this builder
     */
    public VerifierBuilder apply(Consumer<VerifierBuilder> consumer) {
        consumer.accept(this);
        return this;
    }

    /**
     * Constructs an instance of verifier and returns it
     *
     * @return The built verifier, ready to use
     */
    public Verifier build() {
        final KeySupplier keySupplier = switch (requestedKeySupplier) {
            case CUSTOM -> customKeySupplier;
            case ONLINE -> new OnlineKeySupplier(onlineProxy, onlineTimeout, onlineCacheResult);
            case OFFLINE -> new OfflineStaticKeySupplier();
        };
        return new VerifierImpl(trustedIssuers, keySupplier);
    }

    private enum RequestedKeySupplier {
        ONLINE,
        OFFLINE,
        CUSTOM,
    }
}
