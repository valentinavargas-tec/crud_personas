package ceiba.com.co.consulta;

import ceiba.com.co.excepcion.ExcepcionSinDatos;
import ceiba.com.co.modelo.dto.PersonaDTO;
import ceiba.com.co.puerto.dao.DaoPersona;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ManejadorBuscarPersonaPorCedulaTest {

    private DaoPersona daoPersona;
    private ManejadorBuscarPersonaPorCedula manejadorBuscarPersonaPorCedula;

    @BeforeEach
    void setUp() {
        daoPersona = mock(DaoPersona.class);
        manejadorBuscarPersonaPorCedula = new ManejadorBuscarPersonaPorCedula(daoPersona);
    }

    @Test
    void deberia_RetornarPersonaDTO_Cuando_CedulaExiste() {
        // Arrange
        Long cedula = 12345L;
        PersonaDTO personaEsperada = new PersonaDTO(12345L, "Juan", "Perez", "juan@test.com", LocalDate.of(1990, 1, 1));
        when(daoPersona.buscarPorCedula(cedula)).thenReturn(personaEsperada);

        // Act
        PersonaDTO resultado = manejadorBuscarPersonaPorCedula.ejecutar(cedula);

        // Assert
        assertNotNull(resultado);
        assertEquals(12345L, resultado.getCedula());
        assertEquals("Juan", resultado.getNombre());
        assertEquals("Perez", resultado.getApellido());
        verify(daoPersona, times(1)).buscarPorCedula(cedula);
    }

    @Test
    void deberia_LanzarExcepcionSinDatos_Cuando_PersonaNoEsEncontrada() {
        // Arrange
        Long cedula = 99999L;
        when(daoPersona.buscarPorCedula(cedula)).thenReturn(null);

        // Act & Assert
        ExcepcionSinDatos excepcion = assertThrows(
                ExcepcionSinDatos.class,
                () -> manejadorBuscarPersonaPorCedula.ejecutar(cedula)
        );

        assertEquals("Persona no encontrada", excepcion.getMessage());
        verify(daoPersona, times(1)).buscarPorCedula(cedula);
    }
}
