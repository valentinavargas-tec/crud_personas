package ceiba.com.co.comando;

import java.time.LocalDate;

public record ComandoActualizarPersona(
        String nombre,
        String apellido,
        String email,
        LocalDate fechaNacimiento) {
}
