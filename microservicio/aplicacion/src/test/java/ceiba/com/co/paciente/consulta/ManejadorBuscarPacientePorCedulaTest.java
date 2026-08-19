package ceiba.com.co.paciente.consulta;

import ceiba.com.co.excepcion.ExcepcionSinDatos;
import ceiba.com.co.paciente.modelo.dto.PacienteDTO;
import ceiba.com.co.paciente.puerto.dao.DaoPaciente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ManejadorBuscarPacientePorNumeroDocumentoTest {

    private DaoPaciente daoPaciente;
    private ManejadorBuscarPacientePorNumeroDocumento manejadorBuscarPacientePorNumeroDocumento;

    @BeforeEach
    void setUp() {
        daoPaciente = mock(DaoPaciente.class);
        manejadorBuscarPacientePorNumeroDocumento = new ManejadorBuscarPacientePorNumeroDocumento(daoPaciente);
    }

    @Test
    void deberia_RetornarPacienteDTO_Cuando_NumeroDocumentoExiste() {
        // Arrange
        Long numeroDocumento = 12345L;
        PacienteDTO pacienteEsperado = new PacienteDTO(12345L, "CC", "Juan", "Perez",
                LocalDate.of(1990, 1, 1), "3001234567", "juan@test.com", "Sura", "MASCULINO");
        when(daoPaciente.buscarPorNumeroDocumento(numeroDocumento)).thenReturn(pacienteEsperado);

        // Act
        PacienteDTO resultado = manejadorBuscarPacientePorNumeroDocumento.ejecutar(numeroDocumento);

        // Assert
        assertNotNull(resultado);
        assertEquals(12345L, resultado.getNumeroDocumento());
        assertEquals("Juan", resultado.getNombre());
        assertEquals("Perez", resultado.getApellido());
        verify(daoPaciente, times(1)).buscarPorNumeroDocumento(numeroDocumento);
    }

    @Test
    void deberia_LanzarExcepcionSinDatos_Cuando_PacienteNoEsEncontrado() {
        // Arrange
        Long numeroDocumento = 99999L;
        when(daoPaciente.buscarPorNumeroDocumento(numeroDocumento)).thenReturn(null);

        // Act & Assert
        ExcepcionSinDatos excepcion = assertThrows(
                ExcepcionSinDatos.class,
                () -> manejadorBuscarPacientePorNumeroDocumento.ejecutar(numeroDocumento)
        );

        assertEquals("Paciente no encontrado", excepcion.getMessage());
        verify(daoPaciente, times(1)).buscarPorNumeroDocumento(numeroDocumento);
    }
}
