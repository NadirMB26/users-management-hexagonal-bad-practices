package com.jcaa.usersmanagement.domain.valueobject;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.jcaa.usersmanagement.domain.exception.InvalidUserPasswordException;
import java.util.Objects;

// Clean Code aplicado:
// - Regla 4: se reemplazó == null por Objects.requireNonNull().
// - Regla 10: se eliminaron magic numbers, reemplazados por constantes descriptivas MINIMUM_LENGTH y BCRYPT_COST.
// - Regla 15: inmutabilidad garantizada al ser final.
// - Regla 12: validación encapsulada en el propio value object.

public final class UserPassword {

  private static final int MINIMUM_LENGTH = 8;
  private static final int BCRYPT_COST = 12;

  private static final String NULL_PASSWORD_MESSAGE = "Password cannot be null";
  private static final String NULL_HASH_MESSAGE = "Password hash cannot be null";
  private static final String NULL_PLAIN_MESSAGE = "Plain password cannot be null";

  private final String value;

  private UserPassword(final String value) {
    this.value = value;
  }

  /**
   * Crea un UserPassword desde texto plano: valida y aplica hash BCrypt.
   * Usar cuando el usuario crea o cambia su contraseña.
   */
  public static UserPassword fromPlainText(final String plainText) {
    final String normalizedValue =
        Objects.requireNonNull(plainText, NULL_PASSWORD_MESSAGE).trim();
    validateNotEmpty(normalizedValue);
    validateMinimumLength(normalizedValue);
    final String hash = BCrypt.withDefaults().hashToString(BCRYPT_COST, normalizedValue.toCharArray());
    return new UserPassword(hash);
  }

  /**
   * Crea un UserPassword desde un hash ya almacenado en base de datos.
   * No re-valida ni re-hashea.
   */
  public static UserPassword fromHash(final String hash) {
    Objects.requireNonNull(hash, NULL_HASH_MESSAGE);
    return new UserPassword(hash);
  }

  /** Verifica un texto plano contra el hash BCrypt almacenado. */
  public boolean verifyPlain(final String plainText) {
    final String normalizedPlain =
        Objects.requireNonNull(plainText, NULL_PLAIN_MESSAGE).trim();
    final BCrypt.Result result = BCrypt.verifyer().verify(normalizedPlain.toCharArray(), value);
    return result.verified;
  }

  public String value() {
    return value;
  }

  @Override
  public boolean equals(final Object other) {
    if (this == other) return true;
    if (!(other instanceof UserPassword userPassword)) return false;
    return Objects.equals(value, userPassword.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  private static void validateNotEmpty(final String normalizedValue) {
    if (normalizedValue.isEmpty()) {
      throw InvalidUserPasswordException.becauseValueIsEmpty();
    }
  }

  private static void validateMinimumLength(final String normalizedValue) {
    if (normalizedValue.length() < MINIMUM_LENGTH) {
      throw InvalidUserPasswordException.becauseLengthIsTooShort(MINIMUM_LENGTH);
    }
  }
}
