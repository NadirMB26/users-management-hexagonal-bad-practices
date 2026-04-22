package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidUserIdException;
import java.util.Objects;

// Clean Code aplicado:
// - Regla 4: se reemplazó == null por Objects.requireNonNull().
// - Regla 10: se eliminó texto hardcodeado, reemplazado por constante descriptiva.
// - Regla 15: inmutabilidad garantizada al ser record.
// - Regla 12: validación encapsulada en el propio value object.

public record UserId(String value) {

  private static final String NULL_ID_MESSAGE = "UserId cannot be null";

  public UserId {
    final String normalizedValue =
        Objects.requireNonNull(value, NULL_ID_MESSAGE).trim();
    validateNotEmpty(normalizedValue);
    value = normalizedValue;
  }

  private static void validateNotEmpty(final String normalizedValue) {
    if (normalizedValue.isEmpty()) {
      throw InvalidUserIdException.becauseValueIsEmpty();
    }
  }
}
