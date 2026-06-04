package org.ceiba.hu03.service;

import org.ceiba.hu03.constants.AppConstants;
import org.ceiba.hu03.domain.Persona;
import org.ceiba.hu03.dto.PersonaDTO;
import org.ceiba.hu03.dto.PersonaResponse;
import org.ceiba.hu03.exception.CedulaInmutableException;
import org.ceiba.hu03.exception.EmailAlreadyExistsException;
import org.ceiba.hu03.exception.PersonaAlreadyExistsException;
import org.ceiba.hu03.exception.PersonaNotFoundException;
import org.ceiba.hu03.mapper.PersonaMapper;
import org.ceiba.hu03.repository.PersonaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonaServiceImpl implements PersonaService {
    private final PersonaRepository personaRepository;

    public PersonaServiceImpl(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    @Override
    public PersonaResponse crear(Persona persona) {
        validarCedulaUnica(persona.getCedula());
        validarEmailUnico(persona.getEmail());
        Persona personaCreada = personaRepository.save(persona);
        return new PersonaResponse(AppConstants.PERSONA_CREADA, PersonaMapper.toDTO(personaCreada));
    }

    @Override
    public List<PersonaDTO> listar() {
        return personaRepository.findAllByOrderByApellidoAsc()
                .stream()
                .map(PersonaMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Persona buscarPorCedula(Long cedula) {
        return personaRepository.findById(cedula)
                .orElseThrow(() -> new PersonaNotFoundException(AppConstants.PERSONA_NO_ENCONTRADA));
    }

    @Override
    public PersonaDTO buscarPorCedulaDTO(Long cedula) {
        return PersonaMapper.toDTO(buscarPorCedula(cedula));
    }

    @Override
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
        return new PersonaResponse(AppConstants.PERSONA_ACTUALIZADA, PersonaMapper.toDTO(personaActualizadaGuardada));
    }

    @Override
    public PersonaResponse eliminar(Long cedula) {
        Persona persona = buscarPorCedula(cedula);
        personaRepository.delete(persona);
        return new PersonaResponse(AppConstants.PERSONA_ELIMINADA, PersonaMapper.toDTO(persona));
    }

    private void validarCedulaUnica(Long cedula) {
        if (personaRepository.existsById(cedula)) {
            throw new PersonaAlreadyExistsException(
                    AppConstants.CEDULA_YA_REGISTRADA);
        }
    }

    private void validarEmailUnico(String email) {
        if (personaRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(AppConstants.CORREO_EN_USO);
        }
    }
}
