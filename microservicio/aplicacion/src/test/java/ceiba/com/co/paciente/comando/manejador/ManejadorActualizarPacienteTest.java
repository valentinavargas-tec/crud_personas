package ceiba.com.co.paciente.comando.manejador;

import ceiba.com.co.paciente.comando.ComandoActualizarPaciente;
import ceiba.com.co.paciente.comando.ComandoActualizarPacienteTestDataBuilder;
import ceiba.com.co.excepcion.ExcepcionSinDatos;
import ceiba.com.co.paciente.modelo.dto.DatosActualizarPaciente;
import ceiba.com.co.paciente.servicio.ServicioActualizarPaciente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ManejadorActualizarPacienteTest {

    private ServicioActualizarPaciente servicioActualizarPaciente;
    private ManejadorActualizarPaciente manejadorActualizarPaciente;

    @BeforeEach
    void setUp() {
        servicioActualizarPaciente = mock(ServicioActualizarPaciente.class);
        manejadorActualizarPaciente = new ManejadorActualizarPaciente(servicioActualizarPaciente);
    }

    @Test
    @DisplayName("Debería ejecutar el manejador para actualizar paciente")
    void deberia_ActualizarPaciente_Cuando_ComandoEsValido() {
        // Arrange
        Long numeroDocumento = 12345L;
        ComandoActualizarPaciente comando = ComandoActualizarPacienteTestDataBuilder.unComandoActualizarPacienteValido()
                .conNombre("Laura")
                .conApellido("Torres")
                .conCorreoElectronico("laura@gmail.com")
                .conFechaNacimiento(LocalDate.of(1998, 7, 20))
                .build();

        // Act
        manejadorActualizarPaciente.ejecutar(numeroDocumento, comando);

        // Assert
        ArgumentCaptor<DatosActualizarPaciente> captor = ArgumentCaptor.forClass(DatosActualizarPaciente.class);
        verify(servicioActualizarPaciente, times(1)).ejecutar(eq(numeroDocumento), captor.capture());
        assertEquals("Laura", captor.getValue().nombre());
        assertEquals("Torres", captor.getValue().apellido());
        assertEquals("laura@gmail.com", captor.getValue().correoElectronico());
        assertEquals(LocalDate.of(1998, 7, 20), captor.getValue().fechaNacimiento());
    }

    @Test
    void deberia_LanzarExcepcionSinDatos_Cuando_PacienteNoExiste() {
        // Arrange
        Long numeroDocumento = 99999L;
        ComandoActualizarPaciente comando = ComandoActualizarPacienteTestDataBuilder.unComandoActualizarPacienteValido()
                .conNombre("Nombre")
                .conApellido("Apellido")
                .conFechaNacimiento(null)
                .build();

        doThrow(new ExcepcionSinDatos("No existe el paciente que desea actualizar"))
                .when(servicioActualizarPaciente)
                .ejecutar(eq(numeroDocumento), any(DatosActualizarPaciente.class));

        // Act & Assert
        assertThrows(ExcepcionSinDatos.class, () -> manejadorActualizarPaciente.ejecutar(numeroDocumento, comando));
        verify(servicioActualizarPaciente, times(1)).ejecutar(eq(numeroDocumento), any(DatosActualizarPaciente.class));
    }
}
