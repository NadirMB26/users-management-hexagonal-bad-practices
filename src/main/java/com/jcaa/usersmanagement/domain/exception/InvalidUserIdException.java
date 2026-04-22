package com.jcaa.usersmanagement.domain.exception;

// Clean Code - Regla 9 (constructores controlados):
//   Se mantiene constructor privado para forzar el uso de factory methods.
//   Esto asegura que la excepción solo se cree con mensajes predefinidos.
//
// Clean Code - Regla 10 (evitar texto hardcodeado):
//   El mensaje de error se movió a una constante privada con nombre descriptivo.
//   Esto evita duplicación y facilita mantenimiento.

public final class InvalidUserIdException extends DomainException {

  // Constante de mensaje de error
  private static final String EMPTY_ID_MESSAGE =
      "The user id must not be empty.";

  // Constructor privado para forzar uso de factory methods
  private InvalidUserIdException(final String message) {
    super(message);
  }

  // Factory method expresivo
  public static InvalidUserIdException becauseValueIsEmpty() {
    return new InvalidUserIdException(EMPTY_ID_MESSAGE);
  }
}
