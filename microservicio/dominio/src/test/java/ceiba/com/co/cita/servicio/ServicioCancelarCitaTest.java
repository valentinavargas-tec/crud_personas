package ceiba.com.co.cita.servicio;

import ceiba.com.co.cita.modelo.entidad.Cita;
import ceiba.com.co.cita.modelo.entidad.EstadoCita;
import ceiba.com.co.cita.modelo.entidad.TipoCita;
import ceiba.com.co.cita.puerto.repositorio.RepositorioCita;
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
class ServicioCancelarCitaTest {

    private static final Long CITA_ID = 1L;
    private static final LocalDateTime FECHA_FUTURA = LocalDateTime.now().plusDays(5);

    @Mock
    private RepositorioCita repositorioCita;

    @InjectMocks
    private ServicioCancelarCita servicioCancelarCita;

    private Cita citaProgramada;

    @BeforeEach
    void setUp() {
        citaProgramada = Cita.builder()
                .conId(CITA_ID)
                .conPacienteDocumento("1024887449")
                .conDoctorDocumento("21754896")
                .conFechaHora(FECHA_FUTURA)
                .conTipoCita(TipoCita.CONSULTA_GENERAL)
                .conEstado(EstadoCita.PROGRAMADA)
                .conMotivo("Chequeo general")
                .build();
    }

    @Test
    @DisplayName("Debería cancelar la cita exitosamente y persistir el cambio")
    void deberiaCancelarCita_CuandoExisteYEstaProgramada() {
        when(repositorioCita.obtenerPorId(CITA_ID)).thenReturn(Optional.of(citaProgramada));

        Cita resultado = servicioCancelarCita.ejecutar(CITA_ID, "Calamidad doméstica");

        assertNotNull(resultado);
        assertEquals(EstadoCita.CANCELADA, resultado.getEstado());
        assertTrue(resultado.getObservaciones().contains("Calamidad doméstica"));
        verify(repositorioCita, times(1)).actualizar(citaProgramada);
    }

    @Test
    @DisplayName("Debería lanzar ExcepcionSinDatos cuando la cita no existe")
    void deberiaLanzarExcepcionSinDatos_CuandoCitaNoExiste() {
        when(repositorioCita.obtenerPorId(CITA_ID)).thenReturn(Optional.empty());

        assertThrows(ExcepcionSinDatos.class, () ->
                servicioCancelarCita.ejecutar(CITA_ID, "Motivo"));
        verify(repositorioCita, never()).actualizar(any());
    }

    @Test
    @DisplayName("Debería lanzar ExcepcionReglaNegocio cuando la cita no está en estado PROGRAMADA")
    void deberiaLanzarExcepcionReglaNegocio_CuandoCitaNoEstaProgramada() {
        Cita citaCompletada = Cita.builder()
                .conId(CITA_ID)
                .conPacienteDocumento("1024887449")
                .conDoctorDocumento("21754896")
                .conFechaHora(FECHA_FUTURA)
                .conTipoCita(TipoCita.CONSULTA_GENERAL)
                .conEstado(EstadoCita.COMPLETADA)
                .build();
        when(repositorioCita.obtenerPorId(CITA_ID)).thenReturn(Optional.of(citaCompletada));

        assertThrows(ExcepcionReglaNegocio.class, () ->
                servicioCancelarCita.ejecutar(CITA_ID, "Motivo"));
        verify(repositorioCita, never()).actualizar(any());
    }

    @Test
    @DisplayName("Debería cancelar cita exitosamente cuando la cita está en estado REASIGNADA")
    void deberiaCancelarCita_CuandoEstaEnEstadoReasignada() {
        Cita citaReasignada = Cita.builder()
                .conId(CITA_ID)
                .conPacienteDocumento("1024887449")
                .conDoctorDocumento("79123456")
                .conFechaHora(FECHA_FUTURA)
                .conTipoCita(TipoCita.CONSULTA_GENERAL)
                .conEstado(EstadoCita.REASIGNADA)
                .build();
        when(repositorioCita.obtenerPorId(CITA_ID)).thenReturn(Optional.of(citaReasignada));

        Cita resultado = servicioCancelarCita.ejecutar(CITA_ID, "Cancelación tras reasignación");

        assertNotNull(resultado);
        assertEquals(EstadoCita.CANCELADA, resultado.getEstado());
        verify(repositorioCita, times(1)).actualizar(citaReasignada);
    }
}
