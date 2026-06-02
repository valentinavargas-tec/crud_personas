package org.ceiba.hu03.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO para transportar datos de Persona sin exponer la entidad @Entity.
 * Desacopla la API del modelo de persistencia.
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonaDTO {

    private Long cedula;
    private String nombre;
    private String apellido;
    private String email;
    private LocalDate fechaNacimiento;
}
