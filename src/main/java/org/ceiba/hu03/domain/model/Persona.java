package org.ceiba.hu03.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Persona {

    private final Long cedula;
    private final String nombre;
    private final String apellido;
    private final String email;
    private final LocalDate fechaNacimiento;

}
