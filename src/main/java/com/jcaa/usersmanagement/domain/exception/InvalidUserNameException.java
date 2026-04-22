package com.jcaa.usersmanagement.domain.exception;

// Clean Code - Regla 9 (constructores controlados):
//   Se mantiene constructor privado para forzar el uso de factory methods.
//   Esto asegura que la excepción solo se cree con mensajes predefinidos.
//
// Clean Code - Regla 10 (evitar texto hardcodeado):
//   Los mensajes de error se movieron a constantes privadas con nombre descriptivo.
//   Esto evita duplicación y facilita mantenimiento.

public final class InvalidUserNameException extends DomainException {

  // Constantes de mensajes de error
  private static final String EMPTY_NAME_MESSAGE =
      "The user name must not be empty.";
  private static final String TOO_SHORT_MESSAGE =
      "The user name must have at least %d characters.";

  // Constructor privado para forzar uso de factory methods
  private InvalidUserNameException(final String message) {
    super(message);
  }

  // Factory methods expresivos
  public static InvalidUserNameException becauseValueIsEmpty() {
    return new InvalidUserNameException(EMPTY_NAME_MESSAGE);
  }

  public static InvalidUserNameException becauseLengthIsTooShort(final int minimumLength) {
    return new InvalidUserNameException(String.format(TOO_SHORT_MESSAGE, minimumLength));
  }
}
