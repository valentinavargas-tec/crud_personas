package ceiba.com.co.cita.comando.manejador;

import ceiba.com.co.ComandoRespuesta;
import ceiba.com.co.cita.comando.ComandoAgendarCita;
import ceiba.com.co.cita.comando.fabrica.FabricaCita;
import ceiba.com.co.cita.consulta.DtoCita;
import ceiba.com.co.cita.modelo.entidad.Cita;
import ceiba.com.co.cita.modelo.entidad.EstadoCita;
import ceiba.com.co.cita.modelo.entidad.TipoCita;
import ceiba.com.co.cita.servicio.ServicioAgendarCita;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ManejadorAgendarCitaTest {

    private ServicioAgendarCita servicioAgendarCita;
    private FabricaCita fabricaCita;
    private ManejadorAgendarCita manejadorAgendarCita;

    private static final LocalDateTime FECHA_FUTURA = LocalDateTime.now().plusDays(5);

    @BeforeEach
    void setUp() {
        servicioAgendarCita = Mockito.mock(ServicioAgendarCita.class);
        fabricaCita = Mockito.mock(FabricaCita.class);
        manejadorAgendarCita = new ManejadorAgendarCita(servicioAgendarCita, fabricaCita);
    }

    @Test
    @DisplayName("Debería delegar al servicio y retornar ComandoRespuesta con DtoCita y estado PROGRAMADA")
    void deberiaEjecutarManejador_YRetornarDtoCitaConIdYEstadoProgramada() {
        // Arrange
        ComandoAgendarCita comando = new ComandoAgendarCita(
                "123456789", "DOC-001", FECHA_FUTURA, "CONSULTA_GENERAL", "Chequeo");

        Cita cita = Cita.builder()
                .conPacienteDocumento("123456789")
                .conDoctorDocumento("DOC-001")
                .conFechaHora(FECHA_FUTURA)
                .conTipoCita(TipoCita.CONSULTA_GENERAL)
                .conEstado(EstadoCita.PROGRAMADA)
                .conMotivo("Chequeo")
                .build();

        Mockito.when(fabricaCita.crear(comando)).thenReturn(cita);
        Mockito.when(servicioAgendarCita.ejecutar(cita)).thenReturn(42L);

        // Act
        ComandoRespuesta<DtoCita> respuesta = manejadorAgendarCita.ejecutar(comando);

        // Assert
        assertNotNull(respuesta);
        DtoCita dto = respuesta.getValor();
        assertEquals(42L, dto.id());
        assertEquals("123456789", dto.pacienteDocumento());
        assertEquals("DOC-001", dto.doctorDocumento());
        assertEquals("PROGRAMADA", dto.estado());
        assertEquals("CONSULTA_GENERAL", dto.tipoCita());

        Mockito.verify(servicioAgendarCita, Mockito.times(1)).ejecutar(cita);
    }
}
