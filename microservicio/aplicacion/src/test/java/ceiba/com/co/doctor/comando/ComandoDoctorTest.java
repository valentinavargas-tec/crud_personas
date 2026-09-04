package ceiba.com.co.doctor.comando;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ComandoDoctorTest {

    @Test
    void deberiaAsignarYObtenerValores() {
        // Arrange
        ComandoDoctor comando = new ComandoDoctor();
        
        // Act
        comando.setNumeroDocumento("123");
        comando.setNombre("Juan");
        comando.setApellido("Perez");
        comando.setTarjetaProfesional("TP-123");
        comando.setEspecialidad("GENERAL");
        comando.setCorreoInstitucional("juan@hospital.com");

        // Assert
        assertEquals("123", comando.getNumeroDocumento());
        assertEquals("Juan", comando.getNombre());
        assertEquals("Perez", comando.getApellido());
        assertEquals("TP-123", comando.getTarjetaProfesional());
        assertEquals("GENERAL", comando.getEspecialidad());
        assertEquals("juan@hospital.com", comando.getCorreoInstitucional());
    }

    @Test
    void deberiaCrearConConstructorLleno() {
        // Act
        ComandoDoctor comando = new ComandoDoctor("123", "Ana", "Torres", "TP-456", "CARDIOLOGIA", "ana@hospital.com");

        // Assert
        assertEquals("123", comando.getNumeroDocumento());
        assertEquals("Ana", comando.getNombre());
        assertEquals("Torres", comando.getApellido());
        assertEquals("TP-456", comando.getTarjetaProfesional());
        assertEquals("CARDIOLOGIA", comando.getEspecialidad());
        assertEquals("ana@hospital.com", comando.getCorreoInstitucional());
    }
}
