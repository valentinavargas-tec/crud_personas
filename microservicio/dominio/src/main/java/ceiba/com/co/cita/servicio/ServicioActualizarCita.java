package ceiba.com.co.cita.servicio;

import ceiba.com.co.cita.modelo.entidad.Cita;
import ceiba.com.co.cita.modelo.entidad.EstadoCita;
import ceiba.com.co.cita.modelo.entidad.TipoCita;
import ceiba.com.co.cita.puerto.repositorio.RepositorioCita;
import ceiba.com.co.excepcion.ExcepcionConflictoHorario;
import ceiba.com.co.excepcion.ExcepcionReglaNegocio;
import ceiba.com.co.excepcion.ExcepcionSinDatos;

import java.time.LocalDateTime;

public class ServicioActualizarCita {

    private static final String CITA_NO_ENCONTRADA = "La cita médica con identificador %s no se encuentra registrada en el sistema.";
    private static final String REPROGRAMACION_INVALIDA_ESTADO = "Solo se pueden reprogramar citas médicas en estado PROGRAMADA.";
    private static final String HORARIO_NO_DISPONIBLE = "No hay disponibilidad para el doctor o el paciente en el nuevo horario solicitado.";

    private final RepositorioCita repositorioCita;

    public ServicioActualizarCita(RepositorioCita repositorioCita) {
        this.repositorioCita = repositorioCita;
    }

    public Cita ejecutar(Long idCita, LocalDateTime nuevaFechaHora, TipoCita nuevoTipoCita, String nuevoMotivo) {
        Cita cita = this.repositorioCita.obtenerPorId(idCita)
                .orElseThrow(() -> new ExcepcionSinDatos(String.format(CITA_NO_ENCONTRADA, idCita)));

        if (!EstadoCita.PROGRAMADA.equals(cita.getEstado()) && !EstadoCita.REASIGNADA.equals(cita.getEstado())) {
            throw new ExcepcionReglaNegocio(REPROGRAMACION_INVALIDA_ESTADO);
        }

        validarDisponibilidad(idCita, cita.getDoctorDocumento(), cita.getPacienteDocumento(), nuevaFechaHora);

        cita.reprogramar(nuevaFechaHora, nuevoTipoCita, nuevoMotivo);
        this.repositorioCita.actualizar(cita);

        return cita;
    }

    private void validarDisponibilidad(Long idCita, String doctorDocumento, String pacienteDocumento, LocalDateTime nuevaFechaHora) {
        boolean cruceDoctor = this.repositorioCita.existeCitaEnHorarioDoctorExcluyendoCita(idCita, doctorDocumento, nuevaFechaHora);
        boolean crucePaciente = this.repositorioCita.existeCitaEnHorarioPacienteExcluyendoCita(idCita, pacienteDocumento, nuevaFechaHora);
        if (cruceDoctor || crucePaciente) {
            throw new ExcepcionConflictoHorario(HORARIO_NO_DISPONIBLE);
        }
    }
}
