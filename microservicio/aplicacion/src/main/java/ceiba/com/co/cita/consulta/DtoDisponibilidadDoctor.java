package ceiba.com.co.cita.consulta;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record DtoDisponibilidadDoctor(
        String doctorDocumento,
        LocalDate fecha,
        List<LocalTime> franjasLibres
) {}
