package ceiba.com.co.paciente.modelo.entidad;

import ceiba.com.co.excepcion.ExcepcionValorInvalido;
import ceiba.com.co.excepcion.ExcepcionValorObligatorio;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.Period;

import static org.junit.jupiter.api.Assertions.*;

class PacienteTest {

    @Test
    void deberiaCrearPaciente_Cuando_LosDatosSonValidos() {
        // Arrange & Act
        Paciente paciente = PacienteTestDataBuilder.unPacienteValida().build();

        // Assert
        assertNotNull(paciente);
        assertEquals(123456789L, paciente.getNumeroDocumento());
        assertEquals("Juan", paciente.getNombre());
        assertEquals("Perez", paciente.getApellido());
        assertEquals("juan.perez@example.com", paciente.getCorreoElectronico());
        assertNotNull(paciente.getFechaNacimiento());
    }

    @Test
    void deberiaLanzarExcepcion_Cuando_NumeroDocumentoEsNulo() {
        // Arrange, Act & Assert
        assertThrows(ExcepcionValorObligatorio.class, () ->
                PacienteTestDataBuilder.unPacienteValida().numeroDocumento(null).build()
        );
    }

    @Test
    void deberiaLanzarExcepcion_Cuando_NumeroDocumentoEsNegativo() {
        // Arrange, Act & Assert
        assertThrows(ExcepcionValorInvalido.class, () ->
                PacienteTestDataBuilder.unPacienteValida().numeroDocumento(-100L).build()
        );
    }

    @Test
    void deberiaLanzarExcepcion_Cuando_NumeroDocumentoEsCero() {
        // Arrange, Act & Assert
        assertThrows(ExcepcionValorInvalido.class, () ->
                PacienteTestDataBuilder.unPacienteValida().numeroDocumento(0L).build()
        );
    }

    @Test
    void deberiaLanzarExcepcion_Cuando_NombreEsNulo() {
        // Arrange, Act & Assert
        assertThrows(ExcepcionValorObligatorio.class, () ->
                PacienteTestDataBuilder.unPacienteValida().nombre(null).build()
        );
    }

    @Test
    void deberiaLanzarExcepcion_Cuando_NombreEsInvalido() {
        // Arrange, Act & Assert
        assertThrows(ExcepcionValorInvalido.class, () ->
                PacienteTestDataBuilder.unPacienteValida().nombre("Juan123").build()
        );
    }

    @Test
    void deberiaLanzarExcepcion_Cuando_ApellidoEsNulo() {
        // Arrange, Act & Assert
        assertThrows(ExcepcionValorObligatorio.class, () ->
                PacienteTestDataBuilder.unPacienteValida().apellido(null).build()
        );
    }

    @Test
    void deberiaLanzarExcepcion_Cuando_ApellidoEsInvalido() {
        // Arrange, Act & Assert
        assertThrows(ExcepcionValorInvalido.class, () ->
                PacienteTestDataBuilder.unPacienteValida().apellido("Perez123").build()
        );
    }

    @Test
    void deberiaLanzarExcepcion_Cuando_CorreoElectronicoEsNulo() {
        // Arrange, Act & Assert
        assertThrows(ExcepcionValorObligatorio.class, () ->
                PacienteTestDataBuilder.unPacienteValida().correoElectronico(null).build()
        );
    }

    @Test
    void deberiaLanzarExcepcion_Cuando_CorreoElectronicoEsInvalido() {
        // Arrange, Act & Assert
        assertThrows(ExcepcionValorInvalido.class, () ->
                PacienteTestDataBuilder.unPacienteValida().correoElectronico("correo_invalido").build()
        );
    }

    @Test
    void deberiaLanzarExcepcion_Cuando_FechaNacimientoEsFutura() {
        // Arrange
        LocalDate fechaFutura = LocalDate.now().plusDays(1);

        // Act & Assert
        assertThrows(ExcepcionValorInvalido.class, () ->
                PacienteTestDataBuilder.unPacienteValida().fechaNacimiento(fechaFutura).build()
        );
    }

    @Test
    void deberiaPermitirFechaNacimientoNula() {
        // Arrange & Act
        Paciente paciente = PacienteTestDataBuilder.unPacienteValida().fechaNacimiento(null).build();

        // Assert
        assertNull(paciente.getFechaNacimiento());
    }

    @Test
    void deberiaCalcularEdadActual_Cuando_FechaNacimientoEsValida() {
        // Arrange
        LocalDate fechaNacimiento = LocalDate.of(2000, 5, 15);
        Paciente paciente = PacienteTestDataBuilder.unPacienteValida().fechaNacimiento(fechaNacimiento).build();

        // Act
        Integer edadCalculada = paciente.obtenerEdadActual();

        // Assert
        int edadEsperada = Period.between(fechaNacimiento, LocalDate.now()).getYears();
        assertEquals(edadEsperada, edadCalculada);
    }

    @Test
    void deberiaRetornarNullEdad_Cuando_FechaNacimientoEsNula() {
        // Arrange
        Paciente paciente = PacienteTestDataBuilder.unPacienteValida().fechaNacimiento(null).build();

        // Act & Assert
        assertNull(paciente.obtenerEdadActual());
    }

    @Test
    void deberiaActualizarDatos_Cuando_NuevaInformacionEsValida() {
        // Arrange
        Paciente paciente = PacienteTestDataBuilder.unPacienteValida().build();

        // Act
        Paciente pacienteActualizada = paciente.actualizarDatos("Carlos", "Gomez", paciente.getFechaNacimiento(), "3001234567", "carlos@example.com", "EPS Sura", Genero.MASCULINO);

        // Assert
        assertEquals("Carlos", pacienteActualizada.getNombre());
        assertEquals("Gomez", pacienteActualizada.getApellido());
        assertEquals("carlos@example.com", pacienteActualizada.getCorreoElectronico());
        assertEquals(paciente.getNumeroDocumento(), pacienteActualizada.getNumeroDocumento());
    }

    @Test
    void deberiaLanzarExcepcion_Cuando_ParametrosActualizacionSonNulos() {
        // Arrange
        Paciente paciente = PacienteTestDataBuilder.unPacienteValida().build();

        // Act & Assert
        assertThrows(ExcepcionValorObligatorio.class, () ->
                paciente.actualizarDatos(null, null, null, null, null, null, null)
        );
    }

    @Test
    void deberiaLimpiarEspaciosEnNombreYApellidoYEmail() {
        // Arrange & Act - Los valores con espacios al inicio/fin deben ser limpiados por limpiar()
        Paciente paciente = Paciente.builder()
                .conNumeroDocumento(123456789L)
                .conTipoDocumento(TipoDocumento.CC)
                .conNombre(new Nombre("  Juan  ", "El nombre es obligatorio", "El nombre contiene caracteres no permitidos"))
                .conApellido(new Nombre("  Perez  ", "El apellido es obligatorio", "El apellido contiene caracteres no permitidos"))
                .conFechaNacimiento(LocalDate.of(1990, 1, 1))
                .conTelefono("3001234567")
                .conCorreoElectronico(new Email("  juan@example.com  "))
                .conEps("EPS Sura")
                .conGenero(Genero.MASCULINO)
                .build();

        // Assert - el constructor llama limpiar() sobre nombre, apellido y email
        assertEquals("Juan", paciente.getNombre());
        assertEquals("Perez", paciente.getApellido());
        assertEquals("juan@example.com", paciente.getCorreoElectronico());
    }
}
