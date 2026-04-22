package com.jcaa.usersmanagement.domain.exception;

// Clean Code - Regla 9 (constructores controlados):
//   Se mantiene constructor privado para forzar el uso de factory methods.
//   Esto asegura que la excepción solo se cree con mensajes predefinidos.
//
// Clean Code - Regla 10 (evitar texto hardcodeado):
//   El mensaje de error se movió a una constante privada con nombre descriptivo.
//   Esto evita duplicación y facilita mantenimiento.

public final class UserNotFoundException extends DomainException {

  // Constante de mensaje de error
  private static final String USER_NOT_FOUND_MESSAGE =
      "The user with id '%s' was not found.";

  // Constructor privado para forzar uso de factory methods
  private UserNotFoundException(final String message) {
    super(message);
  }

  // Factory method expresivo
  public static UserNotFoundException becauseIdWasNotFound(final String userId) {
    return new UserNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, userId));
  }
}
