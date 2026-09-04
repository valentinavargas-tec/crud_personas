package ceiba.com.co.cita.consulta;

import ceiba.com.co.cita.consulta.manejador.ManejadorConsultarCita;
import ceiba.com.co.cita.puerto.dao.DaoCita;
import ceiba.com.co.excepcion.ExcepcionSinDatos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ManejadorConsultarCitaTest {

    private DaoCita daoCita;
    private ManejadorConsultarCita manejador;

    @BeforeEach
    void setUp() {
        daoCita = Mockito.mock(DaoCita.class);
        manejador = new ManejadorConsultarCita(daoCita);
    }

    @Test
    @DisplayName("Debería retornar DtoCita cuando la cita existe")
    void citaExiste() {
        DtoCita dto = new DtoCita(1L, "123", "DOC", LocalDateTime.now(), "TIPO", "ESTADO", "MOTIVO");
        when(daoCita.buscarPorId(1L)).thenReturn(Optional.of(dto));

        DtoCita resultado = manejador.ejecutar(1L);

        assertEquals(1L, resultado.id());
    }

    @Test
    @DisplayName("Debería lanzar ExcepcionSinDatos cuando la cita no existe")
    void citaNoExiste() {
        when(daoCita.buscarPorId(1L)).thenReturn(Optional.empty());

        assertThrows(ExcepcionSinDatos.class, () -> manejador.ejecutar(1L));
    }
}
