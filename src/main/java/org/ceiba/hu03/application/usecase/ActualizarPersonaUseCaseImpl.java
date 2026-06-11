package org.ceiba.hu03.application.usecase;

import org.ceiba.hu03.domain.constant.AppConstants;
import org.ceiba.hu03.domain.model.Persona;
import org.ceiba.hu03.domain.port.in.ActualizarPersonaUseCase;
import org.ceiba.hu03.domain.port.out.PersonaRepositoryPort;
import org.ceiba.hu03.domain.exception.PersonaNotFoundException;
import org.ceiba.hu03.domain.exception.CedulaInmutableException;
import org.ceiba.hu03.domain.exception.EmailAlreadyExistsException;

public class ActualizarPersonaUseCaseImpl implements ActualizarPersonaUseCase {

    private final PersonaRepositoryPort personaRepositoryPort;

    public ActualizarPersonaUseCaseImpl(PersonaRepositoryPort personaRepositoryPort) {
        this.personaRepositoryPort = personaRepositoryPort;
    }

    @Override
    public Persona actualizar(Long cedula, Persona personaActualizada) {
        Persona personaExistente = personaRepositoryPort.findById(cedula)
                .orElseThrow(() -> new PersonaNotFoundException(AppConstants.PERSONA_NO_ENCONTRADA));

        if (personaActualizada.getCedula() != null &&
                !personaActualizada.getCedula().equals(cedula)) {
            throw new CedulaInmutableException(AppConstants.CEDULA_INMUTABLE);
        }

        if (!personaExistente.getEmail().equals(personaActualizada.getEmail())) {
            validarEmailUnico(personaActualizada.getEmail());
        }

        Persona personaParaActualizar = Persona.builder()
                .cedula(cedula)
                .nombre(personaActualizada.getNombre())
                .apellido(personaActualizada.getApellido())
                .email(personaActualizada.getEmail())
                .fechaNacimiento(personaActualizada.getFechaNacimiento())
                .build();

        return personaRepositoryPort.save(personaParaActualizar);
    }

    private void validarEmailUnico(String email) {
        if (personaRepositoryPort.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(AppConstants.CORREO_EN_USO);
        }
    }
}
