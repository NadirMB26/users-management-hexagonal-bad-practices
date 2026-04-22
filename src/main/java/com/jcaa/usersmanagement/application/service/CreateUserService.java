package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.CreateUserUseCase;
import com.jcaa.usersmanagement.application.port.out.GetUserByEmailPort;
import com.jcaa.usersmanagement.application.port.out.SaveUserPort;
import com.jcaa.usersmanagement.application.service.dto.command.CreateUserCommand;
import com.jcaa.usersmanagement.application.service.mapper.UserApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.UserAlreadyExistsException;
import com.jcaa.usersmanagement.domain.model.UserModel;
import com.jcaa.usersmanagement.domain.valueobject.UserEmail;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.util.Set;

// Clean Code - Reglas 1, 2, 3, 9, 10 / Guía Hexagonal - Regla 6 (Logging):
// El método execute() original mezclaba validación de constraints, log de PII,
// verificación de negocio, construcción manual de dominio, persistencia y notificación
// en un solo bloque largo con comentarios que tapaban código poco expresivo.
// Se extrajo cada responsabilidad en un método privado con nombre descriptivo,
// se eliminaron los comentarios redundantes y se eliminó el log del email (dato PII).
@Log
@RequiredArgsConstructor
public final class CreateUserService implements CreateUserUseCase {

  private final SaveUserPort saveUserPort;
  private final GetUserByEmailPort getUserByEmailPort;
  private final EmailNotificationService emailNotificationService;
  private final UserApplicationMapper mapper;
  private final Validator validator;

  @Override
  public UserModel execute(final CreateUserCommand command) {
    validateCommand(command);
    ensureEmailIsNotRegistered(command.email());
    final UserModel userToSave = UserApplicationMapper.fromCreateCommandToModel(command);
    final UserModel savedUser = saveUserPort.save(userToSave);
    emailNotificationService.notifyUserCreated(savedUser, command.password());
    return savedUser;
  }

  private void validateCommand(final CreateUserCommand command) {
    final Set<ConstraintViolation<CreateUserCommand>> violations = validator.validate(command);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }

  private void ensureEmailIsNotRegistered(final String email) {
    final UserEmail userEmail = new UserEmail(email);
    if (getUserByEmailPort.getByEmail(userEmail).isPresent()) {
      throw UserAlreadyExistsException.becauseEmailAlreadyExists(userEmail.value());
    }
  }
}