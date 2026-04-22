package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.GetAllUsersUseCase;
import com.jcaa.usersmanagement.application.port.out.GetAllUsersPort;
import com.jcaa.usersmanagement.domain.model.UserModel;
import lombok.RequiredArgsConstructor;

import java.util.List;

// Guía Hexagonal - Regla 5 / Clean Code - Regla 21 (no retornar códigos de error):
// El método retornaba null cuando la lista estaba vacía, usando null como señal
// especial de "no hay usuarios". Esto es ambiguo: el llamador no puede distinguir
// entre un error y un resultado vacío válido. La guía indica que los métodos nunca
// deben retornar null; se debe retornar una colección vacía en su lugar.
@RequiredArgsConstructor
public final class GetAllUsersService implements GetAllUsersUseCase {

  private final GetAllUsersPort getAllUsersPort;

  @Override
  public List<UserModel> execute() {
    return getAllUsersPort.getAll();
  }
}