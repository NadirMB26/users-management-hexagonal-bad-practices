package com.jcaa.usersmanagement.application.service.dto.command;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Clean Code - Regla 3 (Lombok y validaciones):
// Se eliminó @Builder de Lombok porque los records ya generan un constructor
// canónico automáticamente. Usar @Builder sobre un record es redundante,
// introduce una dependencia innecesaria y genera confusión sobre cómo
// instanciar el objeto. La solución es usar el constructor canónico del record
// directamente.
public record CreateUserCommand(
    @NotBlank(message = "id must not be blank") String id,
    @NotBlank(message = "name must not be blank")
        @Size(min = 3, message = "name must have at least 3 characters")
        String name,
    @NotBlank(message = "email must not be blank")
        @Email(message = "email must be a valid email address")
        String email,
    @NotBlank(message = "password must not be blank")
        @Size(min = 8, message = "password must have at least 8 characters")
        String password,
    @NotBlank(message = "role must not be blank") String role) {
}