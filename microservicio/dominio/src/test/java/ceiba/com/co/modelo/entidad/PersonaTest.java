package ceiba.com.co.modelo.entidad;

import ceiba.com.co.excepcion.ExcepcionValorInvalido;
import ceiba.com.co.excepcion.ExcepcionValorObligatorio;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.Period;

import static org.junit.jupiter.api.Assertions.*;

class PersonaTest {

    @Test
    void deberiaCrearPersona_Cuando_LosDatosSonValidos() {
        // Arrange & Act
        Persona persona = PersonaTestDataBuilder.unPersonaValida().build();

        // Assert
        assertNotNull(persona);
        assertEquals(123456789L, persona.getCedula());
        assertEquals("Juan", persona.getNombre());
        assertEquals("Perez", persona.getApellido());
        assertEquals("juan.perez@example.com", persona.getEmail());
        assertNotNull(persona.getFechaNacimiento());
    }

    @Test
    void deberiaLanzarExcepcion_Cuando_CedulaEsNula() {
        // Arrange, Act & Assert
        assertThrows(ExcepcionValorObligatorio.class, () ->
                PersonaTestDataBuilder.unPersonaValida().cedula(null).build()
        );
    }

    @Test
    void deberiaLanzarExcepcion_Cuando_CedulaEsNegativa() {
        // Arrange, Act & Assert
        assertThrows(ExcepcionValorInvalido.class, () ->
                PersonaTestDataBuilder.unPersonaValida().cedula(-100L).build()
        );
    }

    @Test
    void deberiaLanzarExcepcion_Cuando_CedulaEsCero() {
        // Arrange, Act & Assert
        assertThrows(ExcepcionValorInvalido.class, () ->
                PersonaTestDataBuilder.unPersonaValida().cedula(0L).build()
        );
    }

    @Test
    void deberiaLanzarExcepcion_Cuando_NombreEsNulo() {
        // Arrange, Act & Assert
        assertThrows(ExcepcionValorObligatorio.class, () ->
                PersonaTestDataBuilder.unPersonaValida().nombre(null).build()
        );
    }

    @Test
    void deberiaLanzarExcepcion_Cuando_NombreEsInvalido() {
        // Arrange, Act & Assert
        assertThrows(ExcepcionValorInvalido.class, () ->
                PersonaTestDataBuilder.unPersonaValida().nombre("Juan123").build()
        );
    }

    @Test
    void deberiaLanzarExcepcion_Cuando_ApellidoEsNulo() {
        // Arrange, Act & Assert
        assertThrows(ExcepcionValorObligatorio.class, () ->
                PersonaTestDataBuilder.unPersonaValida().apellido(null).build()
        );
    }

    @Test
    void deberiaLanzarExcepcion_Cuando_ApellidoEsInvalido() {
        // Arrange, Act & Assert
        assertThrows(ExcepcionValorInvalido.class, () ->
                PersonaTestDataBuilder.unPersonaValida().apellido("Perez123").build()
        );
    }

    @Test
    void deberiaLanzarExcepcion_Cuando_EmailEsNulo() {
        // Arrange, Act & Assert
        assertThrows(ExcepcionValorObligatorio.class, () ->
                PersonaTestDataBuilder.unPersonaValida().email(null).build()
        );
    }

    @Test
    void deberiaLanzarExcepcion_Cuando_EmailEsInvalido() {
        // Arrange, Act & Assert
        assertThrows(ExcepcionValorInvalido.class, () ->
                PersonaTestDataBuilder.unPersonaValida().email("correo_invalido").build()
        );
    }

    @Test
    void deberiaLanzarExcepcion_Cuando_FechaNacimientoEsFutura() {
        // Arrange
        LocalDate fechaFutura = LocalDate.now().plusDays(1);

        // Act & Assert
        assertThrows(ExcepcionValorInvalido.class, () ->
                PersonaTestDataBuilder.unPersonaValida().fechaNacimiento(fechaFutura).build()
        );
    }

    @Test
    void deberiaPermitirFechaNacimientoNula() {
        // Arrange & Act
        Persona persona = PersonaTestDataBuilder.unPersonaValida().fechaNacimiento(null).build();

        // Assert
        assertNull(persona.getFechaNacimiento());
    }

    @Test
    void deberiaCalcularEdadActual_Cuando_FechaNacimientoEsValida() {
        // Arrange
        LocalDate fechaNacimiento = LocalDate.of(2000, 5, 15);
        Persona persona = PersonaTestDataBuilder.unPersonaValida().fechaNacimiento(fechaNacimiento).build();

        // Act
        Integer edadCalculada = persona.obtenerEdadActual();

        // Assert
        int edadEsperada = Period.between(fechaNacimiento, LocalDate.now()).getYears();
        assertEquals(edadEsperada, edadCalculada);
    }

    @Test
    void deberiaRetornarNullEdad_Cuando_FechaNacimientoEsNula() {
        // Arrange
        Persona persona = PersonaTestDataBuilder.unPersonaValida().fechaNacimiento(null).build();

        // Act & Assert
        assertNull(persona.obtenerEdadActual());
    }

    @Test
    void deberiaActualizarDatos_Cuando_NuevaInformacionEsValida() {
        // Arrange
        Persona persona = PersonaTestDataBuilder.unPersonaValida().build();

        // Act
        Persona personaActualizada = persona.actualizarDatos("Carlos", "Gomez", "carlos@example.com", persona.getFechaNacimiento());

        // Assert
        assertEquals("Carlos", personaActualizada.getNombre());
        assertEquals("Gomez", personaActualizada.getApellido());
        assertEquals("carlos@example.com", personaActualizada.getEmail());
        assertEquals(persona.getCedula(), personaActualizada.getCedula());
    }

    @Test
    void deberiaLanzarExcepcion_Cuando_ParametrosActualizacionSonNulos() {
        // Arrange
        Persona persona = PersonaTestDataBuilder.unPersonaValida().build();

        // Act & Assert
        assertThrows(ExcepcionValorObligatorio.class, () ->
                persona.actualizarDatos(null, null, null, null)
        );
    }

    @Test
    void deberiaLimpiarEspaciosEnNombreYApellidoYEmail() {
        // Arrange & Act - Los valores con espacios al inicio/fin deben ser limpiados por limpiar()
        Persona persona = Persona.builder()
                .conCedula(123456789L)
                .conNombre(new Nombre("  Juan  ", "El nombre es obligatorio", "El nombre contiene caracteres no permitidos"))
                .conApellido(new Nombre("  Perez  ", "El apellido es obligatorio", "El apellido contiene caracteres no permitidos"))
                .conEmail(new Email("  juan@example.com  "))
                .conFechaNacimiento(LocalDate.of(1990, 1, 1))
                .build();

        // Assert - el constructor llama limpiar() sobre nombre, apellido y email
        assertEquals("Juan", persona.getNombre());
        assertEquals("Perez", persona.getApellido());
        assertEquals("juan@example.com", persona.getEmail());
    }
}
