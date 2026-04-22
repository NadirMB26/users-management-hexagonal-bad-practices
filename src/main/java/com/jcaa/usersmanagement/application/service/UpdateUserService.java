package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.UpdateUserUseCase;
import com.jcaa.usersmanagement.application.port.out.GetUserByEmailPort;
import com.jcaa.usersmanagement.application.port.out.GetUserByIdPort;
import com.jcaa.usersmanagement.application.port.out.UpdateUserPort;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateUserCommand;
import com.jcaa.usersmanagement.application.service.mapper.UserApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.UserAlreadyExistsException;
import com.jcaa.usersmanagement.domain.exception.UserNotFoundException;
import com.jcaa.usersmanagement.domain.model.UserModel;
import com.jcaa.usersmanagement.domain.valueobject.UserEmail;
import com.jcaa.usersmanagement.domain.valueobject.UserId;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.util.Set;

// Clean Code - Reglas aplicadas:
// Regla 8 (CQS): execute mezclaba comando y consulta. Se recomienda separar responsabilidades.
// Regla 6 (boolean flag): notifyIfRequired usaba un parámetro booleano para dos comportamientos distintos.
// Regla 7 (efecto secundario oculto): el nombre notifyIfRequired no comunicaba que también hacía logging.
// Regla 17 (condiciones limpias): condición booleana excesiva y redundante en ensureEmailIsNotTakenByAnotherUser.
// Regla 25/26/27: se evitó sobrecompactación y se mejoró la claridad con métodos expresivos.

@Log
@RequiredArgsConstructor
public final class UpdateUserService implements UpdateUserUseCase {

  private final UpdateUserPort updateUserPort;
  private final GetUserByIdPort getUserByIdPort;
  private final GetUserByEmailPort getUserByEmailPort;
  private final EmailNotificationService emailNotificationService;
  private final Validator validator;

  @Override
  public UserModel execute(final UpdateUserCommand command) {
    validateCommand(command);

    log.info("Actualizando usuario id=" + command.id() + ", email=" + command.email() + ", nombre=" + command.name());

    final UserId userId = new UserId(command.id());
    final UserModel current = findExistingUserOrFail(userId);
    final UserEmail newEmail = new UserEmail(command.email());

    ensureEmailIsNotTakenByAnotherUser(newEmail, userId);

    final UserModel userToUpdate =
        UserApplicationMapper.fromUpdateCommandToModel(command, current.getPassword());
    final UserModel updatedUser = updateUserPort.update(userToUpdate);

    notifyUserUpdated(updatedUser);

    return updatedUser;
  }

  // Se eliminó el boolean flag y se crearon métodos separados
  private void notifyUserUpdated(final UserModel user) {
    emailNotificationService.notifyUserUpdated(user);
  }

  private void validateCommand(final UpdateUserCommand command) {
    final Set<ConstraintViolation<UpdateUserCommand>> violations = validator.validate(command);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }

  private UserModel findExistingUserOrFail(final UserId userId) {
    return getUserByIdPort
        .getById(userId)
        .orElseThrow(() -> UserNotFoundException.becauseIdWasNotFound(userId.value()));
  }

  private void ensureEmailIsNotTakenByAnotherUser(final UserEmail newEmail, final UserId ownerId) {
    getUserByEmailPort.getByEmail(newEmail).ifPresent(existingUser -> {
      boolean isDifferentUser = !existingUser.getId().equals(ownerId);
      if (isDifferentUser) {
        throw UserAlreadyExistsException.becauseEmailAlreadyExists(newEmail.value());
      }
    });
  }
}
