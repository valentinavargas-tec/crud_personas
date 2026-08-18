package ceiba.com.co.modelo.dto;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonaDTOTest {

    @Test
    void deberiaCrearPersonaDTOCorrectamente() {
        // Arrange
        LocalDate fecha = LocalDate.of(1990, 1, 1);

        // Act
        PersonaDTO personaDTO = new PersonaDTO(123L, "Juan", "Perez", "juan@test.com", fecha);

        // Assert
        assertEquals(123L, personaDTO.getCedula());
        assertEquals("Juan", personaDTO.getNombre());
        assertEquals("Perez", personaDTO.getApellido());
        assertEquals("juan@test.com", personaDTO.getEmail());
        assertEquals(fecha, personaDTO.getFechaNacimiento());
    }
}