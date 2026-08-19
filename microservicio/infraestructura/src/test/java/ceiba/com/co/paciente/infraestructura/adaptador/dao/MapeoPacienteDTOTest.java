package ceiba.com.co.infraestructura.adaptador.dao;

import ceiba.com.co.paciente.adaptador.dao.MapeoPacienteDTO;
import ceiba.com.co.paciente.modelo.dto.PacienteDTO;
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
public class MapeoPacienteDTOTest {

    @Mock
    private ResultSet resultSet;

    @Test
    void deberia_MapearResultSetAPacienteDTO_Cuando_TodosLosCamposEstanPresentes() throws SQLException {
        // Arrange
        MapeoPacienteDTO mapper = new MapeoPacienteDTO();

        when(resultSet.getLong("numero_documento")).thenReturn(123456789L);
        when(resultSet.getString("tipo_documento")).thenReturn("CC");
        when(resultSet.getString("nombre")).thenReturn("Maria");
        when(resultSet.getString("apellido")).thenReturn("Lopez");
        when(resultSet.getString("telefono")).thenReturn("3001234567");
        when(resultSet.getString("correo_electronico")).thenReturn("maria@example.com");
        when(resultSet.getString("eps")).thenReturn("Sura");
        when(resultSet.getString("genero")).thenReturn("FEMENINO");
        java.sql.Timestamp ts = java.sql.Timestamp.valueOf(LocalDate.of(1995, 5, 20).atStartOfDay());
        when(resultSet.getTimestamp("fecha_nacimiento")).thenReturn(ts);
        when(resultSet.wasNull()).thenReturn(false);

        // Act
        PacienteDTO dto = mapper.mapRow(resultSet, 1);

        // Assert
        assertNotNull(dto);
        assertEquals(123456789L, dto.getNumeroDocumento());
        assertEquals("Maria", dto.getNombre());
        assertEquals("Lopez", dto.getApellido());
        assertEquals("maria@example.com", dto.getCorreoElectronico());
        assertEquals(LocalDate.of(1995, 5, 20), dto.getFechaNacimiento());
    }

    // Se elimina el test repetido para evitar conflictos de Mockito
}