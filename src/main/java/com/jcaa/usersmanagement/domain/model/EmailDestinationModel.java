package com.jcaa.usersmanagement.domain.model;

import lombok.Value;
import java.util.Objects;

// Clean Code aplicado:
// - Regla 15 (inmutabilidad): se mantiene @Value para campos finales y sin setters.
// - Regla 4 (uso correcto de null checks): se reemplazó == null por Objects.requireNonNull().
// - Regla 10 (evitar texto hardcodeado): los mensajes de error se movieron a constantes privadas.
// - Regla 12 (alta cohesión): la validación se encapsula en el propio modelo.

@Value
public class EmailDestinationModel {

  String destinationEmail;
  String destinationName;
  String subject;
  String body;

  public EmailDestinationModel(
      final String destinationEmail,
      final String destinationName,
      final String subject,
      final String body) {
    this.destinationEmail = validateNotBlank(destinationEmail, EMPTY_EMAIL_MESSAGE);
    this.destinationName  = validateNotBlank(destinationName, EMPTY_NAME_MESSAGE);
    this.subject          = validateNotBlank(subject, EMPTY_SUBJECT_MESSAGE);
    this.body             = validateNotBlank(body, EMPTY_BODY_MESSAGE);
  }

  // Constantes de mensajes de error
  private static final String EMPTY_EMAIL_MESSAGE   = "El email del destinatario es requerido.";
  private static final String EMPTY_NAME_MESSAGE    = "El nombre del destinatario es requerido.";
  private static final String EMPTY_SUBJECT_MESSAGE = "El asunto es requerido.";
  private static final String EMPTY_BODY_MESSAGE    = "El cuerpo del mensaje es requerido.";

  private static String validateNotBlank(final String value, final String errorMessage) {
    Objects.requireNonNull(value, errorMessage);
    if (value.trim().isEmpty()) {
      throw new IllegalArgumentException(errorMessage);
    }
    return value;
  }
}
