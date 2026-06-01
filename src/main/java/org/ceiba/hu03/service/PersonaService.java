package org.ceiba.hu03.service;

import org.ceiba.hu03.constants.AppConstants;
import org.ceiba.hu03.domain.Persona;
import org.ceiba.hu03.dto.PersonaDTO;
import org.ceiba.hu03.dto.PersonaResponse;
import org.ceiba.hu03.exception.CedulaInmutableException;
import org.ceiba.hu03.exception.EmailAlreadyExistsException;
import org.ceiba.hu03.exception.PersonaAlreadyExistsException;
import org.ceiba.hu03.exception.PersonaNotFoundException;
import org.ceiba.hu03.repository.PersonaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonaService {
    private final PersonaRepository personaRepository;

    public PersonaService(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    public PersonaResponse crear(Persona persona) {
        validarCedulaUnica(persona.getCedula());
        validarEmailUnico(persona.getEmail());
        Persona personaCreada = personaRepository.save(persona);
        return new PersonaResponse(AppConstants.PERSONA_CREADA, mapToDTO(personaCreada));
    }

    public List<PersonaDTO> listar() {
        return personaRepository.findAllByOrderByApellidoAsc()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Persona buscarPorCedula(Long cedula) {
        return personaRepository.findById(cedula)
                .orElseThrow(() ->
                        new PersonaNotFoundException(AppConstants.PERSONA_NO_ENCONTRADA)
                );
    }

    public PersonaDTO buscarPorCedulaDTO(Long cedula) {
        return mapToDTO(buscarPorCedula(cedula));
    }

    public PersonaResponse actualizar(Long cedula, Persona personaActualizada) {
        Persona personaExistente = buscarPorCedula(cedula);

        if (personaActualizada.getCedula() != null &&
                !personaActualizada.getCedula().equals(cedula)) {
            throw new CedulaInmutableException(AppConstants.CEDULA_INMUTABLE);
        }

        if (!personaExistente.getEmail().equals(personaActualizada.getEmail())) {
            validarEmailUnico(personaActualizada.getEmail());
        }

        personaExistente.setNombre(personaActualizada.getNombre());
        personaExistente.setApellido(personaActualizada.getApellido());
        personaExistente.setEmail(personaActualizada.getEmail());
        personaExistente.setFechaNacimiento(personaActualizada.getFechaNacimiento());

        Persona personaActualizadaGuardada = personaRepository.save(personaExistente);
        return new PersonaResponse(AppConstants.PERSONA_ACTUALIZADA, mapToDTO(personaActualizadaGuardada));
    }

    public PersonaResponse eliminar(Long cedula) {
        Persona persona = buscarPorCedula(cedula);
        personaRepository.delete(persona);
        return new PersonaResponse(AppConstants.PERSONA_ELIMINADA, mapToDTO(persona));
    }

    private PersonaDTO mapToDTO(Persona persona) {
        return PersonaDTO.builder()
                .cedula(persona.getCedula())
                .nombre(persona.getNombre())
                .apellido(persona.getApellido())
                .email(persona.getEmail())
                .fechaNacimiento(persona.getFechaNacimiento())
                .build();
    }

    private void validarCedulaUnica(Long cedula) {
        if (personaRepository.existsById(cedula)) {
            throw new PersonaAlreadyExistsException(
                    AppConstants.CEDULA_YA_REGISTRADA
            );
        }
    }

    private void validarEmailUnico(String email) {
        if (personaRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(AppConstants.CORREO_EN_USO);
        }
    }
}
