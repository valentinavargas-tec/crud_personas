package ceiba.com.co.comando.manejador;

import ceiba.com.co.ComandoRespuesta;
import ceiba.com.co.comando.ComandoPersona;
import ceiba.com.co.comando.ComandoPersonaTestDataBuilder;
import ceiba.com.co.comando.fabrica.FabricaPersona;
import ceiba.com.co.excepcion.ExcepcionDuplicidad;
import ceiba.com.co.modelo.entidad.Persona;
import ceiba.com.co.servicio.ServicioCrearPersona;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ManejadorCrearPersonaTest {

    private FabricaPersona fabricaPersona;
    private ServicioCrearPersona servicioCrearPersona;
    private ManejadorCrearPersona manejadorCrearPersona;

    @BeforeEach
    void setUp() {
        fabricaPersona = mock(FabricaPersona.class);
        servicioCrearPersona = mock(ServicioCrearPersona.class);
        manejadorCrearPersona = new ManejadorCrearPersona(fabricaPersona, servicioCrearPersona);
    }

    @Test
    @DisplayName("Debería ejecutar el manejador para crear persona")
    void deberia_CrearPersona_Cuando_ComandoEsValido() {
        // Arrange
        ComandoPersona comando = ComandoPersonaTestDataBuilder.unComandoPersonaValido()
                .conCedula(123L)
                .conNombre("Ana")
                .conApellido("Ruiz")
                .conEmail("ana@example.com")
                .conFechaNacimiento(LocalDate.of(1995, 4, 12))
                .build();
        Persona persona = new Persona(123L, "Ana", "Ruiz", "ana@example.com", LocalDate.of(1995, 4, 12));

        when(fabricaPersona.crear(comando)).thenReturn(persona);
        when(servicioCrearPersona.ejecutar(persona)).thenReturn(123L);

        // Act
        ComandoRespuesta<Long> respuesta = manejadorCrearPersona.ejecutar(comando);

        // Assert
        assertNotNull(respuesta);
        assertEquals(123L, respuesta.getValor());
        verify(servicioCrearPersona, times(1)).ejecutar(persona);
    }

    @Test
    void deberia_LanzarExcepcionDuplicidad_Cuando_ServicioFallaPorDuplicado() {
        // Arrange
        ComandoPersona comando = ComandoPersonaTestDataBuilder.unComandoPersonaValido()
                .conCedula(123L)
                .conNombre("Ana")
                .conApellido("Ruiz")
                .conEmail("ana@example.com")
                .conFechaNacimiento(LocalDate.of(1995, 4, 12))
                .build();
        Persona persona = new Persona(123L, "Ana", "Ruiz", "ana@example.com", LocalDate.of(1995, 4, 12));

        when(fabricaPersona.crear(comando)).thenReturn(persona);
        when(servicioCrearPersona.ejecutar(persona)).thenThrow(new ExcepcionDuplicidad("La cédula ya existe"));

        // Act & Assert
        assertThrows(ExcepcionDuplicidad.class, () -> manejadorCrearPersona.ejecutar(comando));
        verify(servicioCrearPersona, times(1)).ejecutar(persona);
    }
}
