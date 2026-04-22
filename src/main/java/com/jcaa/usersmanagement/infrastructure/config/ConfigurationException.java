// VIOLACIONES DETECTADAS:
// - Regla 10: texto de error hardcodeado directamente en el método fábrica.
//   Se corrigió definiendo una constante privada con nombre descriptivo en lugar de un String literal.

package com.jcaa.usersmanagement.infrastructure.config;

public final class ConfigurationException extends RuntimeException {

  // Constante descriptiva para mensaje de error
  private static final String LOAD_FAILED = "Failed to load the application configuration.";

  private ConfigurationException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public static ConfigurationException becauseLoadFailed(final Throwable cause) {
    // CORREGIDO Regla 10: se usa constante descriptiva en lugar de string literal
    return new ConfigurationException(LOAD_FAILED, cause);
  }
}
