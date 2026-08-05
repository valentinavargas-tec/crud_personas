package ceiba.com.co.consulta;

import ceiba.com.co.modelo.dto.CriteriosBusquedaPersona;
import ceiba.com.co.modelo.dto.Pagina;
import ceiba.com.co.modelo.dto.PersonaDTO;
import ceiba.com.co.puerto.dao.DaoPersona;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ManejadorBuscarPersonasPorCriteriosTest {

    private DaoPersona daoPersona;
    private ManejadorBuscarPersonasPorCriterios manejadorBuscarPersonasPorCriterios;

    @BeforeEach
    void setUp() {
        daoPersona = mock(DaoPersona.class);
        manejadorBuscarPersonasPorCriterios = new ManejadorBuscarPersonasPorCriterios(daoPersona);
    }

    @Test
    @DisplayName("Debería retornar página de personas al buscar por criterios")
    void deberia_RetornarPaginaPersonas_Cuando_BusquedaPorCriteriosRetornaResultados() {
        CriteriosBusquedaPersona criterios = new CriteriosBusquedaPersona("Juan", "Perez", 20, 40, 0, 10, null);
        PersonaDTO personaDTO = new PersonaDTO(12345L, "Juan", "Perez", "juan@test.com", LocalDate.of(1990, 1, 1));
        Pagina<PersonaDTO> paginaEsperada = new Pagina<>(List.of(personaDTO), 1L, 1, 0, 10);
        when(daoPersona.buscarPorCriterios(criterios)).thenReturn(paginaEsperada);

        // Act
        Pagina<PersonaDTO> resultado = manejadorBuscarPersonasPorCriterios.ejecutar(criterios);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getTotalElementos());
        assertEquals(1, resultado.getContenido().size());
        assertEquals("Juan", resultado.getContenido().get(0).getNombre());
        verify(daoPersona, times(1)).buscarPorCriterios(criterios);
    }

    @Test
    void deberia_RetornarPaginaVacia_Cuando_NingunCriterioCoincide() {
        // Arrange
        CriteriosBusquedaPersona criterios = new CriteriosBusquedaPersona(null, null, null, null, 0, 10, null);
        Pagina<PersonaDTO> paginaVacia = new Pagina<>(Collections.emptyList(), 0L, 0, 0, 10);
        when(daoPersona.buscarPorCriterios(criterios)).thenReturn(paginaVacia);

        // Act
        Pagina<PersonaDTO> resultado = manejadorBuscarPersonasPorCriterios.ejecutar(criterios);

        // Assert
        assertNotNull(resultado);
        assertEquals(0L, resultado.getTotalElementos());
        assertTrue(resultado.getContenido().isEmpty());
        verify(daoPersona, times(1)).buscarPorCriterios(criterios);
    }
}
