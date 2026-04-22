package com.jcaa.usersmanagement.domain.model;

import com.jcaa.usersmanagement.domain.enums.UserRole;
import com.jcaa.usersmanagement.domain.enums.UserStatus;
import com.jcaa.usersmanagement.domain.valueobject.UserEmail;
import com.jcaa.usersmanagement.domain.valueobject.UserId;
import com.jcaa.usersmanagement.domain.valueobject.UserName;
import com.jcaa.usersmanagement.domain.valueobject.UserPassword;
import lombok.Value;

// Guía Hexagonal - Regla 9 (dependencias hacia el centro):
// Se eliminó la importación de UserEntity (infraestructura) en el dominio.
// El dominio no debe conocer cómo se persisten sus datos.
// La conversión a entidad debe hacerse en un mapper de la capa adapter.
//
// Clean Code - Regla 15 (inmutabilidad):
// Se reemplazó @Data + @AllArgsConstructor por @Value.
// Con @Value todos los campos son final e inmutables, sin setters públicos.
// Esto protege las invariantes del dominio y evita modificaciones externas.
//
// Clean Code - Regla 14 (Ley de Deméter):
// Se encapsuló la verificación de contraseña en passwordMatches(),
// evitando navegar a internals (user.getPassword().verifyPlain()).
//
// Clean Code - Regla 12 (alta cohesión):
// Se agregó isAllowedToLogin() y isAdmin() para centralizar la lógica de estados válidos
// y roles dentro del modelo de dominio, en lugar de dispersarla en la capa de aplicación.
//
// Clean Code - Regla 17 (condiciones limpias):
// La condición redundante sobre múltiples estados se reemplazó por métodos
// expresivos isAllowedToLogin() e isAdmin().

@Value
public class UserModel {

  UserId id;
  UserName name;
  UserEmail email;
  UserPassword password;
  UserRole role;
  UserStatus status;

  public static UserModel create(
      final UserId id,
      final UserName name,
      final UserEmail email,
      final UserPassword password,
      final UserRole role) {
    return new UserModel(id, name, email, password, role, UserStatus.PENDING);
  }

  public UserModel activate() {
    return new UserModel(id, name, email, password, role, UserStatus.ACTIVE);
  }

  public UserModel deactivate() {
    return new UserModel(id, name, email, password, role, UserStatus.INACTIVE);
  }

  // Encapsula la verificación de contraseña
  public boolean passwordMatches(final String plainPassword) {
    return this.password.verifyPlain(plainPassword);
  }

  // Encapsula la lógica de estados válidos para login
  public boolean isAllowedToLogin() {
    return this.status == UserStatus.ACTIVE;
  }

  // Encapsula la lógica de rol administrador
  public boolean isAdmin() {
    return this.role == UserRole.ADMIN;
  }
}
