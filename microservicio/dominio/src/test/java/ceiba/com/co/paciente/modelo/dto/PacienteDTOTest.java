package ceiba.com.co.paciente.modelo.dto;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PacienteDTOTest {

    @Test
    void deberiaCrearPacienteDTOCorrectamente() {
        // Arrange
        LocalDate fecha = LocalDate.of(1990, 1, 1);

        // Act
        PacienteDTO pacienteDTO = new PacienteDTO(123L, "CC", "Juan", "Perez", fecha, "3001234567", "juan@test.com", "EPS Sura", "MASCULINO");

        // Assert
        assertEquals(123L, pacienteDTO.getNumeroDocumento());
        assertEquals("CC", pacienteDTO.getTipoDocumento());
        assertEquals("Juan", pacienteDTO.getNombre());
        assertEquals("Perez", pacienteDTO.getApellido());
        assertEquals(fecha, pacienteDTO.getFechaNacimiento());
        assertEquals("3001234567", pacienteDTO.getTelefono());
        assertEquals("juan@test.com", pacienteDTO.getCorreoElectronico());
        assertEquals("EPS Sura", pacienteDTO.getEps());
        assertEquals("MASCULINO", pacienteDTO.getGenero());
    }
}