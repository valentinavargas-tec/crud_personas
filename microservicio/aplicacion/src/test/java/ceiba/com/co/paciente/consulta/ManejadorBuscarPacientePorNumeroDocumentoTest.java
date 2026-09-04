package ceiba.com.co.paciente.consulta;

import ceiba.com.co.excepcion.ExcepcionSinDatos;
import ceiba.com.co.paciente.modelo.dto.PacienteDTO;
import ceiba.com.co.paciente.puerto.dao.DaoPaciente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ManejadorBuscarPacientePorNumeroDocumentoTest {

    private DaoPaciente daoPaciente;
    private ManejadorBuscarPacientePorNumeroDocumento manejadorBuscarPaciente;

    @BeforeEach
    void setUp() {
        daoPaciente = Mockito.mock(DaoPaciente.class);
        manejadorBuscarPaciente = new ManejadorBuscarPacientePorNumeroDocumento(daoPaciente);
    }

    @Test
    @DisplayName("Debería retornar PacienteDTO cuando el paciente existe")
    void deberiaRetornarPacienteDTO_Cuando_PacienteExiste() {
        // Arrange
        Long documento = 123456789L;
        PacienteDTO pacienteDTO = new PacienteDTO(
                documento, "CC", "Juan", "Perez",
                LocalDate.of(1990, 1, 1), "3001234567",
                "juan@gmail.com", "Sura", "MASCULINO"
        );
        when(daoPaciente.buscarPorNumeroDocumento(documento)).thenReturn(pacienteDTO);

        // Act
        PacienteDTO resultado = manejadorBuscarPaciente.ejecutar(documento);

        // Assert
        assertNotNull(resultado);
        assertEquals(documento, resultado.getNumeroDocumento());
        assertEquals("Juan", resultado.getNombre());
    }

    @Test
    @DisplayName("Debería lanzar ExcepcionSinDatos cuando el paciente no existe")
    void deberiaLanzarExcepcionSinDatos_Cuando_PacienteNoExiste() {
        // Arrange
        Long documentoInexistente = 999999999L;
        when(daoPaciente.buscarPorNumeroDocumento(documentoInexistente)).thenReturn(null);

        // Act & Assert
        assertThrows(ExcepcionSinDatos.class,
                () -> manejadorBuscarPaciente.ejecutar(documentoInexistente));
    }
}
