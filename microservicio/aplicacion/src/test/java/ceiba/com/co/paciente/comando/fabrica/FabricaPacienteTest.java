package ceiba.com.co.paciente.comando.fabrica;

import ceiba.com.co.paciente.comando.ComandoPaciente;
import ceiba.com.co.paciente.comando.ComandoPacienteTestDataBuilder;
import ceiba.com.co.paciente.modelo.entidad.Paciente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FabricaPacienteTest {

    private FabricaPaciente fabricaPaciente;

    @BeforeEach
    void setUp() {
        fabricaPaciente = new FabricaPaciente();
    }

    @Test
    void deberia_CrearPaciente_Cuando_ComandoEsValido() {
        // Arrange
        ComandoPaciente comando = ComandoPacienteTestDataBuilder.unComandoPacienteValido()
                .conNumeroDocumento(123456789L)
                .conNombre("Maria")
                .conApellido("Lopez")
                .conCorreoElectronico("maria@gmail.com")
                .conFechaNacimiento(LocalDate.of(1992, 3, 10))
                .build();

        // Act
        Paciente paciente = fabricaPaciente.crear(comando);

        // Assert
        assertNotNull(paciente);
        assertEquals(123456789L, paciente.getNumeroDocumento());
        assertEquals("Maria", paciente.getNombre());
        assertEquals("Lopez", paciente.getApellido());
        assertEquals("maria@gmail.com", paciente.getCorreoElectronico());
        assertEquals(LocalDate.of(1992, 3, 10), paciente.getFechaNacimiento());
    }

    @Test
    void deberia_CrearPaciente_Cuando_FechaNacimientoEsNula() {
        // Arrange
        ComandoPaciente comando = ComandoPacienteTestDataBuilder.unComandoPacienteValido()
                .conNumeroDocumento(987654321L)
                .conNombre("Carlos")
                .conApellido("Gomez")
                .conCorreoElectronico("carlos@example.com")
                .conFechaNacimiento(null)
                .build();

        // Act
        Paciente paciente = fabricaPaciente.crear(comando);

        // Assert
        assertNotNull(paciente);
        assertEquals(987654321L, paciente.getNumeroDocumento());
        assertNull(paciente.getFechaNacimiento());
        assertNull(paciente.obtenerEdadActual());
    }
}
