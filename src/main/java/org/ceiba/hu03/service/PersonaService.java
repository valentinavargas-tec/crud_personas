package org.ceiba.hu03.service;

import org.ceiba.hu03.constants.AppConstants;
import org.ceiba.hu03.domain.Persona;
import org.ceiba.hu03.exception.EmailAlreadyExistsException;
import org.ceiba.hu03.exception.PersonaNotFoundException;
import org.ceiba.hu03.repository.PersonaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonaService {
    private final PersonaRepository personaRepository;

    public PersonaService(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    public Persona guardar (Persona persona){
        validarEmailUnico(persona.getEmail());
             return personaRepository.save(persona);
    }

    public List<Persona> listar(){
        return this.personaRepository.findAllByOrderByApellidoAsc();
    }

    public Persona buscarPorId(Long id) {
        return personaRepository.findById(id)
                .orElseThrow(() ->
                        new PersonaNotFoundException(AppConstants.PERSONA_NO_ENCONTRADA)
                );

    }


    public Persona actualizarPersona(Long id, Persona personaActualizada) {

        Persona personaExistente = buscarPorId(id);

        if (!personaExistente.getEmail().equals(personaActualizada.getEmail())) {
            validarEmailUnico(personaActualizada.getEmail());
        }

        personaExistente.setNombre(personaActualizada.getNombre());
        personaExistente.setApellido(personaActualizada.getApellido());
        personaExistente.setEmail(personaActualizada.getEmail());
        personaExistente.setFechaNacimiento(personaActualizada.getFechaNacimiento());

        return personaRepository.save(personaExistente);
    }

    public Persona eliminarPersona(Long id) {

        Persona persona = buscarPorId(id);

        personaRepository.delete(persona);
        return persona;
    }

    private void validarEmailUnico(String email) {
        if (personaRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(AppConstants.CORREO_EN_USO);
        }
    }
}
