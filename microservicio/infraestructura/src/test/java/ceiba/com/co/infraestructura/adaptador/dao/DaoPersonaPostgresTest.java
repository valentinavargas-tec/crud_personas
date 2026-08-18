package ceiba.com.co.infraestructura.adaptador.dao;

import ceiba.com.co.adaptador.dao.DaoPersonaPostgres;
import ceiba.com.co.adaptador.dao.MapeoPersonaDTO;
import ceiba.com.co.infraestructura.jdbc.CustomNamedParameterJdbcTemplate;
import ceiba.com.co.modelo.dto.CriteriosBusquedaPersona;
import ceiba.com.co.modelo.dto.Pagina;
import ceiba.com.co.modelo.dto.PersonaDTO;
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

public class DaoPersonaPostgresTest {

    @Mock
    private CustomNamedParameterJdbcTemplate customNamedParameterJdbcTemplate;

    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Mock
    private MapeoPersonaDTO mapeoPersonaDTO;

    private DaoPersonaPostgres daoPersonaPostgres;

    @BeforeEach
    void setUp() {
        when(customNamedParameterJdbcTemplate.getNamedParameterJdbcTemplate()).thenReturn(namedParameterJdbcTemplate);
        daoPersonaPostgres = new DaoPersonaPostgres(customNamedParameterJdbcTemplate, mapeoPersonaDTO);
        ReflectionTestUtils.setField(DaoPersonaPostgres.class, "sqlBuscarPorCriteriosBase", "select * from persona where 1=1");
        ReflectionTestUtils.setField(DaoPersonaPostgres.class, "sqlListar", "select * from persona");
        ReflectionTestUtils.setField(DaoPersonaPostgres.class, "sqlBuscarPorCedula", "select * from persona where cedula = :cedula");
    }

    @Test
    void deberia_ListarTodasLasPersonas_Cuando_SeEjecutaConsulta() {
        // Arrange
        when(namedParameterJdbcTemplate.query(anyString(), eq(mapeoPersonaDTO)))
                .thenReturn(Collections.emptyList());

        // Act
        List<PersonaDTO> resultado = daoPersonaPostgres.listar();

        // Assert
        assertNotNull(resultado);
        verify(namedParameterJdbcTemplate).query(eq("select * from persona"), eq(mapeoPersonaDTO));
    }

    @Test
    void deberia_BuscarPorCriterios_Cuando_SeProporcionanNombreApellidoRangoEdadYOrdenamiento() {
        // Arrange
        CriteriosBusquedaPersona criterios = new CriteriosBusquedaPersona(
                "Juan", "Perez", 20, 40, 0, 10, "fechaNacimiento,desc"
        );

        when(namedParameterJdbcTemplate.queryForObject(anyString(), any(MapSqlParameterSource.class), eq(Integer.class)))
                .thenReturn(1);

        when(namedParameterJdbcTemplate.query(anyString(), any(MapSqlParameterSource.class), eq(mapeoPersonaDTO)))
                .thenReturn(List.of(new PersonaDTO(123L, "Juan", "Perez", "juan@test.com", null)));

        // Act
        Pagina<PersonaDTO> pagina = daoPersonaPostgres.buscarPorCriterios(criterios);

        // Assert
        assertNotNull(pagina);
        assertEquals(1, pagina.getTotalElementos());
        assertEquals(1, pagina.getContenido().size());

        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        verify(namedParameterJdbcTemplate).query(sqlCaptor.capture(), any(MapSqlParameterSource.class), eq(mapeoPersonaDTO));

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
        CriteriosBusquedaPersona criterios = new CriteriosBusquedaPersona(
                null, null, null, null, 0, 10, null
        );

        when(namedParameterJdbcTemplate.queryForObject(anyString(), any(MapSqlParameterSource.class), eq(Integer.class)))
                .thenReturn(0);

        when(namedParameterJdbcTemplate.query(anyString(), any(MapSqlParameterSource.class), eq(mapeoPersonaDTO)))
                .thenReturn(Collections.emptyList());

        // Act
        Pagina<PersonaDTO> pagina = daoPersonaPostgres.buscarPorCriterios(criterios);

        // Assert
        assertNotNull(pagina);
        assertEquals(0, pagina.getTotalElementos());
        assertTrue(pagina.getContenido().isEmpty());

        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        verify(namedParameterJdbcTemplate).query(sqlCaptor.capture(), any(MapSqlParameterSource.class), eq(mapeoPersonaDTO));

        String sqlGenerado = sqlCaptor.getValue();
        assertTrue(sqlGenerado.contains("order by apellido asc"));
    }
}
