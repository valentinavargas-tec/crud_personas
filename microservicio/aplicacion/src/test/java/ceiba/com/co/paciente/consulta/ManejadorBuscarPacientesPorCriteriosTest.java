package ceiba.com.co.paciente.consulta;

import ceiba.com.co.paciente.modelo.dto.CriteriosBusquedaPaciente;
import ceiba.com.co.paciente.modelo.dto.Pagina;
import ceiba.com.co.paciente.modelo.dto.PacienteDTO;
import ceiba.com.co.paciente.puerto.dao.DaoPaciente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ManejadorBuscarPacientesPorCriteriosTest {

    private DaoPaciente daoPaciente;
    private ManejadorBuscarPacientesPorCriterios manejadorBuscarPacientesPorCriterios;

    @BeforeEach
    void setUp() {
        daoPaciente = mock(DaoPaciente.class);
        manejadorBuscarPacientesPorCriterios = new ManejadorBuscarPacientesPorCriterios(daoPaciente);
    }

    @Test
    @DisplayName("Debería retornar página de pacientes al buscar por criterios")
    void deberia_RetornarPaginaPacientes_Cuando_BusquedaPorCriteriosRetornaResultados() {
        CriteriosBusquedaPaciente criterios = new CriteriosBusquedaPaciente("Juan", "Perez", 20, 40, 0, 10, null);
        PacienteDTO pacienteDTO = new PacienteDTO(12345L, "CC", "Juan", "Perez",
                LocalDate.of(1990, 1, 1), "3001234567", "juan@test.com", "Sura", "MASCULINO");
        Pagina<PacienteDTO> paginaEsperada = new Pagina<>(List.of(pacienteDTO), 1L, 1, 0, 10);
        when(daoPaciente.buscarPorCriterios(criterios)).thenReturn(paginaEsperada);

        // Act
        Pagina<PacienteDTO> resultado = manejadorBuscarPacientesPorCriterios.ejecutar(criterios);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getTotalElementos());
        assertEquals(1, resultado.getContenido().size());
        assertEquals("Juan", resultado.getContenido().get(0).getNombre());
        verify(daoPaciente, times(1)).buscarPorCriterios(criterios);
    }

    @Test
    void deberia_RetornarPaginaVacia_Cuando_NingunCriterioCoincide() {
        // Arrange
        CriteriosBusquedaPaciente criterios = new CriteriosBusquedaPaciente(null, null, null, null, 0, 10, null);
        Pagina<PacienteDTO> paginaVacia = new Pagina<>(Collections.emptyList(), 0L, 0, 0, 10);
        when(daoPaciente.buscarPorCriterios(criterios)).thenReturn(paginaVacia);

        // Act
        Pagina<PacienteDTO> resultado = manejadorBuscarPacientesPorCriterios.ejecutar(criterios);

        // Assert
        assertNotNull(resultado);
        assertEquals(0L, resultado.getTotalElementos());
        assertTrue(resultado.getContenido().isEmpty());
        verify(daoPaciente, times(1)).buscarPorCriterios(criterios);
    }
}
