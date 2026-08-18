package ceiba.com.co.adaptador.repositorio;

import ceiba.com.co.infraestructura.jdbc.CustomNamedParameterJdbcTemplate;
import ceiba.com.co.infraestructura.jdbc.EjecucionBaseDeDatos;
import ceiba.com.co.infraestructura.jdbc.sqlstatement.SqlStatement;
import ceiba.com.co.modelo.entidad.Persona;
import ceiba.com.co.puerto.repositorio.RepositorioPersona;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class RepositorioPersonaPostgres implements RepositorioPersona {

    private final CustomNamedParameterJdbcTemplate customNamedParameterJdbcTemplate;
    private final MapeoPersona mapeoPersona;

    @SqlStatement(namespace = "persona", value = "crear")
    private static String sqlCrear;

    @SqlStatement(namespace = "persona", value = "obtenerporcedula")
    private static String sqlObtenerPorCedula;

    @SqlStatement(namespace = "persona", value = "actualizar")
    private static String sqlActualizar;

    @SqlStatement(namespace = "persona", value = "eliminar")
    private static String sqlEliminar;

    @SqlStatement(namespace = "persona", value = "existeconcedula")
    private static String sqlExisteConCedula;

    @SqlStatement(namespace = "persona", value = "existeconemail")
    private static String sqlExisteConEmail;

    public RepositorioPersonaPostgres(CustomNamedParameterJdbcTemplate customNamedParameterJdbcTemplate,
                                      MapeoPersona mapeoPersona) {
        this.customNamedParameterJdbcTemplate = customNamedParameterJdbcTemplate;
        this.mapeoPersona = mapeoPersona;
    }

    @Override
    public Long guardar(Persona persona) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("cedula", persona.getCedula());
        paramSource.addValue("nombre", persona.getNombre());
        paramSource.addValue("apellido", persona.getApellido());
        paramSource.addValue("email", persona.getEmail());
        paramSource.addValue("fechaNacimiento", persona.getFechaNacimiento());
        this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate().update(sqlCrear, paramSource);
        return persona.getCedula();
    }

    @Override
    public Persona obtener(Long cedula) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("cedula", cedula);
        return EjecucionBaseDeDatos.obtenerUnObjetoONull(() ->
                this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate().queryForObject(sqlObtenerPorCedula,
                        paramSource, this.mapeoPersona));
    }

    @Override
    public void actualizar(Persona persona) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("cedula", persona.getCedula());
        paramSource.addValue("nombre", persona.getNombre());
        paramSource.addValue("apellido", persona.getApellido());
        paramSource.addValue("email", persona.getEmail());
        paramSource.addValue("fechaNacimiento", persona.getFechaNacimiento());
        this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate().update(sqlActualizar, paramSource);
    }

    @Override
    public void eliminar(Long cedula) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("cedula", cedula);
        this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate().update(sqlEliminar, paramSource);
    }

    @Override
    public boolean existeConCedula(Long cedula) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("cedula", cedula);
        Integer count = this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate().queryForObject(sqlExisteConCedula, paramSource, Integer.class);
        return count != null && count > 0;
    }

    @Override
    public boolean existeConEmail(String email) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("email", email);
        Integer count = this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate().queryForObject(sqlExisteConEmail, paramSource, Integer.class);
        return count != null && count > 0;
    }
}