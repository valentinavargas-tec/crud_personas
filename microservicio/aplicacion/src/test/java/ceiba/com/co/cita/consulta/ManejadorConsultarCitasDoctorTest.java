package ceiba.com.co.cita.consulta;

import ceiba.com.co.cita.consulta.manejador.ManejadorConsultarCitasDoctor;
import ceiba.com.co.cita.puerto.dao.DaoCita;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ManejadorConsultarCitasDoctorTest {

    private DaoCita daoCita;
    private ManejadorConsultarCitasDoctor manejador;

    @BeforeEach
    void setUp() {
        daoCita = Mockito.mock(DaoCita.class);
        manejador = new ManejadorConsultarCitasDoctor(daoCita);
    }

    @Test
    @DisplayName("Debería retornar lista de citas del doctor")
    void listarCitas() {
        DtoCita dto = new DtoCita(1L, "123", "DOC", LocalDateTime.now(), "TIPO", "ESTADO", "MOTIVO");
        when(daoCita.buscarCitasPorDoctor("DOC")).thenReturn(List.of(dto));

        List<DtoCita> resultado = manejador.ejecutar("DOC");

        assertEquals(1, resultado.size());
    }
}
