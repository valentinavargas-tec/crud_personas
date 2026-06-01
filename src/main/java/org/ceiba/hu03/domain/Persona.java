package org.ceiba.hu03.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ceiba.hu03.constants.AppConstants;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "personas")
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = AppConstants.NOMBRE_OBLIGATORIO)
    private String nombre;

    @NotBlank(message = AppConstants.APELLIDO_OBLIGATORIO)
    private String apellido;

    @NotBlank(message = AppConstants.EMAIL_OBLIGATORIO)
    @Email(message = AppConstants.EMAIL_INVALIDO)
    private String email;

    @Past(message = AppConstants.FECHA_INVALIDA)
    private LocalDate fechaNacimiento;

}
