package ceiba.com.co.infraestructura.adaptador.repositorio;

import ceiba.com.co.adaptador.repositorio.MapeoPersona;
import ceiba.com.co.modelo.entidad.Persona;
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
class MapeoPersonaTest {

    @Mock
    private ResultSet resultSet;

    @Test
    void deberia_MapearResultSetAPersonaEntidad_Cuando_TodosLosCamposEstanPresentes() throws SQLException {
        // Arrange
        MapeoPersona mapper = new MapeoPersona();
        LocalDate fecha = LocalDate.of(1992, 3, 10);
        Timestamp timestamp = Timestamp.valueOf(fecha.atStartOfDay());

        when(resultSet.getLong("cedula")).thenReturn(987654321L);
        when(resultSet.getString("nombre")).thenReturn("Carlos");
        when(resultSet.getString("apellido")).thenReturn("Gomez");
        when(resultSet.getString("email")).thenReturn("carlos@example.com");
        when(resultSet.getTimestamp("fecha_nacimiento")).thenReturn(timestamp);
        when(resultSet.wasNull()).thenReturn(false);

        // Act
        Persona persona = mapper.mapRow(resultSet, 1);

        // Assert
        assertNotNull(persona);
        assertEquals(987654321L, persona.getCedula());
        assertEquals("Carlos", persona.getNombre());
        assertEquals("Gomez", persona.getApellido());
        assertEquals("carlos@example.com", persona.getEmail());
        assertEquals(fecha, persona.getFechaNacimiento());
    }

    @Test
    void deberia_MapearResultSetAPersonaEntidad_Cuando_FechaNacimientoEsNula() throws SQLException {
        // Arrange
        MapeoPersona mapper = new MapeoPersona();

        when(resultSet.getLong("cedula")).thenReturn(111222333L);
        when(resultSet.getString("nombre")).thenReturn("Laura");
        when(resultSet.getString("apellido")).thenReturn("Torres");
        when(resultSet.getString("email")).thenReturn("laura@example.com");
        when(resultSet.getTimestamp("fecha_nacimiento")).thenReturn(null);
        when(resultSet.wasNull()).thenReturn(true);

        // Act
        Persona persona = mapper.mapRow(resultSet, 1);

        // Assert
        assertNotNull(persona);
        assertEquals(111222333L, persona.getCedula());
        assertNull(persona.getFechaNacimiento());
    }
}