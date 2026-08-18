package ceiba.com.co.comando.manejador;

import ceiba.com.co.comando.ComandoActualizarPersona;
import ceiba.com.co.comando.ComandoActualizarPersonaTestDataBuilder;
import ceiba.com.co.excepcion.ExcepcionSinDatos;
import ceiba.com.co.servicio.ServicioActualizarPersona;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ManejadorActualizarPersonaTest {

    private ServicioActualizarPersona servicioActualizarPersona;
    private ManejadorActualizarPersona manejadorActualizarPersona;

    @BeforeEach
    void setUp() {
        servicioActualizarPersona = mock(ServicioActualizarPersona.class);
        manejadorActualizarPersona = new ManejadorActualizarPersona(servicioActualizarPersona);
    }

    @Test
    @DisplayName("Debería ejecutar el manejador para actualizar persona")
    void deberia_ActualizarPersona_Cuando_ComandoEsValido() {
        // Arrange
        Long cedula = 12345L;
        ComandoActualizarPersona comando = ComandoActualizarPersonaTestDataBuilder.unComandoActualizarPersonaValido()
                .conNombre("Laura")
                .conApellido("Torres")
                .conEmail("laura@gmail.com")
                .conFechaNacimiento(LocalDate.of(1998, 7, 20))
                .build();

        // Act
        manejadorActualizarPersona.ejecutar(cedula, comando);

        // Assert
        verify(servicioActualizarPersona, times(1)).ejecutar(
                cedula,
                "Laura",
                "Torres",
                "laura@gmail.com",
                LocalDate.of(1998, 7, 20)
        );
    }

    @Test
    void deberia_LanzarExcepcionSinDatos_Cuando_PersonaNoExiste() {
        // Arrange
        Long cedula = 99999L;
        ComandoActualizarPersona comando = ComandoActualizarPersonaTestDataBuilder.unComandoActualizarPersonaValido()
                .conNombre("Nombre")
                .conApellido("Apellido")
                .conEmail("test@test.com")
                .conFechaNacimiento(null)
                .build();

        doThrow(new ExcepcionSinDatos("No existe la persona que desea actualizar"))
                .when(servicioActualizarPersona)
                .ejecutar(eq(cedula), any(), any(), any(), any());

        // Act & Assert
        assertThrows(ExcepcionSinDatos.class, () -> manejadorActualizarPersona.ejecutar(cedula, comando));
        verify(servicioActualizarPersona, times(1)).ejecutar(eq(cedula), any(), any(), any(), any());
    }
}
