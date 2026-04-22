package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidUserEmailException;
import java.util.Objects;
import java.util.regex.Pattern;

// Clean Code aplicado:
// - Regla 6: se eliminó el Logger, el dominio no debe tener logs ni exponer PII.
// - Regla 10: se reemplazaron textos hardcodeados por constantes descriptivas.
// - Regla 23: se centralizó la validación de email en este value object, evitando conocimiento disperso.

public record UserEmail(String value) {

  private static final Pattern EMAIL_PATTERN =
      Pattern.compile("^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$");

  private static final String EMPTY_EMAIL_MESSAGE =
      "The user email must not be empty.";
  private static final String INVALID_FORMAT_MESSAGE =
      "The user email format is invalid: '%s'.";

  public UserEmail {
    final String normalizedValue =
        Objects.requireNonNull(value, EMPTY_EMAIL_MESSAGE).trim().toLowerCase();
    validateNotEmpty(normalizedValue);
    validateFormat(normalizedValue);
    value = normalizedValue;
  }

  private static void validateNotEmpty(final String normalizedValue) {
    if (normalizedValue.isEmpty()) {
      throw InvalidUserEmailException.becauseValueIsEmpty();
    }
  }

  private static void validateFormat(final String normalizedValue) {
    if (!EMAIL_PATTERN.matcher(normalizedValue).matches()) {
      throw InvalidUserEmailException.becauseFormatIsInvalid(normalizedValue);
    }
  }

  @Override
  public String toString() {
    return value;
  }
}
