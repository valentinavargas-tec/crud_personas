package org.ceiba.hu03.infrastructure.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "personas")
public class PersonaEntity {

    @Id
    private Long cedula;

    private String nombre;

    private String apellido;

    private String email;

    private LocalDate fechaNacimiento;
}
