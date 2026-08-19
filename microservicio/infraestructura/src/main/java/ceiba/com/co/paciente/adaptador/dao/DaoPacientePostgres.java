package ceiba.com.co.paciente.adaptador.dao;

import ceiba.com.co.infraestructura.jdbc.CustomNamedParameterJdbcTemplate;
import ceiba.com.co.infraestructura.jdbc.EjecucionBaseDeDatos;
import ceiba.com.co.infraestructura.jdbc.sqlstatement.SqlStatement;
import ceiba.com.co.paciente.modelo.dto.CriteriosBusquedaPaciente;
import ceiba.com.co.paciente.modelo.dto.Pagina;
import ceiba.com.co.paciente.modelo.dto.PacienteDTO;
import ceiba.com.co.paciente.puerto.dao.DaoPaciente;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public class DaoPacientePostgres implements DaoPaciente {

    private final CustomNamedParameterJdbcTemplate customNamedParameterJdbcTemplate;
    private final MapeoPacienteDTO mapeoPacienteDTO;

    @SqlStatement(namespace = "paciente", value = "listar")
    private static String sqlListar;

    @SqlStatement(namespace = "paciente", value = "buscarpornumerodocumento")
    private static String sqlBuscarPorNumeroDocumento;

    @SqlStatement(namespace = "paciente", value = "buscarporcriteriosbase")
    private static String sqlBuscarPorCriteriosBase;

    public DaoPacientePostgres(CustomNamedParameterJdbcTemplate customNamedParameterJdbcTemplate, MapeoPacienteDTO mapeoPacienteDTO) {
        this.customNamedParameterJdbcTemplate = customNamedParameterJdbcTemplate;
        this.mapeoPacienteDTO = mapeoPacienteDTO;
    }

    @Override
    public List<PacienteDTO> listar() {
        return this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate()
                .query(sqlListar, mapeoPacienteDTO);
    }

    @Override
    public PacienteDTO buscarPorNumeroDocumento(Long numeroDocumento) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("numeroDocumento", numeroDocumento);
        return EjecucionBaseDeDatos.obtenerUnObjetoONull(() ->
                this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate()
                        .queryForObject(sqlBuscarPorNumeroDocumento, paramSource, mapeoPacienteDTO));
    }

    @Override
    public Pagina<PacienteDTO> buscarPorCriterios(CriteriosBusquedaPaciente criterios) {
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

        // Sorting (ya validado de forma segura en CriteriosBusquedaPaciente)
        String campo = "apellido";
        String orden = "asc";

        if (criterios.getSort() != null && !criterios.getSort().isBlank()) {
            String[] sortParts = criterios.getSort().split(",");
            campo = sortParts[0];
            if ("fechaNacimiento".equals(campo)) {
                campo = "fecha_nacimiento";
            } else if ("numeroDocumento".equals(campo)) {
                campo = "numero_documento";
            }
            if (sortParts.length > 1) {
                orden = sortParts[1].toLowerCase();
            }
        }
        sql.append(" order by ").append(campo).append(" ").append(orden);

        sql.append(" limit :limit offset :offset");
        paramSource.addValue("limit", criterios.getSize());
        paramSource.addValue("offset", criterios.getPage() * criterios.getSize());

        List<PacienteDTO> contenido = this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate()
                .query(sql.toString(), paramSource, mapeoPacienteDTO);

        int totalPaginas = (int) Math.ceil((double) total / criterios.getSize());

        return new Pagina<>(contenido, total, totalPaginas, criterios.getPage(), criterios.getSize());
    }
}
