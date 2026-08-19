package ceiba.com.co.paciente.consulta;

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

class ManejadorListarPacientesTest {

    private DaoPaciente daoPaciente;
    private ManejadorListarPacientes manejadorListarPacientes;

    @BeforeEach
    void setUp() {
        daoPaciente = mock(DaoPaciente.class);
        manejadorListarPacientes = new ManejadorListarPacientes(daoPaciente);
    }

    @Test
    @DisplayName("Debería listar los pacientes invocando al DaoPaciente")
    void deberia_RetornarListaPacientes_Cuando_ExistenPacientes() {
        // Arrange
        PacienteDTO paciente1 = new PacienteDTO(123L, "CC", "Juan", "Perez",
                LocalDate.of(1990, 1, 1), "3001234567", "juan@test.com", "Sura", "MASCULINO");
        PacienteDTO paciente2 = new PacienteDTO(456L, "CC", "Maria", "Lopez",
                LocalDate.of(1985, 6, 15), "3007654321", "maria@test.com", "EPS Bolivar", "FEMENINO");
        when(daoPaciente.listar()).thenReturn(List.of(paciente1, paciente2));

        // Act
        List<PacienteDTO> resultado = manejadorListarPacientes.ejecutar();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Juan", resultado.get(0).getNombre());
        assertEquals("Maria", resultado.get(1).getNombre());
        verify(daoPaciente, times(1)).listar();
    }

    @Test
    void deberia_RetornarListaVacia_Cuando_NoExistenPacientes() {
        // Arrange
        when(daoPaciente.listar()).thenReturn(Collections.emptyList());

        // Act
        List<PacienteDTO> resultado = manejadorListarPacientes.ejecutar();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(daoPaciente, times(1)).listar();
    }
}
