package ceiba.com.co.paciente.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.dao.EmptyResultDataAccessException;

import ceiba.com.co.infraestructura.jdbc.EjecucionBaseDeDatos;

public class EjecucionBaseDeDatosTest {

    @Test
    void deberia_RetornarObjeto_Cuando_EjecucionEsExitosa() {
        // Arrange & Act
        String resultado = EjecucionBaseDeDatos.obtenerUnObjetoONull(() -> "DatoEjemplo");

        // Assert
        assertEquals("DatoEjemplo", resultado);
    }

    @Test
    void deberia_RetornarNull_Cuando_SeLanzaEmptyResultDataAccessException() {
        // Arrange & Act
        String resultado = EjecucionBaseDeDatos.obtenerUnObjetoONull(() -> {
            throw new EmptyResultDataAccessException(1);
        });

        // Assert
        assertNull(resultado);
    }

    @Test
    void deberia_RetornarEntero_Cuando_SeDevuelveEntero() {
        // Arrange & Act
        Integer resultado = EjecucionBaseDeDatos.obtenerUnObjetoONull(() -> 42);

        // Assert
        assertEquals(42, resultado);
    }

    @Test
    void deberia_RelanzarExcepcion_Cuando_SeLanzaExcepcionInesperada() {
        // Arrange & Act & Assert
        org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class, () ->
                EjecucionBaseDeDatos.obtenerUnObjetoONull(() -> {
                    throw new IllegalStateException("Error no controlado");
                })
        );
    }
}
