package ceiba.com.co.paciente.modelo.dto;

import ceiba.com.co.excepcion.ExcepcionValorInvalido;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CriteriosBusquedaPacienteTest {

    @Test
    void deberiaCrearCriteriosBusquedaValidos() {
        // Arrange & Act
        CriteriosBusquedaPaciente criterios = new CriteriosBusquedaPaciente(
                " Juan ", " Perez ", 18, 50, 1, 20, "nombre,asc"
        );

        // Assert
        assertEquals("Juan", criterios.getNombre());
        assertEquals("Perez", criterios.getApellido());
        assertEquals(18, criterios.getEdadMinima());
        assertEquals(50, criterios.getEdadMaxima());
        assertEquals(1, criterios.getPage());
        assertEquals(20, criterios.getSize());
        assertEquals("nombre,asc", criterios.getSort());
    }

    @Test
    void deberiaAplicarValoresPorDefecto_Cuando_ParametrosPaginacionYSortValidos() {
        // Arrange & Act
        CriteriosBusquedaPaciente criterios = new CriteriosBusquedaPaciente(
                null, null, null, null, -1, 0, null
        );

        // Assert
        assertNull(criterios.getNombre());
        assertNull(criterios.getApellido());
        assertNull(criterios.getEdadMinima());
        assertNull(criterios.getEdadMaxima());
        assertEquals(0, criterios.getPage());
        assertEquals(10, criterios.getSize());
        assertNull(criterios.getSort());
    }

    @Test
    void deberiaAjustarTamanoMaximoPagina_Cuando_SuperaLimite() {
        // Arrange & Act
        CriteriosBusquedaPaciente criterios = new CriteriosBusquedaPaciente(
                null, null, null, null, 0, 200, null
        );

        // Assert
        assertEquals(100, criterios.getSize());
    }

    @Test
    void deberiaLanzarExcepcion_Cuando_EdadMinimaEsNegativa() {
        // Arrange, Act & Assert
        ExcepcionValorInvalido excepcion = assertThrows(
                ExcepcionValorInvalido.class,
                () -> new CriteriosBusquedaPaciente(null, null, -1, 30, 0, 10, null)
        );
        assertTrue(excepcion.getMessage().contains("La edad mínima no puede ser negativa"));
    }

    @Test
    void deberiaLanzarExcepcion_Cuando_EdadMaximaEsNegativa() {
        // Arrange, Act & Assert
        ExcepcionValorInvalido excepcion = assertThrows(
                ExcepcionValorInvalido.class,
                () -> new CriteriosBusquedaPaciente(null, null, 18, -5, 0, 10, null)
        );
        assertTrue(excepcion.getMessage().contains("La edad máxima no puede ser negativa"));
    }

    @Test
    void deberiaLanzarExcepcion_Cuando_EdadMinimaMayorQueEdadMaxima() {
        // Arrange, Act & Assert
        ExcepcionValorInvalido excepcion = assertThrows(
                ExcepcionValorInvalido.class,
                () -> new CriteriosBusquedaPaciente(null, null, 40, 20, 0, 10, null)
        );
        assertTrue(excepcion.getMessage().contains("La edad mínima no puede ser mayor que la edad máxima"));
    }

    @Test
    void deberiaLanzarExcepcion_Cuando_FormatoOrdenamientoFormatoInvalido() {
        // Arrange, Act & Assert
        ExcepcionValorInvalido excepcion = assertThrows(
                ExcepcionValorInvalido.class,
                () -> new CriteriosBusquedaPaciente(null, null, null, null, 0, 10, "nombre;select *")
        );
        assertTrue(excepcion.getMessage().contains("El formato del parámetro de ordenamiento es inválido"));
    }

    @Test
    void deberiaLanzarExcepcion_Cuando_CampoOrdenamientoNoPermitido() {
        // Arrange, Act & Assert
        ExcepcionValorInvalido excepcion = assertThrows(
                ExcepcionValorInvalido.class,
                () -> new CriteriosBusquedaPaciente(null, null, null, null, 0, 10, "password,asc")
        );
        assertTrue(excepcion.getMessage().contains("El campo de ordenamiento solicitado no está permitido"));
    }

    @Test
    void deberiaPermitirOrdenamientoPorFechaNacimiento() {
        // Cubre la rama permitida de 'fechaNacimiento'
        CriteriosBusquedaPaciente criterios = new CriteriosBusquedaPaciente(
                null, null, null, 30, 0, 10, "fechaNacimiento,desc"
        );
        assertEquals("fechaNacimiento,desc", criterios.getSort());
        assertEquals(30, criterios.getEdadMaxima());
        assertNull(criterios.getEdadMinima());
    }
}