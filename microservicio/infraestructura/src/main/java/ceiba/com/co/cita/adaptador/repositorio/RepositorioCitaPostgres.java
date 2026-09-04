package ceiba.com.co.cita.adaptador.repositorio;

import ceiba.com.co.cita.modelo.entidad.Cita;
import ceiba.com.co.cita.modelo.entidad.EstadoCita;
import ceiba.com.co.cita.modelo.entidad.TipoCita;
import ceiba.com.co.cita.puerto.repositorio.RepositorioCita;
import ceiba.com.co.infraestructura.jdbc.CustomNamedParameterJdbcTemplate;
import ceiba.com.co.infraestructura.jdbc.sqlstatement.SqlStatement;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
public class RepositorioCitaPostgres implements RepositorioCita {

    private final CustomNamedParameterJdbcTemplate customNamedParameterJdbcTemplate;

    @SqlStatement(namespace = "cita", value = "crear")
    private static String sqlCrear;

    @SqlStatement(namespace = "cita", value = "actualizar")
    private static String sqlActualizar;

    @SqlStatement(namespace = "cita", value = "obtenerporid")
    private static String sqlObtenerPorId;

    @SqlStatement(namespace = "cita", value = "existecitaenhorariodoctor")
    private static String sqlExisteCitaEnHorarioDoctor;

    @SqlStatement(namespace = "cita", value = "existecitaenhorariopaciente")
    private static String sqlExisteCitaEnHorarioPaciente;

    @SqlStatement(namespace = "cita", value = "existecitaenhorariodoctorexcluyendocita")
    private static String sqlExisteCitaEnHorarioDoctorExcluyendoCita;

    @SqlStatement(namespace = "cita", value = "existecitaenhorariopacienteexcluyendocita")
    private static String sqlExisteCitaEnHorarioPacienteExcluyendoCita;

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
    public Optional<Cita> obtenerPorId(Long id) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("id", id);
        List<Cita> result = this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate()
                .query(sqlObtenerPorId, paramSource, (rs, rowNum) -> Cita.builder()
                        .conId(rs.getLong("id"))
                        .conPacienteDocumento(String.valueOf(rs.getLong("paciente_documento")))
                        .conDoctorDocumento(rs.getString("doctor_documento"))
                        .conFechaHora(rs.getTimestamp("fecha_hora").toLocalDateTime())
                        .conTipoCita(TipoCita.valueOf(rs.getString("tipo_cita")))
                        .conEstado(EstadoCita.valueOf(rs.getString("estado")))
                        .conMotivo(rs.getString("motivo"))
                        .conObservaciones(rs.getString("observaciones"))
                        .build()
                );
        return result.stream().findFirst();
    }

    @Override
    public void actualizar(Cita cita) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("id", cita.getId());
        paramSource.addValue("doctorDocumento", cita.getDoctorDocumento());
        paramSource.addValue("fechaHora", Timestamp.valueOf(cita.getFechaHora()));
        paramSource.addValue("tipoCita", cita.getTipoCita().name());
        paramSource.addValue("estado", cita.getEstado().name());
        paramSource.addValue("motivo", cita.getMotivo());
        paramSource.addValue("observaciones", cita.getObservaciones());
        this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate()
                .update(sqlActualizar, paramSource);
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

    @Override
    public boolean existeCitaEnHorarioDoctorExcluyendoCita(Long idCita, String doctorDocumento, LocalDateTime fechaHora) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("idCita", idCita);
        paramSource.addValue("doctorDocumento", doctorDocumento);
        paramSource.addValue("fechaHora", Timestamp.valueOf(fechaHora));
        Integer count = this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate()
                .queryForObject(sqlExisteCitaEnHorarioDoctorExcluyendoCita, paramSource, Integer.class);
        return count != null && count > 0;
    }

    @Override
    public boolean existeCitaEnHorarioPacienteExcluyendoCita(Long idCita, String pacienteDocumento, LocalDateTime fechaHora) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("idCita", idCita);
        paramSource.addValue("pacienteDocumento", Long.parseLong(pacienteDocumento));
        paramSource.addValue("fechaHora", Timestamp.valueOf(fechaHora));
        Integer count = this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate()
                .queryForObject(sqlExisteCitaEnHorarioPacienteExcluyendoCita, paramSource, Integer.class);
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
