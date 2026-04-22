package com.jcaa.usersmanagement.domain.exception;

// Clean Code - Regla 9 (constructores controlados):
//   Se eliminaron los constructores públicos. Ahora la excepción solo se instancia
//   mediante factory methods con constructores privados, controlando cómo se crea.
//
// Clean Code - Regla 10 (evitar texto hardcodeado):
//   Los mensajes de error se movieron a constantes privadas con nombre descriptivo.
//   Esto evita duplicación y facilita mantenimiento.

public final class EmailSenderException extends DomainException {

  // Constantes de mensajes de error
  private static final String SMTP_FAILED_MESSAGE =
      "No se pudo enviar el correo a '%s'. Error SMTP: %s";
  private static final String SEND_FAILED_MESSAGE =
      "La notificación por correo no pudo ser enviada.";

  // Constructores privados para forzar uso de factory methods
  private EmailSenderException(final String message) {
    super(message);
  }

  private EmailSenderException(final String message, final Throwable cause) {
    super(message, cause);
  }

  // Factory methods expresivos
  public static EmailSenderException becauseSmtpFailed(
      final String destinationEmail, final String smtpError) {
    return new EmailSenderException(
        String.format(SMTP_FAILED_MESSAGE, destinationEmail, smtpError));
  }

  public static EmailSenderException becauseSendFailed(final Throwable cause) {
    return new EmailSenderException(SEND_FAILED_MESSAGE, cause);
  }
}
