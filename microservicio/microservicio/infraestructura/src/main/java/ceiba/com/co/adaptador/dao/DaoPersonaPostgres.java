package ceiba.com.co.adaptador.dao;

import ceiba.com.co.infraestructura.jdbc.CustomNamedParameterJdbcTemplate;
import ceiba.com.co.infraestructura.jdbc.EjecucionBaseDeDatos;
import ceiba.com.co.infraestructura.jdbc.sqlstatement.SqlStatement;
import ceiba.com.co.modelo.dto.CriteriosBusquedaPersona;
import ceiba.com.co.modelo.dto.Pagina;
import ceiba.com.co.modelo.dto.PersonaDTO;
import ceiba.com.co.puerto.dao.DaoPersona;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public class DaoPersonaPostgres implements DaoPersona {

    private final CustomNamedParameterJdbcTemplate customNamedParameterJdbcTemplate;
    private final MapeoPersonaDTO mapeoPersonaDTO;

    @SqlStatement(namespace = "persona", value = "listar")
    private static String sqlListar;

    @SqlStatement(namespace = "persona", value = "buscarporcedula")
    private static String sqlBuscarPorCedula;

    @SqlStatement(namespace = "persona", value = "buscarporcriteriosbase")
    private static String sqlBuscarPorCriteriosBase;

    public DaoPersonaPostgres(CustomNamedParameterJdbcTemplate customNamedParameterJdbcTemplate, MapeoPersonaDTO mapeoPersonaDTO) {
        this.customNamedParameterJdbcTemplate = customNamedParameterJdbcTemplate;
        this.mapeoPersonaDTO = mapeoPersonaDTO;
    }

    @Override
    public List<PersonaDTO> listar() {
        return this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate()
                .query(sqlListar, mapeoPersonaDTO);
    }

    @Override
    public PersonaDTO buscarPorCedula(Long cedula) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("cedula", cedula);
        return EjecucionBaseDeDatos.obtenerUnObjetoONull(() ->
                this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate()
                        .queryForObject(sqlBuscarPorCedula, paramSource, mapeoPersonaDTO));
    }

    @Override
    public Pagina<PersonaDTO> buscarPorCriterios(CriteriosBusquedaPersona criterios) {
        StringBuilder sql = new StringBuilder(sqlBuscarPorCriteriosBase);
        MapSqlParameterSource paramSource = new MapSqlParameterSource();

        if (criterios.getNombre() != null && !criterios.getNombre().isBlank()) {
            sql.append(" and lower(nombre) like :nombre");
            paramSource.addValue("nombre", "%" + criterios.getNombre().toLowerCase() + "%");
        }

        if (criterios.getApellido() != null && !criterios.getApellido().isBlank()) {
            sql.append(" and lower(apellido) like :apellido");
            paramSource.addValue("apellido", "%" + criterios.getApellido().toLowerCase() + "%");
        }

        LocalDate now = LocalDate.now();
        if (criterios.getEdadMinima() != null) {
            LocalDate fechaNacimientoMaxima = now.minusYears(criterios.getEdadMinima());
            sql.append(" and fecha_nacimiento <= :fechaNacimientoMaxima");
            paramSource.addValue("fechaNacimientoMaxima", fechaNacimientoMaxima);
        }

        if (criterios.getEdadMaxima() != null) {
            LocalDate fechaNacimientoMinima = now.minusYears(criterios.getEdadMaxima() + 1).plusDays(1);
            sql.append(" and fecha_nacimiento >= :fechaNacimientoMinima");
            paramSource.addValue("fechaNacimientoMinima", fechaNacimientoMinima);
        }

        // Count query
        String countSql = "select count(1) from (" + sql.toString() + ") as total";
        Integer totalElementos = this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate()
                .queryForObject(countSql, paramSource, Integer.class);
        long total = totalElementos != null ? totalElementos : 0L;

        // Sorting (ya validado de forma segura en CriteriosBusquedaPersona)
        String campo = "apellido";
        String orden = "asc";

        if (criterios.getSort() != null && !criterios.getSort().isBlank()) {
            String[] sortParts = criterios.getSort().split(",");
            campo = sortParts[0];
            if ("fechaNacimiento".equals(campo)) {
                campo = "fecha_nacimiento";
            }
            if (sortParts.length > 1) {
                orden = sortParts[1].toLowerCase();
            }
        }
        sql.append(" order by ").append(campo).append(" ").append(orden);

        // Pagination
        sql.append(" limit :limit offset :offset");
        paramSource.addValue("limit", criterios.getSize());
        paramSource.addValue("offset", criterios.getPage() * criterios.getSize());

        List<PersonaDTO> contenido = this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate()
                .query(sql.toString(), paramSource, mapeoPersonaDTO);

        int totalPaginas = (int) Math.ceil((double) total / criterios.getSize());

        return new Pagina<>(contenido, total, totalPaginas, criterios.getPage(), criterios.getSize());
    }
}
