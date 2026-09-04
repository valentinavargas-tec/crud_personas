package ceiba.com.co.doctor.adaptador.dao;

import ceiba.com.co.doctor.consulta.DtoDoctor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MapeoDoctorDTO implements RowMapper<DtoDoctor> {

    @Override
    public DtoDoctor mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new DtoDoctor(
                rs.getString("numero_documento"),
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getString("tarjeta_profesional"),
                rs.getString("especialidad"),
                rs.getString("correo_institucional"),
                rs.getBoolean("habilitado")
        );
    }
}
