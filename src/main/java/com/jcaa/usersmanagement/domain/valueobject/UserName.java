package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidUserNameException;
import java.util.Objects;

// Clean Code aplicado:
// - Regla 4: se reemplazó == null por Objects.requireNonNull().
// - Regla 10: se eliminó el magic number, reemplazado por constante descriptiva MINIMUM_LENGTH.
// - Regla 15: inmutabilidad garantizada al ser record.
// - Regla 12: validación encapsulada en el propio value object.

public record UserName(String value) {

  private static final String NULL_NAME_MESSAGE = "UserName cannot be null";
  private static final int MINIMUM_LENGTH = 3;

  public UserName {
    final String normalizedValue =
        Objects.requireNonNull(value, NULL_NAME_MESSAGE).trim();
    validateNotEmpty(normalizedValue);
    validateMinimumLength(normalizedValue);
    value = normalizedValue;
  }

  private static void validateNotEmpty(final String normalizedValue) {
    if (normalizedValue.isEmpty()) {
      throw InvalidUserNameException.becauseValueIsEmpty();
    }
  }

  private static void validateMinimumLength(final String normalizedValue) {
    if (normalizedValue.length() < MINIMUM_LENGTH) {
      throw InvalidUserNameException.becauseLengthIsTooShort(MINIMUM_LENGTH);
    }
  }
}
