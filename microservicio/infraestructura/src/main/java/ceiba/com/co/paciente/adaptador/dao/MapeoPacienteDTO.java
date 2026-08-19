package ceiba.com.co.paciente.adaptador.dao;

import ceiba.com.co.infraestructura.jdbc.MapperResult;
import ceiba.com.co.paciente.modelo.dto.PacienteDTO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class MapeoPacienteDTO implements RowMapper<PacienteDTO>, MapperResult {

    @Override
    public PacienteDTO mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Long numeroDocumento = resultSet.getLong("numero_documento");
        String tipoDocumento = resultSet.getString("tipo_documento");
        String nombre = resultSet.getString("nombre");
        String apellido = resultSet.getString("apellido");
        LocalDate fechaNacimiento = extraerLocalDate(resultSet, "fecha_nacimiento");
        String telefono = resultSet.getString("telefono");
        String correoElectronico = resultSet.getString("correo_electronico");
        String eps = resultSet.getString("eps");
        String genero = resultSet.getString("genero");

        return new PacienteDTO(numeroDocumento, tipoDocumento, nombre, apellido, fechaNacimiento, telefono, correoElectronico, eps, genero);
    }
}
