package ceiba.com.co.cita.consulta;

import ceiba.com.co.cita.consulta.manejador.ManejadorConsultarCitasPaciente;
import ceiba.com.co.cita.puerto.dao.DaoCita;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ManejadorConsultarCitasPacienteTest {

    private DaoCita daoCita;
    private ManejadorConsultarCitasPaciente manejador;

    @BeforeEach
    void setUp() {
        daoCita = Mockito.mock(DaoCita.class);
        manejador = new ManejadorConsultarCitasPaciente(daoCita);
    }

    @Test
    @DisplayName("Debería retornar lista de citas del paciente")
    void listarCitas() {
        DtoCita dto = new DtoCita(1L, "123", "DOC", LocalDateTime.now(), "TIPO", "ESTADO", "MOTIVO");
        when(daoCita.buscarCitasPorPaciente("123")).thenReturn(List.of(dto));

        List<DtoCita> resultado = manejador.ejecutar("123");

        assertEquals(1, resultado.size());
    }
}
