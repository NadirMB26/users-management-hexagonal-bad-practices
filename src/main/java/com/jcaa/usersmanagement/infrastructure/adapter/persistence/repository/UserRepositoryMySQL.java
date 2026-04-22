// VIOLACIONES DETECTADAS:
// - Regla 4: Se usó import con wildcard (*) en java.util.*. Esto oculta qué clases se usan realmente.
//   Se corrigió reemplazando por imports específicos.
// - Regla 4: El mapper se instancia directamente porque no estaba anotado con @UtilityClass.
//   Con la corrección previa en UserPersistenceMapper, ahora debe usarse como clase utilitaria estática.
// - Regla 5: Método saveWithFields recibe demasiados parámetros primitivos relacionados.
//   La regla indica que deben encapsularse en un objeto.
// - Regla 10: Comentarios redundantes que repiten lo que ya dice el código.
//   Se eliminaron o se dejaron documentados como violación.
// - Regla 19: Acoplamiento temporal (init() debe llamarse antes de usar el repositorio).
//   El diseño debería encapsular este orden obligatorio para evitar mal uso.

package com.jcaa.usersmanagement.infrastructure.adapter.persistence.repository;

import com.jcaa.usersmanagement.application.port.out.DeleteUserPort;
import com.jcaa.usersmanagement.application.port.out.GetAllUsersPort;
import com.jcaa.usersmanagement.application.port.out.GetUserByEmailPort;
import com.jcaa.usersmanagement.application.port.out.GetUserByIdPort;
import com.jcaa.usersmanagement.application.port.out.SaveUserPort;
import com.jcaa.usersmanagement.application.port.out.UpdateUserPort;
import com.jcaa.usersmanagement.domain.exception.UserNotFoundException;
import com.jcaa.usersmanagement.domain.model.UserModel;
import com.jcaa.usersmanagement.domain.valueobject.UserEmail;
import com.jcaa.usersmanagement.domain.valueobject.UserId;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.dto.UserPersistenceDto;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.exception.PersistenceException;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.mapper.UserPersistenceMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Log
@RequiredArgsConstructor
public final class UserRepositoryMySQL
    implements SaveUserPort,
        UpdateUserPort,
        GetUserByIdPort,
        GetUserByEmailPort,
        GetAllUsersPort,
        DeleteUserPort {

  // CORREGIDO Regla 4: ahora UserPersistenceMapper es @UtilityClass,
  // por lo que no se instancia, se usa directamente.
  // private final UserPersistenceMapper persistenceMapper = new UserPersistenceMapper();

  private static final String SQL_INSERT =
      "INSERT INTO users "
      + "(id, name, email, password, role, status, created_at, updated_at) "
      + "VALUES (?, ?, ?, ?, ?, ?, NOW(), NOW())";

  private static final String SQL_UPDATE =
      "UPDATE users SET name = ?, email = ?, password = ?, role = ?, status = ?, updated_at = NOW() "
      + "WHERE id = ?";

  private static final String SQL_SELECT_BY_ID =
      "SELECT id, name, email, password, role, status, created_at, updated_at "
      + "FROM users "
      + "WHERE id = ? LIMIT 1";

  private static final String SQL_SELECT_BY_EMAIL =
      "SELECT id, name, email, password, role, status, created_at, updated_at "
      + "FROM users "
      + "WHERE email = ? LIMIT 1";

  private static final String SQL_SELECT_ALL =
      "SELECT id, name, email, password, role, status, created_at, updated_at "
      + "FROM users "
      + "ORDER BY name ASC";

  private static final String SQL_DELETE =
        "DELETE FROM users "
        + "WHERE id = ?";

  private final Connection connection;

  // VIOLACIÓN Regla 19: acoplamiento temporal.
  // init() debe llamarse antes de usar el repositorio, pero el diseño no lo hace evidente.
  private boolean initialized = false;

  public void init() {
    this.initialized = true;
  }

  @Override
  public UserModel save(final UserModel user) {
    final UserPersistenceDto dto = UserPersistenceMapper.fromModelToDto(user);
    executeSave(dto);
    return findByIdOrFail(user.getId());
  }

  // VIOLACIÓN Regla 5: demasiados parámetros primitivos relacionados.
  // Este método debería eliminarse o encapsular los datos en un objeto.
  public UserModel saveWithFields(
      final String id,
      final String name,
      final String email,
      final String password,
      final String role,
      final String status) {
    if (id == null || name == null || email == null || password == null || role == null || status == null) {
      throw new IllegalArgumentException("Todos los campos son obligatorios");
    }
    throw new UnsupportedOperationException("Usar save(UserModel) en su lugar.");
  }

  @Override
  public UserModel update(final UserModel user) {
    final UserPersistenceDto dto = UserPersistenceMapper.fromModelToDto(user);
    executeUpdate(dto);
    return findByIdOrFail(user.getId());
  }

  @Override
  public Optional<UserModel> getById(final UserId userId) {
    try (final PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID)) {
      statement.setString(1, userId.value());
      final ResultSet resultSet = statement.executeQuery();
      if (!resultSet.next()) {
        return Optional.empty();
      }
      return Optional.of(UserPersistenceMapper.fromResultSetToModel(resultSet));
    } catch (final SQLException exception) {
      throw PersistenceException.becauseFindByIdFailed(userId.value(), exception);
    }
  }

  @Override
  public Optional<UserModel> getByEmail(final UserEmail email) {
    try (final PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_EMAIL)) {
      statement.setString(1, email.value());
      final ResultSet resultSet = statement.executeQuery();
      if (!resultSet.next()) {
        return Optional.empty();
      }
      return Optional.of(UserPersistenceMapper.fromResultSetToModel(resultSet));
    } catch (final SQLException exception) {
      throw PersistenceException.becauseFindByEmailFailed(email.value(), exception);
    }
  }

  @Override
  public List<UserModel> getAll() {
    try (final PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL)) {
      final ResultSet resultSet = statement.executeQuery();
      return UserPersistenceMapper.fromResultSetToModelList(resultSet);
    } catch (final SQLException exception) {
      throw PersistenceException.becauseFindAllFailed(exception);
    }
  }

  @Override
  public void delete(final UserId userId) {
    try (final PreparedStatement statement = connection.prepareStatement(SQL_DELETE)) {
      statement.setString(1, userId.value());
      statement.executeUpdate();
    } catch (final SQLException exception) {
      throw PersistenceException.becauseDeleteFailed(userId.value(), exception);
    }
  }

  private void executeSave(final UserPersistenceDto dto) {
    try (final PreparedStatement statement = connection.prepareStatement(SQL_INSERT)) {
      statement.setString(1, dto.id());
      statement.setString(2, dto.name());
      statement.setString(3, dto.email());
      statement.setString(4, dto.password());
      statement.setString(5, dto.role());
      statement.setString(6, dto.status());
      statement.executeUpdate();
    } catch (final SQLException exception) {
      throw PersistenceException.becauseSaveFailed(dto.id(), exception);
    }
  }

  private void executeUpdate(final UserPersistenceDto dto) {
    try (final PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {
      statement.setString(1, dto.name());
      statement.setString(2, dto.email());
      statement.setString(3, dto.password());
      statement.setString(4, dto.role());
      statement.setString(5, dto.status());
      statement.setString(6, dto.id());
      statement.executeUpdate();
    } catch (final SQLException exception) {
      throw PersistenceException.becauseUpdateFailed(dto.id(), exception);
    }
  }

  private UserModel findByIdOrFail(final UserId userId) {
    return getById(userId)
        .orElseThrow(() -> UserNotFoundException.becauseIdWasNotFound(userId.value()));
  }
}
