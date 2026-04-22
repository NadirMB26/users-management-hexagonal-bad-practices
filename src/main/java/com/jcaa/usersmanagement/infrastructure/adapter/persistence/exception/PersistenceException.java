// VIOLACIONES DETECTADAS:
// - Regla 10: todos los mensajes de error estaban hardcodeados directamente en los métodos fábrica.
//   Se corrigió definiendo constantes privadas con nombre descriptivo para cada mensaje.

package com.jcaa.usersmanagement.infrastructure.adapter.persistence.exception;

public final class PersistenceException extends RuntimeException {

  // Constantes descriptivas para mensajes de error
  private static final String SAVE_FAILED = "Failed to save user with ID: '%s'.";
  private static final String UPDATE_FAILED = "Failed to update user with ID: '%s'.";
  private static final String FIND_BY_ID_FAILED = "Failed to find user with ID: '%s'.";
  private static final String FIND_BY_EMAIL_FAILED = "Failed to find user with email: '%s'.";
  private static final String FIND_ALL_FAILED = "Failed to retrieve all users.";
  private static final String DELETE_FAILED = "Failed to delete user with ID: '%s'.";
  private static final String CONNECTION_FAILED = "Could not establish database connection.";

  private PersistenceException(final String message, final Throwable cause) {
    super(message, cause);
  }



  public static PersistenceException becauseSaveFailed(final String userId, final Throwable cause) {
    // CORREGIDO Regla 10: se usa constante descriptiva en lugar de string literal
    return new PersistenceException(String.format(SAVE_FAILED, userId), cause);
  }

  public static PersistenceException becauseUpdateFailed(final String userId, final Throwable cause) {
    return new PersistenceException(String.format(UPDATE_FAILED, userId), cause);
  }

  public static PersistenceException becauseFindByIdFailed(final String userId, final Throwable cause) {
    return new PersistenceException(String.format(FIND_BY_ID_FAILED, userId), cause);
  }

  public static PersistenceException becauseFindByEmailFailed(final String email, final Throwable cause) {
    return new PersistenceException(String.format(FIND_BY_EMAIL_FAILED, email), cause);
  }

  public static PersistenceException becauseFindAllFailed(final Throwable cause) {
    return new PersistenceException(FIND_ALL_FAILED, cause);
  }

  public static PersistenceException becauseDeleteFailed(final String userId, final Throwable cause) {
    return new PersistenceException(String.format(DELETE_FAILED, userId), cause);
  }

  public static PersistenceException becauseConnectionFailed(final Throwable cause) {
    return new PersistenceException(CONNECTION_FAILED, cause);
  }

public static PersistenceException becauseDatabaseConnectionFailed(final Throwable cause) {
    // CORREGIDO Regla 10: se usa constante descriptiva en lugar de string literal
    return new PersistenceException(CONNECTION_FAILED, cause);
}

}
