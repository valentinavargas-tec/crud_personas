package ceiba.com.co.paciente.modelo.dto;

import ceiba.com.co.paciente.modelo.entidad.Genero;

import java.time.LocalDate;

public record DatosActualizarPaciente(
        String nombre,
        String apellido,
        LocalDate fechaNacimiento,
        String telefono,
        String correoElectronico,
        String eps,
        Genero genero
) {
}
