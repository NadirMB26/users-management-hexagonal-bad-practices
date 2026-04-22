package com.jcaa.usersmanagement.domain.exception;

// Clean Code - Regla 9 (constructores controlados):
//   Se mantiene constructor privado para forzar el uso de factory methods.
//   Esto asegura que la excepción solo se cree con mensajes predefinidos.
//
// Clean Code - Regla 10 (evitar texto hardcodeado):
//   Los mensajes de error se movieron a constantes privadas con nombre descriptivo.
//   Esto evita duplicación y facilita mantenimiento.

public final class InvalidCredentialsException extends DomainException {

  // Constantes de mensajes de error
  private static final String INVALID_CREDENTIALS_MESSAGE =
      "Correo o contraseña incorrectos.";
  private static final String USER_NOT_ACTIVE_MESSAGE =
      "Tu cuenta no está activa. Contacta al administrador.";

  // Constructor privado para forzar uso de factory methods
  private InvalidCredentialsException(final String message) {
    super(message);
  }

  // Factory methods expresivos
  public static InvalidCredentialsException becauseCredentialsAreInvalid() {
    return new InvalidCredentialsException(INVALID_CREDENTIALS_MESSAGE);
  }

  public static InvalidCredentialsException becauseUserIsNotActive() {
    return new InvalidCredentialsException(USER_NOT_ACTIVE_MESSAGE);
  }
}
