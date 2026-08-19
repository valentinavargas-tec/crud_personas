package ceiba.com.co.paciente.adaptador.repositorio;

import ceiba.com.co.infraestructura.jdbc.MapperResult;
import ceiba.com.co.paciente.modelo.entidad.Paciente;
import ceiba.com.co.paciente.modelo.entidad.Email;
import ceiba.com.co.paciente.modelo.entidad.Nombre;
import ceiba.com.co.paciente.modelo.entidad.TipoDocumento;
import ceiba.com.co.paciente.modelo.entidad.Genero;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class MapeoPaciente implements RowMapper<Paciente>, MapperResult {

    @Override
    public Paciente mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Long numeroDocumento = resultSet.getLong("numero_documento");
        String tipoDocumentoStr = resultSet.getString("tipo_documento");
        String nombre = resultSet.getString("nombre");
        String apellido = resultSet.getString("apellido");
        LocalDate fechaNacimiento = extraerLocalDate(resultSet, "fecha_nacimiento");
        String telefono = resultSet.getString("telefono");
        String correoElectronico = resultSet.getString("correo_electronico");
        String eps = resultSet.getString("eps");
        String generoStr = resultSet.getString("genero");

        return Paciente.builder()
                .conNumeroDocumento(numeroDocumento)
                .conTipoDocumento(tipoDocumentoStr != null ? TipoDocumento.valueOf(tipoDocumentoStr) : null)
                .conNombre(new Nombre(nombre, "El nombre es obligatorio", "El nombre contiene caracteres no permitidos"))
                .conApellido(new Nombre(apellido, "El apellido es obligatorio", "El apellido contiene caracteres no permitidos"))
                .conFechaNacimiento(fechaNacimiento)
                .conTelefono(telefono)
                .conCorreoElectronico(new Email(correoElectronico))
                .conEps(eps)
                .conGenero(generoStr != null ? Genero.valueOf(generoStr) : null)
                .build();
    }
}
