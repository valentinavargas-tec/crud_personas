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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServicioReasignarCitaTest {

    private static final Long CITA_ID = 1L;
    private static final String DOCTOR_ORIGINAL_DOC = "21754896";
    private static final String NUEVO_DOCTOR_DOC = "79123456";
    private static final LocalDateTime FECHA_FUTURA = LocalDateTime.now().plusDays(5);

    @Mock
    private RepositorioCita repositorioCita;

    @Mock
    private RepositorioDoctor repositorioDoctor;

    @InjectMocks
    private ServicioReasignarCita servicioReasignarCita;

    private Cita citaProgramada;
    private Doctor doctorOriginal;
    private Doctor nuevoDoctor;

    @BeforeEach
    void setUp() {
        citaProgramada = Cita.builder()
                .conId(CITA_ID)
                .conPacienteDocumento("1024887449")
                .conDoctorDocumento(DOCTOR_ORIGINAL_DOC)
                .conFechaHora(FECHA_FUTURA)
                .conTipoCita(TipoCita.CONSULTA_GENERAL)
                .conEstado(EstadoCita.PROGRAMADA)
                .conMotivo("Chequeo general")
                .build();

        doctorOriginal = Doctor.builder()
                .conNumeroDocumento(DOCTOR_ORIGINAL_DOC)
                .conNombre("Carlos")
                .conApellido("Gómez")
                .conTarjetaProfesional("TP-001")
                .conEspecialidad("CARDIOLOGIA")
                .conCorreoInstitucional("carlos.gomez@hospital.com")
                .conHabilitado(true)
                .build();

        nuevoDoctor = Doctor.builder()
                .conNumeroDocumento(NUEVO_DOCTOR_DOC)
                .conNombre("Andrés")
                .conApellido("Pérez")
                .conTarjetaProfesional("TP-002")
                .conEspecialidad("CARDIOLOGIA")
                .conCorreoInstitucional("andres.perez@hospital.com")
                .conHabilitado(true)
                .build();
    }

    @Test
    @DisplayName("Debería reasignar la cita exitosamente al nuevo doctor cuando cumple todas las reglas")
    void deberiaReasignarCita_CuandoCumpleTodasLasReglas() {
        when(repositorioCita.obtenerPorId(CITA_ID)).thenReturn(Optional.of(citaProgramada));
        when(repositorioDoctor.obtenerPorNumeroDocumento(DOCTOR_ORIGINAL_DOC)).thenReturn(Optional.of(doctorOriginal));
        when(repositorioDoctor.obtenerPorNumeroDocumento(NUEVO_DOCTOR_DOC)).thenReturn(Optional.of(nuevoDoctor));
        when(repositorioCita.existeCitaEnHorarioDoctor(NUEVO_DOCTOR_DOC, FECHA_FUTURA)).thenReturn(false);

        Cita resultado = servicioReasignarCita.ejecutar(CITA_ID, NUEVO_DOCTOR_DOC);

        assertNotNull(resultado);
        assertEquals(NUEVO_DOCTOR_DOC, resultado.getDoctorDocumento());
        assertEquals(EstadoCita.REASIGNADA, resultado.getEstado());
        assertTrue(resultado.getObservaciones().contains("Reasignada a doctor: " + NUEVO_DOCTOR_DOC));
        verify(repositorioCita, times(1)).actualizar(citaProgramada);
    }

    @Test
    @DisplayName("Debería lanzar ExcepcionSinDatos cuando la cita no existe")
    void deberiaLanzarExcepcionSinDatos_CuandoCitaNoExiste() {
        when(repositorioCita.obtenerPorId(CITA_ID)).thenReturn(Optional.empty());

        assertThrows(ExcepcionSinDatos.class, () ->
                servicioReasignarCita.ejecutar(CITA_ID, NUEVO_DOCTOR_DOC));
        verify(repositorioCita, never()).actualizar(any());
    }

    @Test
    @DisplayName("Debería lanzar ExcepcionReglaNegocio si se intenta reasignar al mismo doctor actual")
    void deberiaLanzarExcepcionReglaNegocio_CuandoEsElMismoDoctor() {
        when(repositorioCita.obtenerPorId(CITA_ID)).thenReturn(Optional.of(citaProgramada));

        assertThrows(ExcepcionReglaNegocio.class, () ->
                servicioReasignarCita.ejecutar(CITA_ID, DOCTOR_ORIGINAL_DOC));
        verify(repositorioCita, never()).actualizar(any());
    }

    @Test
    @DisplayName("Regla 1 — Debería lanzar ExcepcionSinDatos cuando el nuevo doctor no existe")
    void deberiaLanzarExcepcionSinDatos_CuandoNuevoDoctorNoExiste() {
        when(repositorioCita.obtenerPorId(CITA_ID)).thenReturn(Optional.of(citaProgramada));
        when(repositorioDoctor.obtenerPorNumeroDocumento(DOCTOR_ORIGINAL_DOC)).thenReturn(Optional.of(doctorOriginal));
        when(repositorioDoctor.obtenerPorNumeroDocumento(NUEVO_DOCTOR_DOC)).thenReturn(Optional.empty());

        assertThrows(ExcepcionSinDatos.class, () ->
                servicioReasignarCita.ejecutar(CITA_ID, NUEVO_DOCTOR_DOC));
    }

    @Test
    @DisplayName("Regla 13 — Debería lanzar ExcepcionReglaNegocio cuando el nuevo doctor está inhabilitado")
    void deberiaLanzarExcepcionReglaNegocio_CuandoNuevoDoctorEstaInhabilitado() {
        Doctor doctorInhabilitado = Doctor.builder()
                .conNumeroDocumento(NUEVO_DOCTOR_DOC)
                .conNombre("Andrés")
                .conApellido("Pérez")
                .conTarjetaProfesional("TP-002")
                .conEspecialidad("CARDIOLOGIA")
                .conCorreoInstitucional("andres.perez@hospital.com")
                .conHabilitado(false)
                .build();

        when(repositorioCita.obtenerPorId(CITA_ID)).thenReturn(Optional.of(citaProgramada));
        when(repositorioDoctor.obtenerPorNumeroDocumento(DOCTOR_ORIGINAL_DOC)).thenReturn(Optional.of(doctorOriginal));
        when(repositorioDoctor.obtenerPorNumeroDocumento(NUEVO_DOCTOR_DOC)).thenReturn(Optional.of(doctorInhabilitado));

        assertThrows(ExcepcionReglaNegocio.class, () ->
                servicioReasignarCita.ejecutar(CITA_ID, NUEVO_DOCTOR_DOC));
    }

    @Test
    @DisplayName("Regla 9 — Debería lanzar ExcepcionReglaNegocio cuando el nuevo doctor es de distinta especialidad")
    void deberiaLanzarExcepcionReglaNegocio_CuandoEspecialidadEsDiferente() {
        Doctor doctorPediatra = Doctor.builder()
                .conNumeroDocumento(NUEVO_DOCTOR_DOC)
                .conNombre("Andrés")
                .conApellido("Pérez")
                .conTarjetaProfesional("TP-002")
                .conEspecialidad("PEDIATRIA")
                .conCorreoInstitucional("andres.perez@hospital.com")
                .conHabilitado(true)
                .build();

        when(repositorioCita.obtenerPorId(CITA_ID)).thenReturn(Optional.of(citaProgramada));
        when(repositorioDoctor.obtenerPorNumeroDocumento(DOCTOR_ORIGINAL_DOC)).thenReturn(Optional.of(doctorOriginal));
        when(repositorioDoctor.obtenerPorNumeroDocumento(NUEVO_DOCTOR_DOC)).thenReturn(Optional.of(doctorPediatra));

        assertThrows(ExcepcionReglaNegocio.class, () ->
                servicioReasignarCita.ejecutar(CITA_ID, NUEVO_DOCTOR_DOC));
    }

    @Test
    @DisplayName("Regla 4 — Debería lanzar ExcepcionConflictoHorario cuando el nuevo doctor tiene cruce de horario")
    void deberiaLanzarExcepcionConflictoHorario_CuandoNuevoDoctorTieneCruce() {
        when(repositorioCita.obtenerPorId(CITA_ID)).thenReturn(Optional.of(citaProgramada));
        when(repositorioDoctor.obtenerPorNumeroDocumento(DOCTOR_ORIGINAL_DOC)).thenReturn(Optional.of(doctorOriginal));
        when(repositorioDoctor.obtenerPorNumeroDocumento(NUEVO_DOCTOR_DOC)).thenReturn(Optional.of(nuevoDoctor));
        when(repositorioCita.existeCitaEnHorarioDoctor(NUEVO_DOCTOR_DOC, FECHA_FUTURA)).thenReturn(true);

        assertThrows(ExcepcionConflictoHorario.class, () ->
                servicioReasignarCita.ejecutar(CITA_ID, NUEVO_DOCTOR_DOC));
    }

    @Test
    @DisplayName("Debería permitir reasignar una cita que ya fue reasignada previamente")
    void deberiaReasignarCita_CuandoCitaPreviamenteReasignada() {
        Cita citaReasignadaPrevia = Cita.builder()
                .conId(CITA_ID)
                .conPacienteDocumento("1024887449")
                .conDoctorDocumento(DOCTOR_ORIGINAL_DOC)
                .conFechaHora(FECHA_FUTURA)
                .conTipoCita(TipoCita.CONSULTA_GENERAL)
                .conEstado(EstadoCita.REASIGNADA)
                .build();

        when(repositorioCita.obtenerPorId(CITA_ID)).thenReturn(Optional.of(citaReasignadaPrevia));
        when(repositorioDoctor.obtenerPorNumeroDocumento(DOCTOR_ORIGINAL_DOC)).thenReturn(Optional.of(doctorOriginal));
        when(repositorioDoctor.obtenerPorNumeroDocumento(NUEVO_DOCTOR_DOC)).thenReturn(Optional.of(nuevoDoctor));
        when(repositorioCita.existeCitaEnHorarioDoctor(NUEVO_DOCTOR_DOC, FECHA_FUTURA)).thenReturn(false);

        Cita resultado = servicioReasignarCita.ejecutar(CITA_ID, NUEVO_DOCTOR_DOC);

        assertNotNull(resultado);
        assertEquals(NUEVO_DOCTOR_DOC, resultado.getDoctorDocumento());
        assertEquals(EstadoCita.REASIGNADA, resultado.getEstado());
        verify(repositorioCita, times(1)).actualizar(citaReasignadaPrevia);
    }
}
