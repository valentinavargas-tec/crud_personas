package ceiba.com.co.doctor.adaptador.dao;

import ceiba.com.co.doctor.consulta.DtoDoctor;
import ceiba.com.co.doctor.puerto.dao.DaoDoctor;
import ceiba.com.co.infraestructura.jdbc.CustomNamedParameterJdbcTemplate;
import ceiba.com.co.infraestructura.jdbc.sqlstatement.SqlStatement;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DaoDoctorPostgres implements DaoDoctor {

    private final CustomNamedParameterJdbcTemplate customNamedParameterJdbcTemplate;
    private final MapeoDoctorDTO mapeoDoctorDTO;

    @SqlStatement(namespace = "doctor", value = "listartodos")
    private static String sqlListarTodos;

    @SqlStatement(namespace = "doctor", value = "buscarporespecialidad")
    private static String sqlBuscarPorEspecialidad;

    @SqlStatement(namespace = "doctor", value = "obtenerpornumerodocumento")
    private static String sqlObtenerPorNumeroDocumento;

    public DaoDoctorPostgres(CustomNamedParameterJdbcTemplate customNamedParameterJdbcTemplate, MapeoDoctorDTO mapeoDoctorDTO) {
        this.customNamedParameterJdbcTemplate = customNamedParameterJdbcTemplate;
        this.mapeoDoctorDTO = mapeoDoctorDTO;
    }

    @Override
    public List<DtoDoctor> listarTodos() {
        return this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate()
                .query(sqlListarTodos, this.mapeoDoctorDTO);
    }

    @Override
    public List<DtoDoctor> buscarPorEspecialidad(String especialidad) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("especialidad", especialidad);
        return this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate()
                .query(sqlBuscarPorEspecialidad, paramSource, this.mapeoDoctorDTO);
    }

    @Override
    public Optional<DtoDoctor> buscarPorNumeroDocumento(String numeroDocumento) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("numeroDocumento", numeroDocumento);
        List<DtoDoctor> resultado = this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate()
                .query(sqlObtenerPorNumeroDocumento, paramSource, this.mapeoDoctorDTO);
        return resultado.isEmpty() ? Optional.empty() : Optional.of(resultado.get(0));
    }
}
