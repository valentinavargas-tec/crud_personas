package ceiba.com.co.paciente.adaptador.repositorio;

import ceiba.com.co.infraestructura.jdbc.CustomNamedParameterJdbcTemplate;
import ceiba.com.co.infraestructura.jdbc.EjecucionBaseDeDatos;
import ceiba.com.co.infraestructura.jdbc.sqlstatement.SqlStatement;
import ceiba.com.co.paciente.modelo.entidad.Paciente;
import ceiba.com.co.paciente.puerto.repositorio.RepositorioPaciente;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class RepositorioPacientePostgres implements RepositorioPaciente {

    private final CustomNamedParameterJdbcTemplate customNamedParameterJdbcTemplate;
    private final MapeoPaciente mapeoPaciente;

    @SqlStatement(namespace = "paciente", value = "crear")
    private static String sqlCrear;

    @SqlStatement(namespace = "paciente", value = "obtenerpornumerodocumento")
    private static String sqlObtenerPorNumeroDocumento;

    @SqlStatement(namespace = "paciente", value = "actualizar")
    private static String sqlActualizar;

    @SqlStatement(namespace = "paciente", value = "eliminar")
    private static String sqlEliminar;

    @SqlStatement(namespace = "paciente", value = "existeconnumerodocumento")
    private static String sqlExisteConNumeroDocumento;

    @SqlStatement(namespace = "paciente", value = "existeconcorreoelectronico")
    private static String sqlExisteConCorreoElectronico;

    public RepositorioPacientePostgres(CustomNamedParameterJdbcTemplate customNamedParameterJdbcTemplate,
                                      MapeoPaciente mapeoPaciente) {
        this.customNamedParameterJdbcTemplate = customNamedParameterJdbcTemplate;
        this.mapeoPaciente = mapeoPaciente;
    }

    @Override
    public Long guardar(Paciente paciente) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("numeroDocumento", paciente.getNumeroDocumento());
        paramSource.addValue("tipoDocumento", paciente.getTipoDocumento().name());
        paramSource.addValue("nombre", paciente.getNombre());
        paramSource.addValue("apellido", paciente.getApellido());
        paramSource.addValue("fechaNacimiento", paciente.getFechaNacimiento());
        paramSource.addValue("telefono", paciente.getTelefono());
        paramSource.addValue("correoElectronico", paciente.getCorreoElectronico());
        paramSource.addValue("eps", paciente.getEps());
        paramSource.addValue("genero", paciente.getGenero().name());
        this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate().update(sqlCrear, paramSource);
        return paciente.getNumeroDocumento();
    }

    @Override
    public java.util.Optional<Paciente> obtener(Long numeroDocumento) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("numeroDocumento", numeroDocumento);
        Paciente paciente = EjecucionBaseDeDatos.obtenerUnObjetoONull(() ->
                this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate().queryForObject(sqlObtenerPorNumeroDocumento,
                        paramSource, this.mapeoPaciente));
        return java.util.Optional.ofNullable(paciente);
    }

    @Override
    public void actualizar(Paciente paciente) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("numeroDocumento", paciente.getNumeroDocumento());
        paramSource.addValue("tipoDocumento", paciente.getTipoDocumento().name());
        paramSource.addValue("nombre", paciente.getNombre());
        paramSource.addValue("apellido", paciente.getApellido());
        paramSource.addValue("fechaNacimiento", paciente.getFechaNacimiento());
        paramSource.addValue("telefono", paciente.getTelefono());
        paramSource.addValue("correoElectronico", paciente.getCorreoElectronico());
        paramSource.addValue("eps", paciente.getEps());
        paramSource.addValue("genero", paciente.getGenero().name());
        this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate().update(sqlActualizar, paramSource);
    }

    @Override
    public void eliminar(Long numeroDocumento) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("numeroDocumento", numeroDocumento);
        this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate().update(sqlEliminar, paramSource);
    }

    @Override
    public boolean existeConNumeroDocumento(Long numeroDocumento) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("numeroDocumento", numeroDocumento);
        Integer count = this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate().queryForObject(sqlExisteConNumeroDocumento, paramSource, Integer.class);
        return count != null && count > 0;
    }

    @Override
    public boolean existeConCorreoElectronico(String correoElectronico) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("correoElectronico", correoElectronico);
        Integer count = this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate().queryForObject(sqlExisteConCorreoElectronico, paramSource, Integer.class);
        return count != null && count > 0;
    }
}