package org.goodiemania.nzcp4j.impl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record Root(
    String id,
    @JsonProperty("@context") List<String> context,
    List<VerificationMethod> verificationMethod,
    List<String> assertionMethod) {

}
