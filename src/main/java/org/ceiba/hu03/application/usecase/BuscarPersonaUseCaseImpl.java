package org.ceiba.hu03.application.usecase;

import org.ceiba.hu03.domain.constant.AppConstants;
import org.ceiba.hu03.domain.model.Persona;
import org.ceiba.hu03.domain.port.in.BuscarPersonaUseCase;
import org.ceiba.hu03.domain.port.out.PersonaRepositoryPort;
import org.ceiba.hu03.domain.exception.PersonaNotFoundException;

public class BuscarPersonaUseCaseImpl implements BuscarPersonaUseCase {

    private final PersonaRepositoryPort personaRepositoryPort;

    public BuscarPersonaUseCaseImpl(PersonaRepositoryPort personaRepositoryPort) {
        this.personaRepositoryPort = personaRepositoryPort;
    }

    @Override
    public Persona buscarPorCedula(Long cedula) {
        return personaRepositoryPort.findById(cedula)
                .orElseThrow(() -> new PersonaNotFoundException(AppConstants.PERSONA_NO_ENCONTRADA));
    }
}
