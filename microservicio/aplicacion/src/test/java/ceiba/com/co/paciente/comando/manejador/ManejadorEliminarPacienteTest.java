package ceiba.com.co.paciente.comando.manejador;

import ceiba.com.co.paciente.comando.ComandoEliminarPaciente;
import ceiba.com.co.excepcion.ExcepcionSinDatos;
import ceiba.com.co.paciente.servicio.ServicioEliminarPaciente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ManejadorEliminarPacienteTest {

    private ServicioEliminarPaciente servicioEliminarPaciente;
    private ManejadorEliminarPaciente manejadorEliminarPaciente;

    @BeforeEach
    void setUp() {
        servicioEliminarPaciente = mock(ServicioEliminarPaciente.class);
        manejadorEliminarPaciente = new ManejadorEliminarPaciente(servicioEliminarPaciente);
    }

    @Test
    @DisplayName("Debería ejecutar el manejador para eliminar paciente")
    void deberia_EliminarPaciente_Cuando_CedulaEsValida() {
        // Arrange
        ComandoEliminarPaciente comando = new ComandoEliminarPaciente(12345L);

        // Act
        manejadorEliminarPaciente.ejecutar(comando);

        // Assert
        verify(servicioEliminarPaciente, times(1)).ejecutar(12345L);
    }

    @Test
    void deberia_LanzarExcepcionSinDatos_Cuando_CedulaNoExiste() {
        // Arrange
        Long cedula = 99999L;
        ComandoEliminarPaciente comando = new ComandoEliminarPaciente(cedula);

        doThrow(new ExcepcionSinDatos("No existe una paciente con la cédula ingresada: " + cedula))
                .when(servicioEliminarPaciente).ejecutar(cedula);

        // Act & Assert
        assertThrows(ExcepcionSinDatos.class, () -> manejadorEliminarPaciente.ejecutar(comando));
        verify(servicioEliminarPaciente, times(1)).ejecutar(cedula);
    }
}
