package ceiba.com.co.cita.consulta;

import java.time.LocalDateTime;

public record DtoCita(
        Long id,
        String pacienteDocumento,
        String doctorDocumento,
        LocalDateTime fechaHora,
        String tipoCita,
        String estado,
        String motivo
) {
}
