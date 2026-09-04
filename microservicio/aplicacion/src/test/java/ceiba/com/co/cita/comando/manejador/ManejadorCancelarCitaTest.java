package ceiba.com.co.cita.comando.manejador;

import ceiba.com.co.ComandoRespuesta;
import ceiba.com.co.cita.comando.ComandoCancelarCita;
import ceiba.com.co.cita.consulta.DtoCita;
import ceiba.com.co.cita.modelo.entidad.Cita;
import ceiba.com.co.cita.modelo.entidad.EstadoCita;
import ceiba.com.co.cita.modelo.entidad.TipoCita;
import ceiba.com.co.cita.servicio.ServicioCancelarCita;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ManejadorCancelarCitaTest {

    private static final Long CITA_ID = 1L;

    @Mock
    private ServicioCancelarCita servicioCancelarCita;

    @InjectMocks
    private ManejadorCancelarCita manejadorCancelarCita;

    @Test
    @DisplayName("Debería ejecutar servicio de cancelación y retornar ComandoRespuesta con DtoCita en CANCELADA")
    void deberiaEjecutarManejador_YRetornarDtoCitaCancelada() {
        Cita citaCancelada = Cita.builder()
                .conId(CITA_ID)
                .conPacienteDocumento("1024887449")
                .conDoctorDocumento("21754896")
                .conFechaHora(LocalDateTime.now().plusDays(3))
                .conTipoCita(TipoCita.CONSULTA_GENERAL)
                .conEstado(EstadoCita.CANCELADA)
                .conMotivo("Cancelación por viaje")
                .build();

        ComandoCancelarCita comando = new ComandoCancelarCita("Cancelación por viaje");
        when(servicioCancelarCita.ejecutar(CITA_ID, "Cancelación por viaje")).thenReturn(citaCancelada);

        ComandoRespuesta<DtoCita> respuesta = manejadorCancelarCita.ejecutar(CITA_ID, comando);

        assertNotNull(respuesta);
        assertEquals("Cita médica cancelada exitosamente", respuesta.getMensaje());
        assertEquals(EstadoCita.CANCELADA.name(), respuesta.getValor().estado());
        verify(servicioCancelarCita, times(1)).ejecutar(CITA_ID, "Cancelación por viaje");
    }
}
