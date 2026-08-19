package ceiba.com.co.paciente.comando;

import java.time.LocalDate;

public record ComandoActualizarPaciente(
        String nombre,
        String apellido,
        LocalDate fechaNacimiento,
        String telefono,
        String correoElectronico,
        String eps,
        String genero
) {
}
