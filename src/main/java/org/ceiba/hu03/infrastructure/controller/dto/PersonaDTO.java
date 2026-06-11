package org.ceiba.hu03.infrastructure.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.ceiba.hu03.domain.constant.AppConstants;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonaDTO {

    @NotNull(message = AppConstants.CEDULA_OBLIGATORIA)
    @Positive(message = AppConstants.CEDULA_INVALIDA)
    private Long cedula;

    @NotBlank(message = AppConstants.NOMBRE_OBLIGATORIO)
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ' ]+$", message = AppConstants.NOMBRE_INVALIDO)
    private String nombre;

    @NotBlank(message = AppConstants.APELLIDO_OBLIGATORIO)
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ' ]+$", message = AppConstants.APELLIDO_INVALIDO)
    private String apellido;

    @NotBlank(message = AppConstants.EMAIL_OBLIGATORIO)
    @Email(message = AppConstants.EMAIL_INVALIDO)
    private String email;

    @Past(message = AppConstants.FECHA_INVALIDA)
    private LocalDate fechaNacimiento;
}
