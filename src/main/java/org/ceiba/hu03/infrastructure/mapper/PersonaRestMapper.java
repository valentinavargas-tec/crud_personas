package org.ceiba.hu03.infrastructure.mapper;

import org.ceiba.hu03.domain.model.Persona;
import org.ceiba.hu03.domain.model.PaginatedResult;
import org.ceiba.hu03.infrastructure.controller.dto.PersonaDTO;
import org.ceiba.hu03.infrastructure.controller.dto.PaginatedResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PersonaRestMapper {

    public Persona toDomain(PersonaDTO dto) {
        if (dto == null) {
            return null;
        }
        return Persona.builder()
                .cedula(dto.getCedula())
                .nombre(dto.getNombre() != null ? dto.getNombre().trim() : null)
                .apellido(dto.getApellido() != null ? dto.getApellido().trim() : null)
                .email(dto.getEmail() != null ? dto.getEmail().trim() : null)
                .fechaNacimiento(dto.getFechaNacimiento())
                .build();
    }

    public PersonaDTO toDTO(Persona domain) {
        if (domain == null) {
            return null;
        }
        return PersonaDTO.builder()
                .cedula(domain.getCedula())
                .nombre(domain.getNombre() != null ? domain.getNombre().trim() : null)
                .apellido(domain.getApellido() != null ? domain.getApellido().trim() : null)
                .email(domain.getEmail() != null ? domain.getEmail().trim() : null)
                .fechaNacimiento(domain.getFechaNacimiento())
                .build();
    }

    public PaginatedResponseDTO<PersonaDTO> toPaginatedResponse(PaginatedResult<Persona> result) {
        if (result == null) {
            return null;
        }
        List<PersonaDTO> content = result.getContent() != null ? result.getContent().stream()
                .map(this::toDTO)
                .toList() : List.of();

        return PaginatedResponseDTO.<PersonaDTO>builder()
                .content(content)
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .currentPage(result.getCurrentPage())
                .size(result.getSize())
                .build();
    }
}
