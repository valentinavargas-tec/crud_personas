package ceiba.com.co.cita.servicio;

import ceiba.com.co.cita.modelo.entidad.Cita;
import ceiba.com.co.cita.modelo.entidad.EstadoCita;
import ceiba.com.co.cita.modelo.entidad.TipoCita;
import ceiba.com.co.cita.puerto.repositorio.RepositorioCita;
import ceiba.com.co.doctor.modelo.entidad.Doctor;
import ceiba.com.co.doctor.puerto.repositorio.RepositorioDoctor;
import ceiba.com.co.excepcion.ExcepcionConflictoHorario;
import ceiba.com.co.excepcion.ExcepcionReglaNegocio;
import ceiba.com.co.excepcion.ExcepcionSinDatos;
import ceiba.com.co.paciente.puerto.repositorio.RepositorioPaciente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ServicioAgendarCitaTest {

    private static final String DOCTOR_DOC = "DOC-001";
    private static final String PACIENTE_DOC = "123456789";
    private static final LocalDateTime FECHA_FUTURA = LocalDateTime.now().plusDays(10);

    private RepositorioCita repositorioCita;
    private RepositorioDoctor repositorioDoctor;
    private RepositorioPaciente repositorioPaciente;
    private ServicioAgendarCita servicioAgendarCita;

    @BeforeEach
    void setUp() {
        repositorioCita = Mockito.mock(RepositorioCita.class);
        repositorioDoctor = Mockito.mock(RepositorioDoctor.class);
        repositorioPaciente = Mockito.mock(RepositorioPaciente.class);
        servicioAgendarCita = new ServicioAgendarCita(repositorioCita, repositorioDoctor, repositorioPaciente);
    }

    @Test
    @DisplayName("Debería agendar cita exitosamente cuando todos los datos son válidos")
    void deberiaAgendarCita_CuandoDatosValidos() {
        // Arrange
        Cita cita = citaValida();
        Doctor doctorHabilitado = doctorHabilitado();
        Mockito.when(repositorioDoctor.obtenerPorNumeroDocumento(DOCTOR_DOC))
                .thenReturn(Optional.of(doctorHabilitado));
        Mockito.when(repositorioPaciente.existeConNumeroDocumento(123456789L)).thenReturn(true);
        Mockito.when(repositorioCita.existeCitaEnHorarioDoctor(DOCTOR_DOC, FECHA_FUTURA)).thenReturn(false);
        Mockito.when(repositorioCita.existeCitaEnHorarioPaciente(PACIENTE_DOC, FECHA_FUTURA)).thenReturn(false);
        Mockito.when(repositorioCita.guardar(cita)).thenReturn(1L);

        // Act
        Long idGenerado = servicioAgendarCita.ejecutar(cita);

        // Assert
        assertEquals(1L, idGenerado);
        Mockito.verify(repositorioCita, Mockito.times(1)).guardar(cita);
    }

    @Test
    @DisplayName("Regla 1 — Debería lanzar ExcepcionSinDatos (404) cuando el doctor no existe")
    void deberiaLanzar404_CuandoDoctorNoExiste() {
        // Arrange
        Cita cita = citaValida();
        Mockito.when(repositorioDoctor.obtenerPorNumeroDocumento(DOCTOR_DOC))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ExcepcionSinDatos.class, () -> servicioAgendarCita.ejecutar(cita));
        Mockito.verify(repositorioCita, Mockito.never()).guardar(Mockito.any());
    }

    @Test
    @DisplayName("Regla 2 — Debería lanzar ExcepcionSinDatos (404) cuando el paciente no existe")
    void deberiaLanzar404_CuandoPacienteNoExiste() {
        // Arrange
        Cita cita = citaValida();
        Mockito.when(repositorioDoctor.obtenerPorNumeroDocumento(DOCTOR_DOC))
                .thenReturn(Optional.of(doctorHabilitado()));
        Mockito.when(repositorioPaciente.existeConNumeroDocumento(123456789L)).thenReturn(false);

        // Act & Assert
        assertThrows(ExcepcionSinDatos.class, () -> servicioAgendarCita.ejecutar(cita));
        Mockito.verify(repositorioCita, Mockito.never()).guardar(Mockito.any());
    }

    @Test
    @DisplayName("Regla 13 — Debería lanzar ExcepcionReglaNegocio (400) cuando el doctor está inhabilitado")
    void deberiaLanzar400_CuandoDoctorEstaInhabilitado() {
        // Arrange
        Cita cita = citaValida();
        Doctor doctorInhabilitado = Doctor.builder()
                .conNumeroDocumento(DOCTOR_DOC)
                .conNombre("Doctor")
                .conApellido("Inactivo")
                .conTarjetaProfesional("TP-999")
                .conEspecialidad("CARDIOLOGIA")
                .conCorreoInstitucional("inactivo@hospital.com")
                .conHabilitado(false)
                .build();
        Mockito.when(repositorioDoctor.obtenerPorNumeroDocumento(DOCTOR_DOC))
                .thenReturn(Optional.of(doctorInhabilitado));
        Mockito.when(repositorioPaciente.existeConNumeroDocumento(123456789L)).thenReturn(true);

        // Act & Assert
        assertThrows(ExcepcionReglaNegocio.class, () -> servicioAgendarCita.ejecutar(cita));
        Mockito.verify(repositorioCita, Mockito.never()).guardar(Mockito.any());
    }

    @Test
    @DisplayName("Regla 4 — Debería lanzar ExcepcionConflictoHorario (409) cuando el doctor tiene cruce de horario")
    void deberiaLanzar409_CuandoDoctorTieneCruceDeHorario() {
        // Arrange
        Cita cita = citaValida();
        Mockito.when(repositorioDoctor.obtenerPorNumeroDocumento(DOCTOR_DOC))
                .thenReturn(Optional.of(doctorHabilitado()));
        Mockito.when(repositorioPaciente.existeConNumeroDocumento(123456789L)).thenReturn(true);
        Mockito.when(repositorioCita.existeCitaEnHorarioDoctor(DOCTOR_DOC, FECHA_FUTURA)).thenReturn(true);

        // Act & Assert
        assertThrows(ExcepcionConflictoHorario.class, () -> servicioAgendarCita.ejecutar(cita));
        Mockito.verify(repositorioCita, Mockito.never()).guardar(Mockito.any());
    }

    @Test
    @DisplayName("Regla 5 — Debería lanzar ExcepcionConflictoHorario (409) cuando el paciente tiene cruce de horario")
    void deberiaLanzar409_CuandoPacienteTieneCruceDeHorario() {
        // Arrange
        Cita cita = citaValida();
        Mockito.when(repositorioDoctor.obtenerPorNumeroDocumento(DOCTOR_DOC))
                .thenReturn(Optional.of(doctorHabilitado()));
        Mockito.when(repositorioPaciente.existeConNumeroDocumento(123456789L)).thenReturn(true);
        Mockito.when(repositorioCita.existeCitaEnHorarioDoctor(DOCTOR_DOC, FECHA_FUTURA)).thenReturn(false);
        Mockito.when(repositorioCita.existeCitaEnHorarioPaciente(PACIENTE_DOC, FECHA_FUTURA)).thenReturn(true);

        // Act & Assert
        assertThrows(ExcepcionConflictoHorario.class, () -> servicioAgendarCita.ejecutar(cita));
        Mockito.verify(repositorioCita, Mockito.never()).guardar(Mockito.any());
    }

    // ─────────────────────── Helpers ───────────────────────

    private Cita citaValida() {
        return Cita.builder()
                .conPacienteDocumento(PACIENTE_DOC)
                .conDoctorDocumento(DOCTOR_DOC)
                .conFechaHora(FECHA_FUTURA)
                .conTipoCita(TipoCita.CONSULTA_GENERAL)
                .conEstado(EstadoCita.PROGRAMADA)
                .conMotivo("Chequeo de rutina")
                .build();
    }

    private Doctor doctorHabilitado() {
        return Doctor.builder()
                .conNumeroDocumento(DOCTOR_DOC)
                .conNombre("Ana")
                .conApellido("Torres")
                .conTarjetaProfesional("TP-001")
                .conEspecialidad("CARDIOLOGIA")
                .conCorreoInstitucional("ana@hospital.com")
                .conHabilitado(true)
                .build();
    }
}
