package ceiba.com.co.paciente.infraestructura.adaptador.repositorio;

import ceiba.com.co.paciente.adaptador.repositorio.MapeoPaciente;
import ceiba.com.co.paciente.modelo.entidad.Paciente;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MapeoPacienteTest {

    @Mock
    private ResultSet resultSet;

    @Test
    void deberia_MapearResultSetAPacienteEntidad_Cuando_TodosLosCamposEstanPresentes() throws SQLException {
        // Arrange
        MapeoPaciente mapper = new MapeoPaciente();

        when(resultSet.getLong("numero_documento")).thenReturn(987654321L);
        when(resultSet.getString("tipo_documento")).thenReturn("CC");
        when(resultSet.getString("nombre")).thenReturn("Carlos");
        when(resultSet.getString("apellido")).thenReturn("Gomez");
        when(resultSet.getString("telefono")).thenReturn("3001234567");
        when(resultSet.getString("correo_electronico")).thenReturn("carlos@example.com");
        when(resultSet.getString("eps")).thenReturn("Sura");
        when(resultSet.getString("genero")).thenReturn("MASCULINO");
        java.sql.Timestamp ts = java.sql.Timestamp.valueOf(LocalDate.of(1980, 10, 15).atStartOfDay());
        when(resultSet.getTimestamp("fecha_nacimiento")).thenReturn(ts);
        when(resultSet.wasNull()).thenReturn(false);

        // Act
        Paciente paciente = mapper.mapRow(resultSet, 1);

        // Assert
        assertNotNull(paciente);
        assertEquals(987654321L, paciente.getNumeroDocumento());
        assertEquals("Carlos", paciente.getNombre());
        assertEquals("Gomez", paciente.getApellido());
        assertEquals("carlos@example.com", paciente.getCorreoElectronico());
        assertEquals(LocalDate.of(1980, 10, 15), paciente.getFechaNacimiento());
    }

    // Se elimina el test repetido para evitar conflictos
}