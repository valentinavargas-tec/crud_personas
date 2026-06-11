package org.ceiba.hu03.domain.port.in;

import org.ceiba.hu03.domain.model.PaginatedResult;
import org.ceiba.hu03.domain.model.Persona;

public interface BuscarPersonaAvanzadaUseCase {
    PaginatedResult<Persona> buscar(
            String nombre,
            String apellido,
            Integer edadMinima,
            Integer edadMaxima,
            int page,
            int size,
            String sort
    );
}
