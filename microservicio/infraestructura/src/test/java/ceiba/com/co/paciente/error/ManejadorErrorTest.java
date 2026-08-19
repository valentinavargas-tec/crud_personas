package ceiba.com.co.error;

import ceiba.com.co.excepcion.ExcepcionDuplicidad;
import ceiba.com.co.excepcion.ExcepcionLongitudValor;
import ceiba.com.co.excepcion.ExcepcionSinDatos;
import ceiba.com.co.excepcion.ExcepcionValorInvalido;
import ceiba.com.co.excepcion.ExcepcionValorObligatorio;
import ceiba.com.co.infraestructura.error.ErrorRespuesta;
import ceiba.com.co.infraestructura.error.ManejadorError;
import ceiba.com.co.infraestructura.excepcion.ExcepcionTecnica;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ManejadorErrorTest {

    private ManejadorError manejadorError;

    @BeforeEach
    void setUp() {
        manejadorError = new ManejadorError();
    }

    @Test
    void deberia_Retornar400_Cuando_ExcepcionDuplicidadEsLanzada() {
        // Arrange
        ExcepcionDuplicidad excepcion = new ExcepcionDuplicidad("La paciente ya existe");

        // Act
        ResponseEntity<ErrorRespuesta> respuesta = manejadorError.handleAllExceptions(excepcion);
        ErrorRespuesta error = respuesta.getBody();

        // Assert
        assertNotNull(respuesta);
        assertNotNull(error);
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("ExcepcionDuplicidad", error.getNombreExcepcion());
        assertEquals("La paciente ya existe", error.getMensaje());
    }

    @Test
    void deberia_Retornar404_Cuando_ExcepcionSinDatosEsLanzada() {
        // Arrange
        ExcepcionSinDatos excepcion = new ExcepcionSinDatos("No se encontro paciente");

        // Act
        ResponseEntity<ErrorRespuesta> respuesta = manejadorError.handleAllExceptions(excepcion);
        ErrorRespuesta error = respuesta.getBody();

        // Assert
        assertNotNull(respuesta);
        assertNotNull(error);
        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
        assertEquals("ExcepcionSinDatos", error.getNombreExcepcion());
    }

    @Test
    void deberia_Retornar400_Cuando_ExcepcionValorInvalidoEsLanzada() {
        // Arrange
        ExcepcionValorInvalido excepcion = new ExcepcionValorInvalido("El valor es invalido");

        // Act
        ResponseEntity<ErrorRespuesta> respuesta = manejadorError.handleAllExceptions(excepcion);
        ErrorRespuesta error = respuesta.getBody();

        // Assert
        assertNotNull(respuesta);
        assertNotNull(error);
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("ExcepcionValorInvalido", error.getNombreExcepcion());
        assertEquals("El valor es invalido", error.getMensaje());
    }

    @Test
    void deberia_Retornar400_Cuando_ExcepcionValorObligatorioEsLanzada() {
        // Arrange
        ExcepcionValorObligatorio excepcion = new ExcepcionValorObligatorio("El campo es obligatorio");

        // Act
        ResponseEntity<ErrorRespuesta> respuesta = manejadorError.handleAllExceptions(excepcion);
        ErrorRespuesta error = respuesta.getBody();

        // Assert
        assertNotNull(respuesta);
        assertNotNull(error);
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("ExcepcionValorObligatorio", error.getNombreExcepcion());
    }

    @Test
    void deberia_Retornar400_Cuando_ExcepcionLongitudValorEsLanzada() {
        // Arrange
        ExcepcionLongitudValor excepcion = new ExcepcionLongitudValor("Longitud invalida");

        // Act
        ResponseEntity<ErrorRespuesta> respuesta = manejadorError.handleAllExceptions(excepcion);
        ErrorRespuesta error = respuesta.getBody();

        // Assert
        assertNotNull(respuesta);
        assertNotNull(error);
        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertEquals("ExcepcionLongitudValor", error.getNombreExcepcion());
    }

    @Test
    void deberia_Retornar500_Cuando_ExcepcionTecnicaEsLanzada() {
        // Arrange
        ExcepcionTecnica excepcion = new ExcepcionTecnica("Error tecnico", new RuntimeException("Causa"));

        // Act
        ResponseEntity<ErrorRespuesta> respuesta = manejadorError.handleAllExceptions(excepcion);

        // Assert
        assertNotNull(respuesta);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, respuesta.getStatusCode());
    }

    @Test
    void deberia_Retornar500_Cuando_ExcepcionDesconocidaEsLanzada() {
        // Arrange
        RuntimeException excepcion = new RuntimeException("Error inesperado de sistema");

        // Act
        ResponseEntity<ErrorRespuesta> respuesta = manejadorError.handleAllExceptions(excepcion);

        // Assert
        assertNotNull(respuesta);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, respuesta.getStatusCode());
    }
}