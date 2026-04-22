package com.jcaa.usersmanagement.application.service.dto.query;

import jakarta.validation.constraints.NotBlank;

// Clean Code - Regla 3 (Lombok y validaciones):
// 1. Se eliminó @Builder porque los records ya generan un constructor
//    canónico automáticamente. Usarlo es redundante e innecesario.
// 2. Se eliminó el atributo message= de @NotBlank. La guía indica no
//    personalizar mensajes en las constraints; se deben usar los mensajes
//    por defecto de Jakarta Validation para mantener consistencia.
public record GetUserByIdQuery(
    @NotBlank String id) {

}