package ceiba.com.co.adaptador.repositorio;

import ceiba.com.co.infraestructura.jdbc.MapperResult;
import ceiba.com.co.modelo.entidad.Email;
import ceiba.com.co.modelo.entidad.Nombre;
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

        return Persona.builder()
                .conCedula(cedula)
                .conNombre(new Nombre(nombre, "El nombre es obligatorio", "El nombre contiene caracteres no permitidos"))
                .conApellido(new Nombre(apellido, "El apellido es obligatorio", "El apellido contiene caracteres no permitidos"))
                .conEmail(new Email(email))
                .conFechaNacimiento(fechaNacimiento)
                .build();
    }
}
