package org.goodiemania.j4nzcp.impl.entities;

import java.time.LocalDate;

public record CredentialSubject(String givenName, String familyName, LocalDate dob) {
}
