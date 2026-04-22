// VIOLACIONES DETECTADAS:
// - Regla 4: Se usaba `== null` en lugar de `Objects.requireNonNull()` o `Objects.isNull()`.
//   Para objetos siempre debe usarse la utilidad de `Objects`, nunca operadores == o !=.
// - Regla 4: Se usaban nombres abreviados ("props", "val") en lugar de nombres descriptivos.
//   Los nombres deben ser claros y sin abreviaturas.

package com.jcaa.usersmanagement.infrastructure.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public final class AppProperties {

  private static final String PROPERTIES_FILE = "application.properties";

  private final Properties properties;

  public AppProperties() {
    this(AppProperties.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE));
  }

  // Package-private — test entry point
  AppProperties(final InputStream stream) {
    this.properties = doLoad(stream);
  }

  private static Properties doLoad(final InputStream stream) {
    // CORREGIDO Regla 4: se usa Objects.requireNonNull en lugar de == null.
    Objects.requireNonNull(stream, "File not found in classpath: " + PROPERTIES_FILE);

    // CORREGIDO Regla 4: nombre descriptivo "properties" en lugar de "props".
    final Properties properties = new Properties();
    try (stream) {
      properties.load(stream);
    } catch (final IOException exception) {
      throw ConfigurationException.becauseLoadFailed(exception);
    }
    return properties;
  }

  public String get(final String key) {
    // CORREGIDO Regla 4: nombre descriptivo "value" en lugar de "val".
    final String value = properties.getProperty(key);

    // CORREGIDO Regla 4: se usa Objects.requireNonNull en lugar de == null.
    return Objects.requireNonNull(value, 
        "Property not found in " + PROPERTIES_FILE + ": " + key);
  }

  public int getInt(final String key) {
    return Integer.parseInt(get(key));
  }
}
