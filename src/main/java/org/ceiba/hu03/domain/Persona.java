package org.ceiba.hu03.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "personas")
public class Persona {

    @Id
    private Long cedula;

    private String nombre;

    private String apellido;

    private String email;

    private LocalDate fechaNacimiento;

}
