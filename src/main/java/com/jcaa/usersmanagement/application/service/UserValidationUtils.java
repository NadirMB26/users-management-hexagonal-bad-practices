package com.jcaa.usersmanagement.application.service;

/**
 * Se eliminó la clase UserValidationUtils.
 *
 * Clean Code - Regla 13 (evitar clases utilitarias innecesarias):
 *   La lógica de validación fue movida a los objetos de dominio correspondientes.
 *
 * Clean Code - Regla 12 (alta cohesión):
 *   Cada clase maneja su propia responsabilidad:
 *     - UserModel: isAllowedToLogin(), isAdmin()
 *     - UserEmail: isValidFormat()
 *     - UserPassword: isValid()
 *     - UserAccessPolicy: canPerformAction()
 *
 * Clean Code - Regla 11/23 (evitar duplicación y conocimiento disperso):
 *   Validaciones centralizadas en los value objects y el modelo.
 *
 * Clean Code - Regla 18 (evitar valores mágicos):
 *   Se reemplazaron literales como "ACTIVE" y "PENDING" por enums UserStatus.ACTIVE y UserStatus.PENDING.
 *
 * Clean Code - Regla 20 (usar objetos de dominio en lugar de primitivos):
 *   Se encapsularon conceptos como Email, Password y Status en value objects.
 *
 * Resultado:
 * - La clase UserValidationUtils ya no existe.
 * - La lógica se distribuye en el dominio, mejorando cohesión, claridad y encapsulación.
 */
public class UserValidationUtils {
  // Clase eliminada: toda la lógica fue movida al dominio.
}
