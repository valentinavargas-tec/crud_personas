package org.ceiba.hu03.infrastructure.persistence;

import org.ceiba.hu03.domain.model.Persona;
import org.springframework.stereotype.Component;

@Component
public class PersonaPersistenceMapper {

    public PersonaEntity toEntity(Persona domain) {
        if (domain == null) {
            return null;
        }
        return PersonaEntity.builder()
                .cedula(domain.getCedula())
                .nombre(domain.getNombre())
                .apellido(domain.getApellido())
                .email(domain.getEmail())
                .fechaNacimiento(domain.getFechaNacimiento())
                .build();
    }

    public Persona toDomain(PersonaEntity entity) {
        if (entity == null) {
            return null;
        }
        return Persona.builder()
                .cedula(entity.getCedula())
                .nombre(entity.getNombre())
                .apellido(entity.getApellido())
                .email(entity.getEmail())
                .fechaNacimiento(entity.getFechaNacimiento())
                .build();
    }
}
