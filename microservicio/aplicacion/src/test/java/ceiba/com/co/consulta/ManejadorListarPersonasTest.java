package ceiba.com.co.consulta;

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

class ManejadorListarPersonasTest {

    private DaoPersona daoPersona;
    private ManejadorListarPersonas manejadorListarPersonas;

    @BeforeEach
    void setUp() {
        daoPersona = mock(DaoPersona.class);
        manejadorListarPersonas = new ManejadorListarPersonas(daoPersona);
    }

    @Test
    @DisplayName("Debería listar las personas invocando al DaoPersona")
    void deberia_RetornarListaPersonas_Cuando_ExistenPersonas() {
        // Arrange
        PersonaDTO persona1 = new PersonaDTO(123L, "Juan", "Perez", "juan@test.com", LocalDate.of(1990, 1, 1));
        PersonaDTO persona2 = new PersonaDTO(456L, "Maria", "Lopez", "maria@test.com", LocalDate.of(1985, 6, 15));
        when(daoPersona.listar()).thenReturn(List.of(persona1, persona2));

        // Act
        List<PersonaDTO> resultado = manejadorListarPersonas.ejecutar();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Juan", resultado.get(0).getNombre());
        assertEquals("Maria", resultado.get(1).getNombre());
        verify(daoPersona, times(1)).listar();
    }

    @Test
    void deberia_RetornarListaVacia_Cuando_NoExistenPersonas() {
        // Arrange
        when(daoPersona.listar()).thenReturn(Collections.emptyList());

        // Act
        List<PersonaDTO> resultado = manejadorListarPersonas.ejecutar();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(daoPersona, times(1)).listar();
    }
}
