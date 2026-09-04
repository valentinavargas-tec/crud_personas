package ceiba.com.co.doctor.comando;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ComandoActualizarDoctorTest {

    @Test
    void deberiaAsignarYObtenerValores() {
        // Arrange
        ComandoActualizarDoctor comando = new ComandoActualizarDoctor();
        
        // Act
        comando.setNombre("Juan");
        comando.setApellido("Perez");
        comando.setEspecialidad("GENERAL");
        comando.setCorreoInstitucional("juan@hospital.com");

        // Assert
        assertEquals("Juan", comando.getNombre());
        assertEquals("Perez", comando.getApellido());
        assertEquals("GENERAL", comando.getEspecialidad());
        assertEquals("juan@hospital.com", comando.getCorreoInstitucional());
    }

    @Test
    void deberiaCrearConConstructorLleno() {
        // Act
        ComandoActualizarDoctor comando = new ComandoActualizarDoctor("Ana", "Torres", "CARDIOLOGIA", "ana@hospital.com");

        // Assert
        assertEquals("Ana", comando.getNombre());
        assertEquals("Torres", comando.getApellido());
        assertEquals("CARDIOLOGIA", comando.getEspecialidad());
        assertEquals("ana@hospital.com", comando.getCorreoInstitucional());
    }
}
