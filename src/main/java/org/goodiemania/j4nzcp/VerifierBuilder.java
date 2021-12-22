package org.goodiemania.j4nzcp;

import org.goodiemania.j4nzcp.impl.VerifierImpl;
import org.goodiemania.j4nzcp.impl.key.KeySupplier;
import org.goodiemania.j4nzcp.impl.key.OfflineStaticKeySupplier;
import org.goodiemania.j4nzcp.impl.key.UnirestKeySupplier;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Builder for NZCP verifier
 */
public class VerifierBuilder {
    private Set<String> trustedIssuers = new HashSet<>(List.of("nzcp.identity.health.nz"));
    private KeySupplier customKeySupplier;
    private RequestedKeySupplier requestedKeySupplier = RequestedKeySupplier.ONLINE_CACHED;

    /**
     * Lets users add a trusted issuer
     * <p>
     * Use with caution, this can result in your pass verifier being insecure
     *
     * @param issuer a new issuer to be added to the set of trusted issuers
     * @return This object, for fluent chaining of methods
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
     * @return This object, for fluent chaining of methods
     */
    public VerifierBuilder setTrustedIssuer(final Set<String> issuers) {
        trustedIssuers = issuers;
        return this;
    }

    /**
     * Use the offline verifier
     *
     * @return This object, for fluent chaining of methods
     */
    public VerifierBuilder useOfflineCertificateSupplier() {
        requestedKeySupplier = RequestedKeySupplier.OFFLINE;
        return this;
    }

    /**
     * Use the online (unirest) certificate verifier
     *
     * @return This object, for fluent chaining of methods
     */
    public VerifierBuilder useOnlineCertificateSupplier() {
        requestedKeySupplier = RequestedKeySupplier.ONLINE;
        return this;
    }

    /**
     * Allows users to pass in a supplier, if they so choose
     * <p>
     * This should take a string representing the domain of the issuer, and return a key details object representing the results
     *
     * @param keySupplier custom key supplier to be used by the verifier
     * @return This object, for fluent chaining of methods
     */
    public VerifierBuilder useCustomCertificateSupplier(final KeySupplier keySupplier) {
        requestedKeySupplier = RequestedKeySupplier.CUSTOM;
        customKeySupplier = keySupplier;
        return this;
    }

    /**
     * Allows for fluent use of the builder, but also including a if statement in a lamba
     * <p>
     * I saw this in kotlin and kinda liked it
     *
     * @param consumer the consumer to be run now, with this builder
     * @return This object, for fluent chaining of methods
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
            case ONLINE_CACHED, ONLINE -> new UnirestKeySupplier(); //TODO update
            case OFFLINE -> new OfflineStaticKeySupplier();
        };
        return new VerifierImpl(trustedIssuers, keySupplier);
    }

    private enum RequestedKeySupplier {
        ONLINE_CACHED,
        ONLINE,
        OFFLINE,
        CUSTOM,
    }
}
