package ceiba.com.co.cita.adaptador.dao;

import ceiba.com.co.cita.consulta.DtoCita;
import ceiba.com.co.cita.puerto.dao.DaoCita;
import ceiba.com.co.infraestructura.jdbc.CustomNamedParameterJdbcTemplate;
import ceiba.com.co.infraestructura.jdbc.sqlstatement.SqlStatement;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class DaoCitaPostgres implements DaoCita {

    private final CustomNamedParameterJdbcTemplate customNamedParameterJdbcTemplate;
    private final MapeoCitaDTO mapeoCitaDTO;

    @SqlStatement(namespace = "cita", value = "buscarporid")
    private static String sqlBuscarPorId;

    @SqlStatement(namespace = "cita", value = "buscarporpaciente")
    private static String sqlBuscarPorPaciente;

    @SqlStatement(namespace = "cita", value = "buscarpordoctor")
    private static String sqlBuscarPorDoctor;

    @SqlStatement(namespace = "cita", value = "buscarpordoctoryfecha")
    private static String sqlBuscarPorDoctorYFecha;

    public DaoCitaPostgres(CustomNamedParameterJdbcTemplate customNamedParameterJdbcTemplate, MapeoCitaDTO mapeoCitaDTO) {
        this.customNamedParameterJdbcTemplate = customNamedParameterJdbcTemplate;
        this.mapeoCitaDTO = mapeoCitaDTO;
    }

    @Override
    public Optional<DtoCita> buscarPorId(Long idCita) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("idCita", idCita);
        List<DtoCita> result = this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate()
                .query(sqlBuscarPorId, paramSource, this.mapeoCitaDTO);
        return result.stream().findFirst();
    }

    @Override
    public List<DtoCita> buscarCitasPorPaciente(String pacienteDocumento) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("pacienteDocumento", Long.parseLong(pacienteDocumento));
        return this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate()
                .query(sqlBuscarPorPaciente, paramSource, this.mapeoCitaDTO);
    }

    @Override
    public List<DtoCita> buscarCitasPorDoctor(String doctorDocumento) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("doctorDocumento", doctorDocumento);
        return this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate()
                .query(sqlBuscarPorDoctor, paramSource, this.mapeoCitaDTO);
    }

    @Override
    public List<DtoCita> buscarCitasDoctorPorFecha(String doctorDocumento, LocalDate fecha) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("doctorDocumento", doctorDocumento);
        paramSource.addValue("fecha", fecha);
        return this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate()
                .query(sqlBuscarPorDoctorYFecha, paramSource, this.mapeoCitaDTO);
    }
}
