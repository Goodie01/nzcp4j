package org.goodiemania.j4nzcp;

import org.goodiemania.j4nzcp.impl.VerifierImpl;
import org.goodiemania.j4nzcp.impl.key.KeySupplier;
import org.goodiemania.j4nzcp.impl.key.UnirestKeySupplier;

import java.util.Set;
import java.util.function.Consumer;

/**
 * Builder for NZCP verifier
 */
public class VerifierBuilder {
    private KeySupplier keySupplier;

    /**
     * Lets users add a trusted issuer
     *
     * Use with caution, this can result in your pass verifier being insecure
     *
     * @param issuer
     * @return This object, for fluent chaining of methods
     */
    public VerifierBuilder addTrustedIssuer(final String issuer) {
        return this;
    }

    /**
     * Lets users set the full list of trusted issuers
     *
     * Use with caution, this can result in your pass verifier being insecure
     *
     * @param issuers
     * @return This object, for fluent chaining of methods
     */
    public VerifierBuilder setTrustedIssuer(final Set<String> issuers) {
        return this;
    }

    /**
     * Use the offline verifier
     * @return This object, for fluent chaining of methods
     */
    public VerifierBuilder useOfflineCertificateSupplier() {
        return this;
    }

    /**
     * Use the online (unirest) certificate verifier
     * @return This object, for fluent chaining of methods
     */
    public VerifierBuilder useOnlineCertificateSupplier() {
        return this;
    }

    /**
     * Allows users to pass in a supplier, if they so choose
     *
     * This should take a string representing the domain of the issuer, and return a key details object representing the results
     *
     * @param keySupplier
     * @return This object, for fluent chaining of methods
     */
    public VerifierBuilder useCustomCertificateSupplier(final KeySupplier keySupplier) {
        this.keySupplier = keySupplier;
        return this;
    }

    /**
     * Allows for fluent use of the builder, but also including a if statement in a lamba
     *
     * I saw this in kotlin and kinda liked it
     *
     * @param consumer
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
        if(keySupplier == null) {
            keySupplier = new UnirestKeySupplier();
        }
        return new VerifierImpl();
    }
}
