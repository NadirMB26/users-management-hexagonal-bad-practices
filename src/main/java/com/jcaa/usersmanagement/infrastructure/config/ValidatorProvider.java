// VIOLACIÓN Regla 4: clase con solo métodos estáticos que NO estaba anotada con @UtilityClass.
// Se corrigió agregando la anotación @UtilityClass para cumplir con Clean Code.

package com.jcaa.usersmanagement.infrastructure.config;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.experimental.UtilityClass;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

@UtilityClass
public class ValidatorProvider {

  public static Validator buildValidator() {
    try (ValidatorFactory factory =
             Validation.byDefaultProvider()
                 .configure()
                 .messageInterpolator(new ParameterMessageInterpolator())
                 .buildValidatorFactory()) {
      return factory.getValidator();
    }
  }
}
