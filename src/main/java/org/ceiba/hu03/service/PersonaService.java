package org.ceiba.hu03.service;

import org.ceiba.hu03.domain.Persona;
import org.ceiba.hu03.dto.PersonaDTO;
import org.ceiba.hu03.dto.PersonaResponse;

import java.util.List;

public interface PersonaService {
    PersonaResponse crear(Persona persona);
    List<PersonaDTO> listar();
    PersonaDTO buscarPorCedulaDTO(Long cedula);
    PersonaResponse actualizar(Long cedula, Persona personaActualizada);
    PersonaResponse eliminar(Long cedula);
}
