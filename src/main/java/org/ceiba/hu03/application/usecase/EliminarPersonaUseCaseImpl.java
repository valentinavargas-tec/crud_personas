package org.ceiba.hu03.application.usecase;

import org.ceiba.hu03.domain.constant.AppConstants;
import org.ceiba.hu03.domain.model.Persona;
import org.ceiba.hu03.domain.port.in.EliminarPersonaUseCase;
import org.ceiba.hu03.domain.port.out.PersonaRepositoryPort;
import org.ceiba.hu03.domain.exception.PersonaNotFoundException;

public class EliminarPersonaUseCaseImpl implements EliminarPersonaUseCase {

    private final PersonaRepositoryPort personaRepositoryPort;

    public EliminarPersonaUseCaseImpl(PersonaRepositoryPort personaRepositoryPort) {
        this.personaRepositoryPort = personaRepositoryPort;
    }

    @Override
    public Persona eliminar(Long cedula) {
        Persona persona = personaRepositoryPort.findById(cedula)
                .orElseThrow(() -> new PersonaNotFoundException(AppConstants.PERSONA_NO_ENCONTRADA));
        personaRepositoryPort.delete(persona);
        return persona;
    }
}
