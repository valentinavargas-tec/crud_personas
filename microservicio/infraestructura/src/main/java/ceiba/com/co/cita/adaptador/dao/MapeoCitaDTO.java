package ceiba.com.co.cita.adaptador.dao;

import ceiba.com.co.cita.consulta.DtoCita;
import ceiba.com.co.infraestructura.jdbc.MapperResult;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Component
public class MapeoCitaDTO implements RowMapper<DtoCita>, MapperResult {

    @Override
    public DtoCita mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Long id = resultSet.getLong("id");
        String pacienteDocumento = resultSet.getString("pacienteDocumento");
        String doctorDocumento = resultSet.getString("doctorDocumento");
        LocalDateTime fechaHora = extraerLocalDateTime(resultSet, "fechaHora");
        String tipoCita = resultSet.getString("tipoCita");
        String estado = resultSet.getString("estado");
        String motivo = resultSet.getString("motivo");

        return new DtoCita(id, pacienteDocumento, doctorDocumento, fechaHora, tipoCita, estado, motivo);
    }
}
