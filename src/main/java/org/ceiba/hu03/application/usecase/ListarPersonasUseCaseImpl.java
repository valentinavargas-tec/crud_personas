package org.ceiba.hu03.application.usecase;

import org.ceiba.hu03.domain.model.Persona;
import org.ceiba.hu03.domain.port.in.ListarPersonasUseCase;
import org.ceiba.hu03.domain.port.out.PersonaRepositoryPort;

import java.util.List;

public class ListarPersonasUseCaseImpl implements ListarPersonasUseCase {

    private final PersonaRepositoryPort personaRepositoryPort;

    public ListarPersonasUseCaseImpl(PersonaRepositoryPort personaRepositoryPort) {
        this.personaRepositoryPort = personaRepositoryPort;
    }

    @Override
    public List<Persona> listar() {
        return personaRepositoryPort.findAllByOrderByApellidoAsc();
    }
}
