package ceiba.com.co.cita.servicio;

import ceiba.com.co.cita.modelo.entidad.Cita;
import ceiba.com.co.cita.modelo.entidad.EstadoCita;
import ceiba.com.co.cita.modelo.entidad.TipoCita;
import ceiba.com.co.cita.puerto.repositorio.RepositorioCita;
import ceiba.com.co.excepcion.ExcepcionConflictoHorario;
import ceiba.com.co.excepcion.ExcepcionReglaNegocio;
import ceiba.com.co.excepcion.ExcepcionSinDatos;
import ceiba.com.co.excepcion.ExcepcionValorInvalido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ServicioActualizarCitaTest {

    private static final Long CITA_ID = 1L;
    private static final String DOCTOR_DOC = "DOC-001";
    private static final String PACIENTE_DOC = "123456789";
    private static final LocalDateTime FECHA_FUTURA = LocalDateTime.now().plusDays(5);
    private static final LocalDateTime NUEVA_FECHA_FUTURA = LocalDateTime.now().plusDays(8);
    private static final LocalDateTime FECHA_PASADA = LocalDateTime.now().minusDays(2);

    private RepositorioCita repositorioCita;
    private ServicioActualizarCita servicioActualizarCita;

    @BeforeEach
    void setUp() {
        repositorioCita = Mockito.mock(RepositorioCita.class);
        servicioActualizarCita = new ServicioActualizarCita(repositorioCita);
    }

    @Test
    @DisplayName("Debería reprogramar cita exitosamente cuando todos los datos y disponibilidades son válidos")
    void deberiaReprogramarCita_CuandoDatosValidos() {
        // Arrange
        Cita cita = citaProgramada();
        Mockito.when(repositorioCita.obtenerPorId(CITA_ID)).thenReturn(Optional.of(cita));
        Mockito.when(repositorioCita.existeCitaEnHorarioDoctorExcluyendoCita(CITA_ID, DOCTOR_DOC, NUEVA_FECHA_FUTURA)).thenReturn(false);
        Mockito.when(repositorioCita.existeCitaEnHorarioPacienteExcluyendoCita(CITA_ID, PACIENTE_DOC, NUEVA_FECHA_FUTURA)).thenReturn(false);

        // Act
        Cita actualizada = servicioActualizarCita.ejecutar(CITA_ID, NUEVA_FECHA_FUTURA, TipoCita.ESPECIALIZADA, "Nuevo motivo");

        // Assert
        assertEquals(NUEVA_FECHA_FUTURA, actualizada.getFechaHora());
        assertEquals(TipoCita.ESPECIALIZADA, actualizada.getTipoCita());
        assertEquals("Nuevo motivo", actualizada.getMotivo());
        Mockito.verify(repositorioCita, Mockito.times(1)).actualizar(cita);
    }

    @Test
    @DisplayName("Debería lanzar ExcepcionSinDatos (404) cuando la cita a reprogramar no existe")
    void deberiaLanzar404_CuandoCitaNoExiste() {
        // Arrange
        Mockito.when(repositorioCita.obtenerPorId(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ExcepcionSinDatos.class, () ->
                servicioActualizarCita.ejecutar(999L, NUEVA_FECHA_FUTURA, TipoCita.CONTROL, "Motivo"));
        Mockito.verify(repositorioCita, Mockito.never()).actualizar(Mockito.any());
    }

    @Test
    @DisplayName("Debería lanzar ExcepcionReglaNegocio (400) cuando la cita no está en estado PROGRAMADA")
    void deberiaLanzar400_CuandoCitaNoEstaProgramada() {
        // Arrange
        Cita citaCancelada = Cita.builder()
                .conId(CITA_ID)
                .conPacienteDocumento(PACIENTE_DOC)
                .conDoctorDocumento(DOCTOR_DOC)
                .conFechaHora(FECHA_FUTURA)
                .conTipoCita(TipoCita.CONSULTA_GENERAL)
                .conEstado(EstadoCita.CANCELADA)
                .build();
        Mockito.when(repositorioCita.obtenerPorId(CITA_ID)).thenReturn(Optional.of(citaCancelada));

        // Act & Assert
        assertThrows(ExcepcionReglaNegocio.class, () ->
                servicioActualizarCita.ejecutar(CITA_ID, NUEVA_FECHA_FUTURA, TipoCita.CONTROL, "Motivo"));
        Mockito.verify(repositorioCita, Mockito.never()).actualizar(Mockito.any());
    }

    @Test
    @DisplayName("Regla 10 — Debería lanzar ExcepcionConflictoHorario (409) cuando el doctor ya tiene cita en el horario")
    void deberiaLanzar409_CuandoDoctorTieneConflictoHorario() {
        // Arrange
        Cita cita = citaProgramada();
        Mockito.when(repositorioCita.obtenerPorId(CITA_ID)).thenReturn(Optional.of(cita));
        Mockito.when(repositorioCita.existeCitaEnHorarioDoctorExcluyendoCita(CITA_ID, DOCTOR_DOC, NUEVA_FECHA_FUTURA)).thenReturn(true);

        // Act & Assert
        assertThrows(ExcepcionConflictoHorario.class, () ->
                servicioActualizarCita.ejecutar(CITA_ID, NUEVA_FECHA_FUTURA, TipoCita.CONTROL, "Motivo"));
        Mockito.verify(repositorioCita, Mockito.never()).actualizar(Mockito.any());
    }

    @Test
    @DisplayName("Regla 10 — Debería lanzar ExcepcionConflictoHorario (409) cuando el paciente ya tiene cita en el horario")
    void deberiaLanzar409_CuandoPacienteTieneConflictoHorario() {
        // Arrange
        Cita cita = citaProgramada();
        Mockito.when(repositorioCita.obtenerPorId(CITA_ID)).thenReturn(Optional.of(cita));
        Mockito.when(repositorioCita.existeCitaEnHorarioDoctorExcluyendoCita(CITA_ID, DOCTOR_DOC, NUEVA_FECHA_FUTURA)).thenReturn(false);
        Mockito.when(repositorioCita.existeCitaEnHorarioPacienteExcluyendoCita(CITA_ID, PACIENTE_DOC, NUEVA_FECHA_FUTURA)).thenReturn(true);

        // Act & Assert
        assertThrows(ExcepcionConflictoHorario.class, () ->
                servicioActualizarCita.ejecutar(CITA_ID, NUEVA_FECHA_FUTURA, TipoCita.CONTROL, "Motivo"));
        Mockito.verify(repositorioCita, Mockito.never()).actualizar(Mockito.any());
    }

    @Test
    @DisplayName("Regla 6 — Debería lanzar ExcepcionValorInvalido (400) cuando la nueva fecha es pasada")
    void deberiaLanzar400_CuandoNuevaFechaEsPasada() {
        // Arrange
        Cita cita = citaProgramada();
        Mockito.when(repositorioCita.obtenerPorId(CITA_ID)).thenReturn(Optional.of(cita));
        Mockito.when(repositorioCita.existeCitaEnHorarioDoctorExcluyendoCita(CITA_ID, DOCTOR_DOC, FECHA_PASADA)).thenReturn(false);
        Mockito.when(repositorioCita.existeCitaEnHorarioPacienteExcluyendoCita(CITA_ID, PACIENTE_DOC, FECHA_PASADA)).thenReturn(false);

        // Act & Assert
        assertThrows(ExcepcionValorInvalido.class, () ->
                servicioActualizarCita.ejecutar(CITA_ID, FECHA_PASADA, TipoCita.CONTROL, "Motivo"));
        Mockito.verify(repositorioCita, Mockito.never()).actualizar(Mockito.any());
    }

    private Cita citaProgramada() {
        return Cita.builder()
                .conId(CITA_ID)
                .conPacienteDocumento(PACIENTE_DOC)
                .conDoctorDocumento(DOCTOR_DOC)
                .conFechaHora(FECHA_FUTURA)
                .conTipoCita(TipoCita.CONSULTA_GENERAL)
                .conEstado(EstadoCita.PROGRAMADA)
                .conMotivo("Chequeo inicial")
                .build();
    }
}
