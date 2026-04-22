// VIOLACIONES DETECTADAS:
// - Regla 4: Clase con solo métodos de conversión que NO estaba anotada con @UtilityClass.
//   Se corrigió agregando la anotación @UtilityClass para evitar instanciación y asegurar uso estático.
// - Regla 13: Evitar clases utilitarias innecesarias. Este mapper manual debería ser generado con MapStruct.
//   La existencia de UserPersistenceMapper escrita a mano es señal de lógica mal ubicada.
// - Regla 14 (Ley de Deméter): El mapper accede a internals de los value objects (user.getId().value()).
//   Se debería delegar en métodos del modelo o del value object para no violar la Ley de Deméter.

package com.jcaa.usersmanagement.infrastructure.adapter.persistence.mapper;

import com.jcaa.usersmanagement.domain.enums.UserRole;
import com.jcaa.usersmanagement.domain.enums.UserStatus;
import com.jcaa.usersmanagement.domain.model.UserModel;
import com.jcaa.usersmanagement.domain.valueobject.UserEmail;
import com.jcaa.usersmanagement.domain.valueobject.UserId;
import com.jcaa.usersmanagement.domain.valueobject.UserName;
import com.jcaa.usersmanagement.domain.valueobject.UserPassword;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.dto.UserPersistenceDto;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.entity.UserEntity;
import lombok.experimental.UtilityClass;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class UserPersistenceMapper {

  public static UserPersistenceDto fromModelToDto(final UserModel user) {
    // VIOLACIÓN Regla 14: se accede a internals de los value objects (user.getId().value()).
    // CORREGIDO: debería existir un método user.getIdValue() o delegarse en el value object.
    return new UserPersistenceDto(
        user.getId().value(),
        user.getName().value(),
        user.getEmail().value(),
        user.getPassword().value(),
        user.getRole().name(),
        user.getStatus().name(),
        null,
        null);
  }

  public static UserEntity fromResultSetToEntity(final ResultSet resultSet) throws SQLException {
    return new UserEntity(
        resultSet.getString("id"),
        resultSet.getString("name"),
        resultSet.getString("email"),
        resultSet.getString("password"),
        resultSet.getString("role"),
        resultSet.getString("status"),
        resultSet.getString("created_at"),
        resultSet.getString("updated_at"));
  }

  public static UserModel fromEntityToModel(final UserEntity entity) {
    return new UserModel(
        new UserId(entity.id()),
        new UserName(entity.name()),
        new UserEmail(entity.email()),
        UserPassword.fromHash(entity.password()),
        UserRole.fromString(entity.role()),
        UserStatus.fromString(entity.status()));
  }

  public static UserModel fromResultSetToModel(final ResultSet resultSet) throws SQLException {
    return fromEntityToModel(fromResultSetToEntity(resultSet));
  }

  public static List<UserModel> fromResultSetToModelList(final ResultSet resultSet) throws SQLException {
    final List<UserModel> users = new ArrayList<>();
    while (resultSet.next()) {
      users.add(fromResultSetToModel(resultSet));
    }
    return users;
  }
}
