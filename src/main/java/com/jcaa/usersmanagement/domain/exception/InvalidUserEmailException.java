package com.jcaa.usersmanagement.domain.exception;

// Clean Code - Regla 9 (constructores controlados):
//   Se mantiene constructor privado para forzar el uso de factory methods.
//   Esto asegura que la excepción solo se cree con mensajes predefinidos.
//
// Clean Code - Regla 10 (evitar texto hardcodeado):
//   Los mensajes de error se movieron a constantes privadas con nombre descriptivo.
//   Esto evita duplicación y facilita mantenimiento.

public final class InvalidUserEmailException extends DomainException {

  // Constantes de mensajes de error
  private static final String EMPTY_EMAIL_MESSAGE =
      "The user email must not be empty.";
  private static final String INVALID_FORMAT_MESSAGE =
      "The user email format is invalid: '%s'.";

  // Constructor privado para forzar uso de factory methods
  private InvalidUserEmailException(final String message) {
    super(message);
  }

  // Factory methods expresivos
  public static InvalidUserEmailException becauseValueIsEmpty() {
    return new InvalidUserEmailException(EMPTY_EMAIL_MESSAGE);
  }

  public static InvalidUserEmailException becauseFormatIsInvalid(final String email) {
    return new InvalidUserEmailException(String.format(INVALID_FORMAT_MESSAGE, email));
  }
}
