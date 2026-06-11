package org.ceiba.hu03.domain.port.in;

import org.ceiba.hu03.domain.model.Persona;

public interface BuscarPersonaUseCase {
    Persona buscarPorCedula(Long cedula);
}
