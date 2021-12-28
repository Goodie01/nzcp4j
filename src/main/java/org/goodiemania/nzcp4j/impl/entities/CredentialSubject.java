package org.goodiemania.nzcp4j.impl.entities;

import java.time.LocalDate;

public record CredentialSubject(String givenName, String familyName, LocalDate dob) {
}
