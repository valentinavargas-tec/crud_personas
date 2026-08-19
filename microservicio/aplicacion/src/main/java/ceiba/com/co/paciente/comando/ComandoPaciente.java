package ceiba.com.co.paciente.comando;

import java.time.LocalDate;

public record ComandoPaciente(
        Long numeroDocumento,
        String tipoDocumento,
        String nombre,
        String apellido,
        LocalDate fechaNacimiento,
        String telefono,
        String correoElectronico,
        String eps,
        String genero
) {
}
