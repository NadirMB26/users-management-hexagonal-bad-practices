// VIOLACIONES DETECTADAS:
// - Regla 4: Clase con solo métodos estáticos que NO estaba anotada con @UtilityClass.
//   Sin @UtilityClass, Lombok no genera el constructor privado y la clase puede instanciarse.
//   Se corrigió agregando la anotación @UtilityClass para cumplir con Clean Code.

package com.jcaa.usersmanagement.infrastructure.adapter.persistence.config;

import com.jcaa.usersmanagement.infrastructure.adapter.persistence.exception.PersistenceException;
import lombok.experimental.UtilityClass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@UtilityClass
public class DatabaseConnectionConfig {

  public static Connection getConnection(final String url, final String user, final String password) {
    try {
      return DriverManager.getConnection(url, user, password);
    } catch (final SQLException exception) {
      // CORREGIDO Regla 4: ahora la clase está anotada con @UtilityClass,
      // lo que evita que pueda instanciarse y asegura que solo se use de forma estática.
      throw PersistenceException.becauseDatabaseConnectionFailed(exception);
    }
  }
}
