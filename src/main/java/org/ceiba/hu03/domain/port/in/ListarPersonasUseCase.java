package org.ceiba.hu03.domain.port.in;

import java.util.List;

import org.ceiba.hu03.domain.model.Persona;

public interface ListarPersonasUseCase {
    List<Persona> listar();
}
