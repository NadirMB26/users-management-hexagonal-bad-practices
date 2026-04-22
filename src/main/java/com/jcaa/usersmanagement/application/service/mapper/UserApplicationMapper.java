package com.jcaa.usersmanagement.application.service.mapper;

import com.jcaa.usersmanagement.application.service.dto.command.CreateUserCommand;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteUserCommand;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateUserCommand;
import com.jcaa.usersmanagement.application.service.dto.query.GetUserByIdQuery;
import com.jcaa.usersmanagement.domain.enums.UserRole;
import com.jcaa.usersmanagement.domain.enums.UserStatus;
import com.jcaa.usersmanagement.domain.model.UserModel;
import com.jcaa.usersmanagement.domain.valueobject.UserEmail;
import com.jcaa.usersmanagement.domain.valueobject.UserId;
import com.jcaa.usersmanagement.domain.valueobject.UserName;
import com.jcaa.usersmanagement.domain.valueobject.UserPassword;
import java.util.Objects;
import java.util.Optional;

// Clean Code - Regla 24 (consistencia semántica):
// El email del usuario se llamaba "correo" en fromCreateCommandToModel
// y "correoElectronico" en fromUpdateCommandToModel sin ninguna justificación.
// Se unificó el nombre a "email" en ambos métodos para mantener consistencia.
//
// Clean Code - Regla 21 (no retornar códigos de error):
// roleToCode() retornaba -1 para representar rol inválido o ausente.
// Usar valores especiales como -1 es ambiguo: el llamador no sabe si significa
// error, ausencia o caso no contemplado. Se cambió para retornar Optional<Integer>
// con semántica clara: vacío significa rol no reconocido.
public class UserApplicationMapper {

  public static UserModel fromCreateCommandToModel(final CreateUserCommand command) {
    final String userId   = command.id();
    final String userName = command.name();
    final String email    = command.email();
    final String userPass = command.password();
    final String userRole = command.role();

    return UserModel.create(
        new UserId(userId),
        new UserName(userName),
        new UserEmail(email),
        UserPassword.fromPlainText(userPass),
        UserRole.fromString(userRole));
  }

  public static UserModel fromUpdateCommandToModel(
      final UpdateUserCommand command, final UserPassword currentPassword) {

    final UserPassword passwordToUse = resolvePassword(command, currentPassword);
    final String email = command.email();

    return new UserModel(
        new UserId(command.id()),
        new UserName(command.name()),
        new UserEmail(email),
        passwordToUse,
        UserRole.fromString(command.role()),
        UserStatus.fromString(command.status()));
  }

  public static UserId fromGetUserByIdQueryToUserId(final GetUserByIdQuery query) {
    return new UserId(query.id());
  }

  public static UserId fromDeleteCommandToUserId(final DeleteUserCommand command) {
    return new UserId(command.id());
  }

  public static Optional<Integer> roleToCode(final String role) {
    if (Objects.isNull(role) || role.isBlank()) {
      return Optional.empty();
    }
    if ("ADMIN".equalsIgnoreCase(role)) {
      return Optional.of(1);
    } else if ("MEMBER".equalsIgnoreCase(role)) {
      return Optional.of(2);
    } else if ("REVIEWER".equalsIgnoreCase(role)) {
      return Optional.of(3);
    }
    return Optional.empty();
  }

  private static UserPassword resolvePassword(
      final UpdateUserCommand command, final UserPassword currentPassword) {
    if (command.password() == null || command.password().isBlank()) {
      return currentPassword;
    }
    return UserPassword.fromPlainText(command.password());
  }
}