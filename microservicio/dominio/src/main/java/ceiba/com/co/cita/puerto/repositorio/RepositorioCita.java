package ceiba.com.co.cita.puerto.repositorio;

import ceiba.com.co.cita.modelo.entidad.Cita;

import java.time.LocalDateTime;

public interface RepositorioCita {

    Long guardar(Cita cita);

    boolean existeCitaEnHorarioDoctor(String doctorDocumento, LocalDateTime fechaHora);

    boolean existeCitaEnHorarioPaciente(String pacienteDocumento, LocalDateTime fechaHora);
}
