package ceiba.com.co.cita.servicio;

import ceiba.com.co.ValidadorArgumento;
import ceiba.com.co.cita.modelo.entidad.Cita;
import ceiba.com.co.cita.modelo.entidad.EstadoCita;
import ceiba.com.co.cita.puerto.repositorio.RepositorioCita;
import ceiba.com.co.doctor.modelo.entidad.Doctor;
import ceiba.com.co.doctor.puerto.repositorio.RepositorioDoctor;
import ceiba.com.co.excepcion.ExcepcionConflictoHorario;
import ceiba.com.co.excepcion.ExcepcionReglaNegocio;
import ceiba.com.co.excepcion.ExcepcionSinDatos;

import java.time.LocalDateTime;

public class ServicioReasignarCita {

    private static final String CITA_NO_ENCONTRADA = "La cita médica con identificador %s no se encuentra registrada en el sistema.";
    private static final String REASIGNACION_INVALIDA_ESTADO = "Solo se pueden reasignar citas médicas en estado PROGRAMADA.";
    private static final String REASIGNACION_FECHA_PASADA = "No es posible reasignar la cita médica porque la fecha y hora programada ya transcurrió.";
    private static final String MISMO_DOCTOR = "La cita ya se encuentra asignada al doctor con documento %s.";
    private static final String DOCTOR_NO_ENCONTRADO = "El doctor con número de documento %s no se encuentra registrado en el sistema.";
    private static final String DOCTOR_INHABILITADO = "El doctor con número de documento %s no se encuentra habilitado para atender citas en este momento.";
    private static final String ESPECIALIDAD_DIFERENTE = "No se puede reasignar la cita. El nuevo doctor debe pertenecer a la misma especialidad (%s).";
    private static final String CRUCE_HORARIO_DOCTOR = "El doctor ya cuenta con una cita agendada en la fecha y hora %s.";

    private final RepositorioCita repositorioCita;
    private final RepositorioDoctor repositorioDoctor;

    public ServicioReasignarCita(RepositorioCita repositorioCita, RepositorioDoctor repositorioDoctor) {
        this.repositorioCita = repositorioCita;
        this.repositorioDoctor = repositorioDoctor;
    }

    public Cita ejecutar(Long idCita, String nuevoDoctorDocumento) {
        ValidadorArgumento.validarObligatorio(nuevoDoctorDocumento, "El documento del nuevo doctor es obligatorio");
        Cita cita = validarCitaExistenteYProgramada(idCita);

        if (nuevoDoctorDocumento.equals(cita.getDoctorDocumento())) {
            throw new ExcepcionReglaNegocio(String.format(MISMO_DOCTOR, nuevoDoctorDocumento));
        }

        Doctor doctorOriginal = obtenerDoctor(cita.getDoctorDocumento());
        Doctor nuevoDoctor = obtenerDoctor(nuevoDoctorDocumento);

        validarDoctorHabilitado(nuevoDoctor);
        validarMismaEspecialidad(doctorOriginal, nuevoDoctor);
        validarDisponibilidadDoctor(nuevoDoctorDocumento, cita.getFechaHora());

        cita.reasignar(nuevoDoctorDocumento);
        this.repositorioCita.actualizar(cita);

        return cita;
    }

    private Cita validarCitaExistenteYProgramada(Long idCita) {
        Cita cita = this.repositorioCita.obtenerPorId(idCita)
                .orElseThrow(() -> new ExcepcionSinDatos(String.format(CITA_NO_ENCONTRADA, idCita)));
        if (!EstadoCita.PROGRAMADA.equals(cita.getEstado()) && !EstadoCita.REASIGNADA.equals(cita.getEstado())) {
            throw new ExcepcionReglaNegocio(REASIGNACION_INVALIDA_ESTADO);
        }
        if (cita.getFechaHora().isBefore(LocalDateTime.now())) {
            throw new ExcepcionReglaNegocio(REASIGNACION_FECHA_PASADA);
        }
        return cita;
    }

    private Doctor obtenerDoctor(String doctorDocumento) {
        return this.repositorioDoctor.obtenerPorNumeroDocumento(doctorDocumento)
                .orElseThrow(() -> new ExcepcionSinDatos(String.format(DOCTOR_NO_ENCONTRADO, doctorDocumento)));
    }

    private void validarDoctorHabilitado(Doctor doctor) {
        if (!doctor.isHabilitado()) {
            throw new ExcepcionReglaNegocio(String.format(DOCTOR_INHABILITADO, doctor.getNumeroDocumento()));
        }
    }

    private void validarMismaEspecialidad(Doctor doctorOriginal, Doctor nuevoDoctor) {
        if (!nuevoDoctor.getEspecialidad().equalsIgnoreCase(doctorOriginal.getEspecialidad())) {
            throw new ExcepcionReglaNegocio(String.format(ESPECIALIDAD_DIFERENTE, doctorOriginal.getEspecialidad()));
        }
    }

    private void validarDisponibilidadDoctor(String doctorDocumento, LocalDateTime fechaHora) {
        boolean cruce = this.repositorioCita.existeCitaEnHorarioDoctor(doctorDocumento, fechaHora);
        if (cruce) {
            throw new ExcepcionConflictoHorario(String.format(CRUCE_HORARIO_DOCTOR, fechaHora));
        }
    }
}
