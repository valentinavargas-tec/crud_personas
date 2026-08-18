package ceiba.com.co.comando.manejador;

import ceiba.com.co.comando.ComandoEliminarPersona;
import ceiba.com.co.excepcion.ExcepcionSinDatos;
import ceiba.com.co.servicio.ServicioEliminarPersona;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ManejadorEliminarPersonaTest {

    private ServicioEliminarPersona servicioEliminarPersona;
    private ManejadorEliminarPersona manejadorEliminarPersona;

    @BeforeEach
    void setUp() {
        servicioEliminarPersona = mock(ServicioEliminarPersona.class);
        manejadorEliminarPersona = new ManejadorEliminarPersona(servicioEliminarPersona);
    }

    @Test
    @DisplayName("Debería ejecutar el manejador para eliminar persona")
    void deberia_EliminarPersona_Cuando_CedulaEsValida() {
        // Arrange
        ComandoEliminarPersona comando = new ComandoEliminarPersona(12345L);

        // Act
        manejadorEliminarPersona.ejecutar(comando);

        // Assert
        verify(servicioEliminarPersona, times(1)).ejecutar(12345L);
    }

    @Test
    void deberia_LanzarExcepcionSinDatos_Cuando_CedulaNoExiste() {
        // Arrange
        Long cedula = 99999L;
        ComandoEliminarPersona comando = new ComandoEliminarPersona(cedula);

        doThrow(new ExcepcionSinDatos("No existe una persona con la cédula ingresada: " + cedula))
                .when(servicioEliminarPersona).ejecutar(cedula);

        // Act & Assert
        assertThrows(ExcepcionSinDatos.class, () -> manejadorEliminarPersona.ejecutar(comando));
        verify(servicioEliminarPersona, times(1)).ejecutar(cedula);
    }
}
