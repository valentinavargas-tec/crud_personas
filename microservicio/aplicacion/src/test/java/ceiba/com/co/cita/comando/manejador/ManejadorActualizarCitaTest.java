package ceiba.com.co.cita.comando.manejador;

import ceiba.com.co.ComandoRespuesta;
import ceiba.com.co.cita.comando.ComandoActualizarCita;
import ceiba.com.co.cita.comando.fabrica.FabricaCita;
import ceiba.com.co.cita.consulta.DtoCita;
import ceiba.com.co.cita.modelo.entidad.Cita;
import ceiba.com.co.cita.modelo.entidad.EstadoCita;
import ceiba.com.co.cita.modelo.entidad.TipoCita;
import ceiba.com.co.cita.servicio.ServicioActualizarCita;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ManejadorActualizarCitaTest {

    private ServicioActualizarCita servicioActualizarCita;
    private FabricaCita fabricaCita;
    private ManejadorActualizarCita manejadorActualizarCita;

    private static final Long CITA_ID = 5L;
    private static final LocalDateTime NUEVA_FECHA = LocalDateTime.now().plusDays(7);

    @BeforeEach
    void setUp() {
        servicioActualizarCita = Mockito.mock(ServicioActualizarCita.class);
        fabricaCita = Mockito.mock(FabricaCita.class);
        manejadorActualizarCita = new ManejadorActualizarCita(servicioActualizarCita, fabricaCita);
    }

    @Test
    @DisplayName("Debería ejecutar actualización, delegar al servicio y retornar DTO con nueva fecha y tipo")
    void deberiaEjecutarManejador_YRetornarDtoCitaActualizado() {
        // Arrange
        ComandoActualizarCita comando = new ComandoActualizarCita(
                NUEVA_FECHA, "TELEMEDICINA", "Reajuste por viaje");

        Cita citaActualizada = Cita.builder()
                .conId(CITA_ID)
                .conPacienteDocumento("123456789")
                .conDoctorDocumento("DOC-001")
                .conFechaHora(NUEVA_FECHA)
                .conTipoCita(TipoCita.TELEMEDICINA)
                .conEstado(EstadoCita.PROGRAMADA)
                .conMotivo("Reajuste por viaje")
                .build();

        Mockito.when(fabricaCita.parsearTipoCita("TELEMEDICINA")).thenReturn(TipoCita.TELEMEDICINA);
        Mockito.when(servicioActualizarCita.ejecutar(CITA_ID, NUEVA_FECHA, TipoCita.TELEMEDICINA, "Reajuste por viaje"))
                .thenReturn(citaActualizada);

        // Act
        ComandoRespuesta<DtoCita> respuesta = manejadorActualizarCita.ejecutar(CITA_ID, comando);

        // Assert
        assertNotNull(respuesta);
        DtoCita dto = respuesta.getValor();
        assertEquals(CITA_ID, dto.id());
        assertEquals("123456789", dto.pacienteDocumento());
        assertEquals("DOC-001", dto.doctorDocumento());
        assertEquals(NUEVA_FECHA, dto.fechaHora());
        assertEquals("TELEMEDICINA", dto.tipoCita());
        assertEquals("PROGRAMADA", dto.estado());
        assertEquals("Reajuste por viaje", dto.motivo());

        Mockito.verify(servicioActualizarCita, Mockito.times(1))
                .ejecutar(CITA_ID, NUEVA_FECHA, TipoCita.TELEMEDICINA, "Reajuste por viaje");
    }
}
