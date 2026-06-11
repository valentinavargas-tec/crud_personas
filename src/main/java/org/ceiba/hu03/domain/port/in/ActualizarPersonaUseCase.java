package org.ceiba.hu03.domain.port.in;

import org.ceiba.hu03.domain.model.Persona;

public interface ActualizarPersonaUseCase {
    Persona actualizar(Long cedula, Persona personaActualizada);
}
