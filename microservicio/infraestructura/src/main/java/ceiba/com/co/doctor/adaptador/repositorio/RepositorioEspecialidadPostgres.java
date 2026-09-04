package ceiba.com.co.doctor.adaptador.repositorio;

import ceiba.com.co.doctor.puerto.repositorio.RepositorioEspecialidad;
import ceiba.com.co.infraestructura.jdbc.CustomNamedParameterJdbcTemplate;
import ceiba.com.co.infraestructura.jdbc.sqlstatement.SqlStatement;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class RepositorioEspecialidadPostgres implements RepositorioEspecialidad {

    private final CustomNamedParameterJdbcTemplate customNamedParameterJdbcTemplate;

    @SqlStatement(namespace = "especialidad", value = "existe")
    private static String sqlExiste;

    public RepositorioEspecialidadPostgres(CustomNamedParameterJdbcTemplate customNamedParameterJdbcTemplate) {
        this.customNamedParameterJdbcTemplate = customNamedParameterJdbcTemplate;
    }

    @Override
    public boolean existe(String nombreEspecialidad) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("nombreEspecialidad", nombreEspecialidad);
        Integer count = this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate()
                .queryForObject(sqlExiste, paramSource, Integer.class);
        return count != null && count > 0;
    }
}
