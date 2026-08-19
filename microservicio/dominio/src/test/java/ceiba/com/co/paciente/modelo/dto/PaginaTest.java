package ceiba.com.co.paciente.modelo.dto;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PaginaTest {

    @Test
    void deberia_CrearPagina_Cuando_ContenidoEsProporcionado() {
        // Arrange
        List<String> contenido = List.of("Elemento 1", "Elemento 2");

        // Act
        Pagina<String> pagina = new Pagina<>(contenido, 2L, 1, 0, 10);

        // Assert
        assertEquals(2, pagina.getContenido().size());
        assertEquals("Elemento 1", pagina.getContenido().get(0));
        assertEquals(2L, pagina.getTotalElementos());
        assertEquals(1, pagina.getTotalPaginas());
        assertEquals(0, pagina.getNumeroPagina());
        assertEquals(10, pagina.getTamanoPagina());
    }

    @Test
    void deberia_RetornarListaVacia_Cuando_ContenidoEsNulo() {
        // Arrange & Act
        Pagina<String> pagina = new Pagina<>(null, 0L, 0, 0, 10);

        // Assert
        assertNotNull(pagina.getContenido());
        assertTrue(pagina.getContenido().isEmpty());
        assertEquals(0L, pagina.getTotalElementos());
    }

    @Test
    void deberia_CrearPaginaConListaVacia_Cuando_ContenidoEsListaVacia() {
        // Arrange
        List<String> listaVacia = List.of();

        // Act
        Pagina<String> pagina = new Pagina<>(listaVacia, 0L, 0, 0, 5);

        // Assert
        assertNotNull(pagina.getContenido());
        assertTrue(pagina.getContenido().isEmpty());
        assertEquals(5, pagina.getTamanoPagina());
    }
}