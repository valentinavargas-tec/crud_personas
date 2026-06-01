package org.ceiba.hu03.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.ceiba.hu03.domain.Persona;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonaResponse {


    private String mensaje;
    private String advertencia;
    private Persona persona;

}
