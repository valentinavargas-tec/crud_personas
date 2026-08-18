package ceiba.com.co.comando.fabrica;

import ceiba.com.co.comando.ComandoPersona;
import ceiba.com.co.comando.ComandoPersonaTestDataBuilder;
import ceiba.com.co.modelo.entidad.Persona;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FabricaPersonaTest {

    private FabricaPersona fabricaPersona;

    @BeforeEach
    void setUp() {
        fabricaPersona = new FabricaPersona();
    }

    @Test
    void deberia_CrearPersona_Cuando_ComandoEsValido() {
        // Arrange
        ComandoPersona comando = ComandoPersonaTestDataBuilder.unComandoPersonaValido()
                .conCedula(123456789L)
                .conNombre("Maria")
                .conApellido("Lopez")
                .conEmail("maria@gmail.com")
                .conFechaNacimiento(LocalDate.of(1992, 3, 10))
                .build();

        // Act
        Persona persona = fabricaPersona.crear(comando);

        // Assert
        assertNotNull(persona);
        assertEquals(123456789L, persona.getCedula());
        assertEquals("Maria", persona.getNombre());
        assertEquals("Lopez", persona.getApellido());
        assertEquals("maria@gmail.com", persona.getEmail());
        assertEquals(LocalDate.of(1992, 3, 10), persona.getFechaNacimiento());
    }

    @Test
    void deberia_CrearPersona_Cuando_FechaNacimientoEsNula() {
        // Arrange
        ComandoPersona comando = ComandoPersonaTestDataBuilder.unComandoPersonaValido()
                .conCedula(987654321L)
                .conNombre("Carlos")
                .conApellido("Gomez")
                .conEmail("carlos@example.com")
                .conFechaNacimiento(null)
                .build();

        // Act
        Persona persona = fabricaPersona.crear(comando);

        // Assert
        assertNotNull(persona);
        assertEquals(987654321L, persona.getCedula());
        assertNull(persona.getFechaNacimiento());
        assertNull(persona.obtenerEdadActual());
    }
}
