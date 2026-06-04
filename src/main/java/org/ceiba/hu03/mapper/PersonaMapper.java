package org.ceiba.hu03.mapper;

import org.ceiba.hu03.domain.Persona;
import org.ceiba.hu03.dto.PersonaDTO;

public class PersonaMapper {

    public static PersonaDTO toDTO(Persona persona) {
        if (persona == null) {
            return null;
        }
        return PersonaDTO.builder()
                .cedula(persona.getCedula())
                .nombre(persona.getNombre())
                .apellido(persona.getApellido())
                .email(persona.getEmail())
                .fechaNacimiento(persona.getFechaNacimiento())
                .build();
    }

    public static Persona toEntity(PersonaDTO dto) {
        if (dto == null) {
            return null;
        }
        return Persona.builder()
                .cedula(dto.getCedula())
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .email(dto.getEmail())
                .fechaNacimiento(dto.getFechaNacimiento())
                .build();
    }
}
