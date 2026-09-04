package ceiba.com.co.doctor.adaptador.repositorio;

import ceiba.com.co.doctor.modelo.entidad.Doctor;
import ceiba.com.co.doctor.puerto.repositorio.RepositorioDoctor;
import ceiba.com.co.infraestructura.jdbc.CustomNamedParameterJdbcTemplate;
import ceiba.com.co.infraestructura.jdbc.sqlstatement.SqlStatement;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public class RepositorioDoctorPostgres implements RepositorioDoctor {

    private final CustomNamedParameterJdbcTemplate customNamedParameterJdbcTemplate;

    @SqlStatement(namespace = "doctor", value = "crear")
    private static String sqlCrear;

    @SqlStatement(namespace = "doctor", value = "actualizar")
    private static String sqlActualizar;

    @SqlStatement(namespace = "doctor", value = "deshabilitar")
    private static String sqlDeshabilitar;

    @SqlStatement(namespace = "doctor", value = "obtenerpornumerodocumento")
    private static String sqlObtenerPorNumeroDocumento;

    @SqlStatement(namespace = "doctor", value = "existeporcorreoinstitucional")
    private static String sqlExistePorCorreoInstitucional;

    @SqlStatement(namespace = "doctor", value = "existeportarjetaprofesional")
    private static String sqlExistePorTarjetaProfesional;

    @SqlStatement(namespace = "doctor", value = "existecorreoparaotrodoctor")
    private static String sqlExisteCorreoParaOtroDoctor;

    public RepositorioDoctorPostgres(CustomNamedParameterJdbcTemplate customNamedParameterJdbcTemplate) {
        this.customNamedParameterJdbcTemplate = customNamedParameterJdbcTemplate;
    }

    @Override
    public void guardar(Doctor doctor) {
        MapSqlParameterSource paramSource = construirParametrosCompletos(doctor);
        paramSource.addValue("habilitado", doctor.isHabilitado());
        this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate()
                .update(sqlCrear, paramSource);
    }

    @Override
    public void actualizar(Doctor doctor) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("numeroDocumento", doctor.getNumeroDocumento());
        paramSource.addValue("nombre", doctor.getNombre());
        paramSource.addValue("apellido", doctor.getApellido());
        paramSource.addValue("especialidad", doctor.getEspecialidad());
        paramSource.addValue("correoInstitucional", doctor.getCorreoInstitucional());
        this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate()
                .update(sqlActualizar, paramSource);
    }

    @Override
    public void deshabilitar(String numeroDocumento) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("numeroDocumento", numeroDocumento);
        this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate()
                .update(sqlDeshabilitar, paramSource);
    }

    @Override
    public Optional<Doctor> obtenerPorNumeroDocumento(String numeroDocumento) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("numeroDocumento", numeroDocumento);
        List<Doctor> resultado = this.customNamedParameterJdbcTemplate
                .getNamedParameterJdbcTemplate()
                .query(sqlObtenerPorNumeroDocumento, paramSource, doctorRowMapper());
        return resultado.isEmpty() ? Optional.empty() : Optional.of(resultado.get(0));
    }

    @Override
    public boolean existePorCorreoInstitucional(String correoInstitucional) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("correoInstitucional", correoInstitucional);
        Integer count = this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate()
                .queryForObject(sqlExistePorCorreoInstitucional, paramSource, Integer.class);
        return count != null && count > 0;
    }

    @Override
    public boolean existePorTarjetaProfesional(String tarjetaProfesional) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("tarjetaProfesional", tarjetaProfesional);
        Integer count = this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate()
                .queryForObject(sqlExistePorTarjetaProfesional, paramSource, Integer.class);
        return count != null && count > 0;
    }

    @Override
    public boolean existeCorreoParaOtroDoctor(String correoInstitucional, String numeroDocumentoExcluido) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("correoInstitucional", correoInstitucional);
        paramSource.addValue("numeroDocumento", numeroDocumentoExcluido);
        Integer count = this.customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate()
                .queryForObject(sqlExisteCorreoParaOtroDoctor, paramSource, Integer.class);
        return count != null && count > 0;
    }

    private RowMapper<Doctor> doctorRowMapper() {
        return (rs, rowNum) -> Doctor.builder()
                .conNumeroDocumento(rs.getString("numero_documento"))
                .conNombre(rs.getString("nombre"))
                .conApellido(rs.getString("apellido"))
                .conTarjetaProfesional(rs.getString("tarjeta_profesional"))
                .conEspecialidad(rs.getString("especialidad"))
                .conCorreoInstitucional(rs.getString("correo_institucional"))
                .conHabilitado(rs.getBoolean("habilitado"))
                .build();
    }

    private MapSqlParameterSource construirParametrosCompletos(Doctor doctor) {
        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("numeroDocumento", doctor.getNumeroDocumento());
        paramSource.addValue("nombre", doctor.getNombre());
        paramSource.addValue("apellido", doctor.getApellido());
        paramSource.addValue("tarjetaProfesional", doctor.getTarjetaProfesional());
        paramSource.addValue("especialidad", doctor.getEspecialidad());
        paramSource.addValue("correoInstitucional", doctor.getCorreoInstitucional());
        return paramSource;
    }
}

