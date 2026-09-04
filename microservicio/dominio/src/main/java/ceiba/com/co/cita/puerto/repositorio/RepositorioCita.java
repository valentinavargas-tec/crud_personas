package ceiba.com.co.cita.puerto.repositorio;

import ceiba.com.co.cita.modelo.entidad.Cita;

import java.time.LocalDateTime;
import java.util.Optional;

public interface RepositorioCita {

    Long guardar(Cita cita);

    Optional<Cita> obtenerPorId(Long id);

    void actualizar(Cita cita);

    boolean existeCitaEnHorarioDoctor(String doctorDocumento, LocalDateTime fechaHora);

    boolean existeCitaEnHorarioPaciente(String pacienteDocumento, LocalDateTime fechaHora);

    boolean existeCitaEnHorarioDoctorExcluyendoCita(Long idCita, String doctorDocumento, LocalDateTime fechaHora);

    boolean existeCitaEnHorarioPacienteExcluyendoCita(Long idCita, String pacienteDocumento, LocalDateTime fechaHora);
}
