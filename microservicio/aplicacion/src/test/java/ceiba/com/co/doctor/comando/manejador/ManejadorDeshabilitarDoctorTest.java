package ceiba.com.co.doctor.comando.manejador;

import ceiba.com.co.ComandoRespuesta;
import ceiba.com.co.doctor.servicio.ServicioDeshabilitarDoctor;
import ceiba.com.co.excepcion.ExcepcionSinDatos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ManejadorDeshabilitarDoctorTest {

    private ServicioDeshabilitarDoctor servicioDeshabilitarDoctor;
    private ManejadorDeshabilitarDoctor manejadorDeshabilitarDoctor;

    @BeforeEach
    void setUp() {
        servicioDeshabilitarDoctor = Mockito.mock(ServicioDeshabilitarDoctor.class);
        manejadorDeshabilitarDoctor = new ManejadorDeshabilitarDoctor(servicioDeshabilitarDoctor);
    }

    @Test
    @DisplayName("Debería deshabilitar doctor y retornar ComandoRespuesta exitosa")
    void deberiaDeshabilitarDoctor_Y_RetornarRespuesta() {
        // Arrange
        String numeroDocumento = "DOC-001";
        doNothing().when(servicioDeshabilitarDoctor).ejecutar(numeroDocumento);

        // Act
        ComandoRespuesta<String> respuesta = manejadorDeshabilitarDoctor.ejecutar(numeroDocumento);

        // Assert
        assertNotNull(respuesta);
        assertEquals("DOC-001", respuesta.getValor());
        assertEquals("Doctor deshabilitado exitosamente", respuesta.getMensaje());
        verify(servicioDeshabilitarDoctor, times(1)).ejecutar(numeroDocumento);
    }

    @Test
    @DisplayName("Debería propagar ExcepcionSinDatos cuando el doctor no existe")
    void deberiaPropagar_ExcepcionSinDatos_Cuando_DoctorNoExiste() {
        // Arrange
        String documentoInexistente = "NOEXISTE";
        doThrow(new ExcepcionSinDatos("No existe un doctor con el número de documento NOEXISTE."))
                .when(servicioDeshabilitarDoctor).ejecutar(documentoInexistente);

        // Act & Assert
        assertThrows(ExcepcionSinDatos.class,
                () -> manejadorDeshabilitarDoctor.ejecutar(documentoInexistente));
    }
}
