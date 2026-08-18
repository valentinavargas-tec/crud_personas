package ceiba.com.co.adaptador.dao;

import ceiba.com.co.infraestructura.jdbc.MapperResult;
import ceiba.com.co.modelo.dto.PersonaDTO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class MapeoPersonaDTO implements RowMapper<PersonaDTO>, MapperResult {

    @Override
    public PersonaDTO mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Long cedula = resultSet.getLong("cedula");
        String nombre = resultSet.getString("nombre");
        String apellido = resultSet.getString("apellido");
        String email = resultSet.getString("email");
        LocalDate fechaNacimiento = extraerLocalDate(resultSet, "fecha_nacimiento");

        return new PersonaDTO(cedula, nombre, apellido, email, fechaNacimiento);
    }
}
