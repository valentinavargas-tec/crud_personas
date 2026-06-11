package org.ceiba.hu03.domain.port.in;

import org.ceiba.hu03.domain.model.Persona;

public interface EliminarPersonaUseCase {
    Persona eliminar(Long cedula);
}
