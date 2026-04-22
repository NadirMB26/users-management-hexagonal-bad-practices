package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.LoginUseCase;
import com.jcaa.usersmanagement.application.port.out.GetUserByEmailPort;
import com.jcaa.usersmanagement.application.service.dto.command.LoginCommand;
import com.jcaa.usersmanagement.domain.exception.InvalidCredentialsException;
import com.jcaa.usersmanagement.domain.model.UserModel;
import com.jcaa.usersmanagement.domain.valueobject.UserEmail;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

import java.util.Set;

// Clean Code - Reglas aplicadas:
// Regla 1 y 2 (una sola responsabilidad / funciones cortas):
//   Se dividió getAndValidateUser en métodos privados con nombres claros:
//   findUserByEmail(), verifyPassword(), ensureUserIsActive().
// Regla 8 (CQS):
//   Se eliminó la mezcla de consulta y efectos implícitos.
//   authenticateUser coordina pasos sin modificar estado oculto.
// Regla 12 (alta cohesión):
//   La lógica de "¿puede loguearse?" se delega al dominio con user.isAllowedToLogin().
// Regla 14 (Ley de Deméter):
//   Se reemplazó user.getPassword().verifyPlain() por user.passwordMatches().
// Regla 17 (condiciones limpias):
//   La condición redundante se encapsuló en un método expresivo del modelo.

@RequiredArgsConstructor
public final class LoginService implements LoginUseCase {

  private final GetUserByEmailPort getUserByEmailPort;
  private final Validator validator;

  @Override
  public UserModel execute(final LoginCommand command) {
    validateCommand(command);
    final UserEmail email = new UserEmail(command.email());
    return authenticateUser(email, command.password());
  }

  private UserModel authenticateUser(final UserEmail email, final String plainPassword) {
    final UserModel user = findUserByEmail(email);
    verifyPassword(user, plainPassword);
    ensureUserIsActive(user);
    return user;
  }

  private UserModel findUserByEmail(final UserEmail email) {
    return getUserByEmailPort.getByEmail(email)
        .orElseThrow(InvalidCredentialsException::becauseCredentialsAreInvalid);
  }

  private void verifyPassword(final UserModel user, final String plainPassword) {
    if (!user.passwordMatches(plainPassword)) {
      throw InvalidCredentialsException.becauseCredentialsAreInvalid();
    }
  }

  private void ensureUserIsActive(final UserModel user) {
    if (!user.isAllowedToLogin()) {
      throw InvalidCredentialsException.becauseUserIsNotActive();
    }
  }

  private void validateCommand(final LoginCommand command) {
    final Set<ConstraintViolation<LoginCommand>> violations = validator.validate(command);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }
}
