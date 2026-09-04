package ceiba.com.co.cita.adaptador.repositorio;

import ceiba.com.co.cita.modelo.entidad.Cita;
import ceiba.com.co.cita.puerto.repositorio.RepositorioCita;
import ceiba.com.co.infraestructura.jdbc.CustomNamedParameterJdbcTemplate;
import ceiba.com.co.infraestructura.jdbc.sqlstatement.SqlStatement;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

@Repository
public class RepositorioCitaPostgres implements RepositorioCita {

    private final CustomNamedParameterJdbcTemplate customNamedParameterJdbcTemplate;

    @SqlStatement(namespace = "cita", value = "crear")
    private static String sqlCrear;

    @SqlStatement(namespace = "cita", value = "existecitaenhorariodoctor")
    private static String sqlExisteCitaEnHorarioDoctor;

    @SqlStatement(namespace = "cita", value = "existecitaenhorariopaciente")
    private static String sqlExisteCitaEnHorarioPaciente;

    public RepositorioCitaPostgres(CustomNamedParameterJdbcTemplate customNamedParameterJdbcTemplate) {
        this.customNamedParameterJdbcTemplate = customNamedParameterJdbcTemplate;
    }

    @Override
    public Long guardar(Cita cita) {
        MapSqlParameterSource paramSource = construirParametros(cita);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate()
                .update(sqlCrear, paramSource, keyHolder);
        return extraerIdGenerado(keyHolder);
    }

    @Override
    public boolean existeCitaEnHorarioDoctor(String doctorDocumento, LocalDateTime fechaHora) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("doctorDocumento", doctorDocumento);
        paramSource.addValue("fechaHora", Timestamp.valueOf(fechaHora));
        Integer count = this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate()
                .queryForObject(sqlExisteCitaEnHorarioDoctor, paramSource, Integer.class);
        return count != null && count > 0;
    }

    @Override
    public boolean existeCitaEnHorarioPaciente(String pacienteDocumento, LocalDateTime fechaHora) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("pacienteDocumento", Long.parseLong(pacienteDocumento));
        paramSource.addValue("fechaHora", Timestamp.valueOf(fechaHora));
        Integer count = this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate()
                .queryForObject(sqlExisteCitaEnHorarioPaciente, paramSource, Integer.class);
        return count != null && count > 0;
    }

    private MapSqlParameterSource construirParametros(Cita cita) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("pacienteDocumento", Long.parseLong(cita.getPacienteDocumento()));
        paramSource.addValue("doctorDocumento", cita.getDoctorDocumento());
        paramSource.addValue("fechaHora", Timestamp.valueOf(cita.getFechaHora()));
        paramSource.addValue("tipoCita", cita.getTipoCita().name());
        paramSource.addValue("estado", cita.getEstado().name());
        paramSource.addValue("motivo", cita.getMotivo());
        paramSource.addValue("observaciones", cita.getObservaciones());
        return paramSource;
    }

    private Long extraerIdGenerado(KeyHolder keyHolder) {
        Map<String, Object> keys = keyHolder.getKeys();
        if (keys != null && keys.containsKey("id")) {
            return ((Number) Objects.requireNonNull(keys.get("id"))).longValue();
        }
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }
}
