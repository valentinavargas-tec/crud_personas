package org.ceiba.hu03.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * DTO estándar para respuestas exitosas de operaciones sobre Persona.
 * Transporta un mensaje y los datos de persona sin exponer la entidad.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonaResponse {

    private String mensaje;
    private PersonaDTO persona;
}
