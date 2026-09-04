package ceiba.com.co.cita.puerto.dao;

import ceiba.com.co.cita.consulta.DtoCita;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DaoCita {
    Optional<DtoCita> buscarPorId(Long idCita);
    List<DtoCita> buscarCitasPorPaciente(String pacienteDocumento);
    List<DtoCita> buscarCitasPorDoctor(String doctorDocumento);
    List<DtoCita> buscarCitasDoctorPorFecha(String doctorDocumento, LocalDate fecha);
}
