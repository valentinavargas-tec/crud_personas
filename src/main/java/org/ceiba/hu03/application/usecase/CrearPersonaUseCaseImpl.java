package org.ceiba.hu03.application.usecase;

import org.ceiba.hu03.domain.constant.AppConstants;
import org.ceiba.hu03.domain.model.Persona;
import org.ceiba.hu03.domain.port.in.CrearPersonaUseCase;
import org.ceiba.hu03.domain.port.out.PersonaRepositoryPort;
import org.ceiba.hu03.domain.exception.PersonaAlreadyExistsException;
import org.ceiba.hu03.domain.exception.EmailAlreadyExistsException;

public class CrearPersonaUseCaseImpl implements CrearPersonaUseCase {

    private final PersonaRepositoryPort personaRepositoryPort;

    public CrearPersonaUseCaseImpl(PersonaRepositoryPort personaRepositoryPort) {
        this.personaRepositoryPort = personaRepositoryPort;
    }

    @Override
    public Persona crear(Persona persona) {
        validarCedulaUnica(persona.getCedula());
        validarEmailUnico(persona.getEmail());
        return personaRepositoryPort.save(persona);
    }

    private void validarCedulaUnica(Long cedula) {
        if (personaRepositoryPort.existsById(cedula)) {
            throw new PersonaAlreadyExistsException(AppConstants.CEDULA_YA_REGISTRADA);
        }
    }

    private void validarEmailUnico(String email) {
        if (personaRepositoryPort.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(AppConstants.CORREO_EN_USO);
        }
    }
}
