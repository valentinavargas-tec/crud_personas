package ceiba.com.co.adaptador.repositorio;

import ceiba.com.co.infraestructura.jdbc.MapperResult;
import ceiba.com.co.modelo.entidad.Persona;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class MapeoPersona implements RowMapper<Persona>, MapperResult {

    @Override
    public Persona mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Long cedula = resultSet.getLong("cedula");
        String nombre = resultSet.getString("nombre");
        String apellido = resultSet.getString("apellido");
        String email = resultSet.getString("email");
        LocalDate fechaNacimiento = extraerLocalDate(resultSet, "fecha_nacimiento");

        return new Persona(cedula, nombre, apellido, email, fechaNacimiento);
    }
}
