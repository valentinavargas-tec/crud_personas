package ceiba.com.co.comando;

import java.time.LocalDate;

public record ComandoPersona(
        Long cedula,
        String nombre,
        String apellido,
        String email,
        LocalDate fechaNacimiento
) {
}
