package ceiba.com.co.cita.servicio;

import ceiba.com.co.cita.modelo.entidad.Cita;
import ceiba.com.co.cita.puerto.repositorio.RepositorioCita;
import ceiba.com.co.doctor.modelo.entidad.Doctor;
import ceiba.com.co.doctor.puerto.repositorio.RepositorioDoctor;
import ceiba.com.co.excepcion.ExcepcionConflictoHorario;
import ceiba.com.co.excepcion.ExcepcionReglaNegocio;
import ceiba.com.co.excepcion.ExcepcionSinDatos;
import ceiba.com.co.paciente.puerto.repositorio.RepositorioPaciente;

public class ServicioAgendarCita {

    private static final String DOCTOR_NO_ENCONTRADO =
            "El doctor con número de documento %s no se encuentra registrado en el sistema.";
    private static final String PACIENTE_NO_ENCONTRADO =
            "El paciente con número de documento %s no se encuentra registrado en el sistema.";
    private static final String DOCTOR_INHABILITADO =
            "El doctor con número de documento %s no se encuentra habilitado para atender citas en este momento.";
    private static final String CRUCE_HORARIO_DOCTOR =
            "El doctor ya cuenta con una cita agendada en la fecha y hora %s.";
    private static final String CRUCE_HORARIO_PACIENTE =
            "El paciente %s ya tiene una cita médica programada en la fecha y hora solicitada.";

    private final RepositorioCita repositorioCita;
    private final RepositorioDoctor repositorioDoctor;
    private final RepositorioPaciente repositorioPaciente;

    public ServicioAgendarCita(RepositorioCita repositorioCita,
                                RepositorioDoctor repositorioDoctor,
                                RepositorioPaciente repositorioPaciente) {
        this.repositorioCita = repositorioCita;
        this.repositorioDoctor = repositorioDoctor;
        this.repositorioPaciente = repositorioPaciente;
    }

    public Long ejecutar(Cita cita) {
        Doctor doctor = validarDoctorExistente(cita.getDoctorDocumento());
        validarPacienteExistente(cita.getPacienteDocumento());
        validarDoctorHabilitado(doctor);
        validarDisponibilidadDoctor(cita);
        validarDisponibilidadPaciente(cita);
        return this.repositorioCita.guardar(cita);
    }

    private Doctor validarDoctorExistente(String doctorDocumento) {
        return this.repositorioDoctor.obtenerPorNumeroDocumento(doctorDocumento)
                .orElseThrow(() -> new ExcepcionSinDatos(
                        String.format(DOCTOR_NO_ENCONTRADO, doctorDocumento)));
    }

    private void validarPacienteExistente(String pacienteDocumento) {
        Long numDocumento;
        try {
            numDocumento = Long.parseLong(pacienteDocumento);
        } catch (NumberFormatException e) {
            throw new ceiba.com.co.excepcion.ExcepcionValorInvalido("El número de documento del paciente debe ser numérico");
        }
        boolean existe = this.repositorioPaciente.existeConNumeroDocumento(numDocumento);
        if (!existe) {
            throw new ExcepcionSinDatos(
                    String.format(PACIENTE_NO_ENCONTRADO, pacienteDocumento));
        }
    }

    private void validarDoctorHabilitado(Doctor doctor) {
        if (!doctor.isHabilitado()) {
            throw new ExcepcionReglaNegocio(
                    String.format(DOCTOR_INHABILITADO, doctor.getNumeroDocumento()));
        }
    }

    private void validarDisponibilidadDoctor(Cita cita) {
        boolean tieneCruce = this.repositorioCita.existeCitaEnHorarioDoctor(
                cita.getDoctorDocumento(), cita.getFechaHora());
        if (tieneCruce) {
            throw new ExcepcionConflictoHorario(
                    String.format(CRUCE_HORARIO_DOCTOR, cita.getFechaHora()));
        }
    }

    private void validarDisponibilidadPaciente(Cita cita) {
        boolean tieneCruce = this.repositorioCita.existeCitaEnHorarioPaciente(
                cita.getPacienteDocumento(), cita.getFechaHora());
        if (tieneCruce) {
            throw new ExcepcionConflictoHorario(
                    String.format(CRUCE_HORARIO_PACIENTE, cita.getPacienteDocumento()));
        }
    }
}
