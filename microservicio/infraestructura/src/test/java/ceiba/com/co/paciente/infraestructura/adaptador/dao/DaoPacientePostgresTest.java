package ceiba.com.co.paciente.infraestructura.adaptador.dao;

import ceiba.com.co.paciente.adaptador.dao.DaoPacientePostgres;
import ceiba.com.co.paciente.adaptador.dao.MapeoPacienteDTO;
import ceiba.com.co.infraestructura.jdbc.CustomNamedParameterJdbcTemplate;
import ceiba.com.co.paciente.modelo.dto.CriteriosBusquedaPaciente;
import ceiba.com.co.paciente.modelo.dto.Pagina;
import ceiba.com.co.paciente.modelo.dto.PacienteDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

public class DaoPacientePostgresTest {

    @Mock
    private CustomNamedParameterJdbcTemplate customNamedParameterJdbcTemplate;

    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Mock
    private MapeoPacienteDTO mapeoPacienteDTO;

    private DaoPacientePostgres daoPacientePostgres;

    @BeforeEach
    void setUp() {
        when(customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate()).thenReturn(namedParameterJdbcTemplate);
        daoPacientePostgres = new DaoPacientePostgres(customNamedParameterJdbcTemplate, mapeoPacienteDTO);
        ReflectionTestUtils.setField(DaoPacientePostgres.class, "sqlBuscarPorCriteriosBase", "select * from paciente where 1=1");
        ReflectionTestUtils.setField(DaoPacientePostgres.class, "sqlListar", "select * from paciente");
        ReflectionTestUtils.setField(DaoPacientePostgres.class, "sqlBuscarPorNumeroDocumento", "select * from paciente where numero_documento = :numero_documento");
    }

    @Test
    void deberia_ListarTodasLasPacientes_Cuando_SeEjecutaConsulta() {
        // Arrange
        when(namedParameterJdbcTemplate.query(anyString(), eq(mapeoPacienteDTO)))
                .thenReturn(Collections.emptyList());

        // Act
        List<PacienteDTO> resultado = daoPacientePostgres.listar();

        // Assert
        assertNotNull(resultado);
        verify(namedParameterJdbcTemplate).query(eq("select * from paciente"), eq(mapeoPacienteDTO));
    }

    @Test
    void deberia_BuscarPorCriterios_Cuando_SeProporcionanNombreApellidoRangoEdadYOrdenamiento() {
        // Arrange
        CriteriosBusquedaPaciente criterios = new CriteriosBusquedaPaciente(
                "Juan", "Perez", 20, 40, 0, 10, "fechaNacimiento,desc"
        );

        when(namedParameterJdbcTemplate.queryForObject(anyString(), any(MapSqlParameterSource.class), eq(Integer.class)))
                .thenReturn(1);

        when(namedParameterJdbcTemplate.query(anyString(), any(MapSqlParameterSource.class), eq(mapeoPacienteDTO)))
                .thenReturn(List.of(new PacienteDTO(123L, "CC", "Juan", "Perez", null, "3001234567", "juan@test.com", "EPS", "MASCULINO")));

        // Act
        Pagina<PacienteDTO> pagina = daoPacientePostgres.buscarPorCriterios(criterios);

        // Assert
        assertNotNull(pagina);
        assertEquals(1, pagina.getTotalElementos());
        assertEquals(1, pagina.getContenido().size());

        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        verify(namedParameterJdbcTemplate).query(sqlCaptor.capture(), any(MapSqlParameterSource.class), eq(mapeoPacienteDTO));

        String sqlGenerado = sqlCaptor.getValue();
        assertTrue(sqlGenerado.contains("lower(nombre) like :nombre"));
        assertTrue(sqlGenerado.contains("lower(apellido) like :apellido"));
        assertTrue(sqlGenerado.contains("fecha_nacimiento <= :fechaNacimientoMaxima"));
        assertTrue(sqlGenerado.contains("fecha_nacimiento >= :fechaNacimientoMinima"));
        assertTrue(sqlGenerado.contains("order by fecha_nacimiento desc"));
    }

    @Test
    void deberia_BuscarPorCriterios_Cuando_SinCriteriosSeUsanValoresPorDefecto() {
        // Arrange
        CriteriosBusquedaPaciente criterios = new CriteriosBusquedaPaciente(
                null, null, null, null, 0, 10, null
        );

        when(namedParameterJdbcTemplate.queryForObject(anyString(), any(MapSqlParameterSource.class), eq(Integer.class)))
                .thenReturn(0);

        when(namedParameterJdbcTemplate.query(anyString(), any(MapSqlParameterSource.class), eq(mapeoPacienteDTO)))
                .thenReturn(Collections.emptyList());

        // Act
        Pagina<PacienteDTO> pagina = daoPacientePostgres.buscarPorCriterios(criterios);

        // Assert
        assertNotNull(pagina);
        assertEquals(0, pagina.getTotalElementos());
        assertTrue(pagina.getContenido().isEmpty());

        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        verify(namedParameterJdbcTemplate).query(sqlCaptor.capture(), any(MapSqlParameterSource.class), eq(mapeoPacienteDTO));

        String sqlGenerado = sqlCaptor.getValue();
        assertTrue(sqlGenerado.contains("order by apellido asc"));
    }
}
