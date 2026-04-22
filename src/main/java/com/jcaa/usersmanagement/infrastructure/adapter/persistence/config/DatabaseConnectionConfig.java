// VIOLACIONES DETECTADAS:
// - Regla 4: Clase con solo métodos estáticos que NO estaba anotada con @UtilityClass.
//   Se corrigió agregando la anotación @UtilityClass para cumplir con Clean Code.

package com.jcaa.usersmanagement.infrastructure.adapter.persistence.config;

import com.jcaa.usersmanagement.infrastructure.adapter.persistence.exception.PersistenceException;
import lombok.experimental.UtilityClass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@UtilityClass
public class DatabaseConnectionConfig {

  public static Connection getConnection(
      final String host,
      final int port,
      final String name,
      final String user,
      final String password) {
    try {
      String url = String.format("jdbc:mysql://%s:%d/%s", host, port, name);
      return DriverManager.getConnection(url, user, password);
    } catch (final SQLException exception) {
      throw PersistenceException.becauseDatabaseConnectionFailed(exception);
    }
  }
  
}
