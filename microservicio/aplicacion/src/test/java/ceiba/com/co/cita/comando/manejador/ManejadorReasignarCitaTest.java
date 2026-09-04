package ceiba.com.co.cita.comando.manejador;

import ceiba.com.co.ComandoRespuesta;
import ceiba.com.co.cita.comando.ComandoReasignarCita;
import ceiba.com.co.cita.consulta.DtoCita;
import ceiba.com.co.cita.modelo.entidad.Cita;
import ceiba.com.co.cita.modelo.entidad.EstadoCita;
import ceiba.com.co.cita.modelo.entidad.TipoCita;
import ceiba.com.co.cita.servicio.ServicioReasignarCita;
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
class ManejadorReasignarCitaTest {

    private static final Long CITA_ID = 1L;
    private static final String NUEVO_DOCTOR_DOC = "79123456";

    @Mock
    private ServicioReasignarCita servicioReasignarCita;

    @InjectMocks
    private ManejadorReasignarCita manejadorReasignarCita;

    @Test
    @DisplayName("Debería ejecutar servicio de reasignación y retornar ComandoRespuesta con DtoCita actualizado")
    void deberiaEjecutarManejador_YRetornarDtoCitaReasignada() {
        Cita citaReasignada = Cita.builder()
                .conId(CITA_ID)
                .conPacienteDocumento("1024887449")
                .conDoctorDocumento(NUEVO_DOCTOR_DOC)
                .conFechaHora(LocalDateTime.now().plusDays(3))
                .conTipoCita(TipoCita.CONSULTA_GENERAL)
                .conEstado(EstadoCita.REASIGNADA)
                .conMotivo("Chequeo general")
                .build();

        ComandoReasignarCita comando = new ComandoReasignarCita(NUEVO_DOCTOR_DOC);
        when(servicioReasignarCita.ejecutar(CITA_ID, NUEVO_DOCTOR_DOC)).thenReturn(citaReasignada);

        ComandoRespuesta<DtoCita> respuesta = manejadorReasignarCita.ejecutar(CITA_ID, comando);

        assertNotNull(respuesta);
        assertEquals("Cita médica reasignada exitosamente", respuesta.getMensaje());
        assertEquals(NUEVO_DOCTOR_DOC, respuesta.getValor().doctorDocumento());
        assertEquals(EstadoCita.REASIGNADA.name(), respuesta.getValor().estado());
        verify(servicioReasignarCita, times(1)).ejecutar(CITA_ID, NUEVO_DOCTOR_DOC);
    }
}
