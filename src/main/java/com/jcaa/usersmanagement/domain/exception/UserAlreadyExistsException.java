package com.jcaa.usersmanagement.domain.exception;

// Clean Code - Regla 9 (constructores controlados):
//   Se mantiene constructor privado para forzar el uso de factory methods.
//   Esto asegura que la excepción solo se cree con mensajes predefinidos.
//
// Clean Code - Regla 10 (evitar texto hardcodeado):
//   El mensaje de error se movió a una constante privada con nombre descriptivo.
//   Esto evita duplicación y facilita mantenimiento.

public final class UserAlreadyExistsException extends DomainException {

  // Constante de mensaje de error
  private static final String EMAIL_ALREADY_EXISTS_MESSAGE =
      "A user with email '%s' already exists.";

  // Constructor privado para forzar uso de factory methods
  private UserAlreadyExistsException(final String message) {
    super(message);
  }

  // Factory method expresivo
  public static UserAlreadyExistsException becauseEmailAlreadyExists(final String email) {
    return new UserAlreadyExistsException(String.format(EMAIL_ALREADY_EXISTS_MESSAGE, email));
  }
}
