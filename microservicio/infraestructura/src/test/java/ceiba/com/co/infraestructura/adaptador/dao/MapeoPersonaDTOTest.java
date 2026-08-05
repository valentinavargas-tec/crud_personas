package ceiba.com.co.infraestructura.adaptador.dao;

import ceiba.com.co.adaptador.dao.MapeoPersonaDTO;
import ceiba.com.co.modelo.dto.PersonaDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MapeoPersonaDTOTest {

    @Mock
    private ResultSet resultSet;

    @Test
    void deberia_MapearResultSetAPersonaDTO_Cuando_TodosLosCamposEstanPresentes() throws SQLException {
        // Arrange
        MapeoPersonaDTO mapper = new MapeoPersonaDTO();
        LocalDate fecha = LocalDate.of(1995, 8, 20);
        Timestamp timestamp = Timestamp.valueOf(fecha.atStartOfDay());

        when(resultSet.getLong("cedula")).thenReturn(123456789L);
        when(resultSet.getString("nombre")).thenReturn("Maria");
        when(resultSet.getString("apellido")).thenReturn("Lopez");
        when(resultSet.getString("email")).thenReturn("maria@example.com");
        when(resultSet.getTimestamp("fecha_nacimiento")).thenReturn(timestamp);
        when(resultSet.wasNull()).thenReturn(false);

        // Act
        PersonaDTO dto = mapper.mapRow(resultSet, 1);

        // Assert
        assertNotNull(dto);
        assertEquals(123456789L, dto.getCedula());
        assertEquals("Maria", dto.getNombre());
        assertEquals("Lopez", dto.getApellido());
        assertEquals("maria@example.com", dto.getEmail());
        assertEquals(fecha, dto.getFechaNacimiento());
    }

    @Test
    void deberia_MapearResultSetAPersonaDTO_Cuando_FechaNacimientoEsNula() throws SQLException {
        // Arrange
        MapeoPersonaDTO mapper = new MapeoPersonaDTO();

        when(resultSet.getLong("cedula")).thenReturn(111L);
        when(resultSet.getString("nombre")).thenReturn("Carlos");
        when(resultSet.getString("apellido")).thenReturn("Gomez");
        when(resultSet.getString("email")).thenReturn("carlos@test.com");
        when(resultSet.getTimestamp("fecha_nacimiento")).thenReturn(null);
        when(resultSet.wasNull()).thenReturn(true);

        // Act
        PersonaDTO dto = mapper.mapRow(resultSet, 1);

        // Assert
        assertNotNull(dto);
        assertEquals(111L, dto.getCedula());
        assertNull(dto.getFechaNacimiento());
    }
}