package ceiba.com.co.paciente.comando.manejador;

import ceiba.com.co.ComandoRespuesta;
import ceiba.com.co.paciente.comando.ComandoPaciente;
import ceiba.com.co.paciente.comando.ComandoPacienteTestDataBuilder;
import ceiba.com.co.paciente.comando.fabrica.FabricaPaciente;
import ceiba.com.co.excepcion.ExcepcionDuplicidad;
import ceiba.com.co.paciente.modelo.entidad.Email;
import ceiba.com.co.paciente.modelo.entidad.Genero;
import ceiba.com.co.paciente.modelo.entidad.Nombre;
import ceiba.com.co.paciente.modelo.entidad.Paciente;
import ceiba.com.co.paciente.modelo.entidad.TipoDocumento;
import ceiba.com.co.paciente.servicio.ServicioCrearPaciente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ManejadorCrearPacienteTest {

    private FabricaPaciente fabricaPaciente;
    private ServicioCrearPaciente servicioCrearPaciente;
    private ManejadorCrearPaciente manejadorCrearPaciente;

    @BeforeEach
    void setUp() {
        fabricaPaciente = mock(FabricaPaciente.class);
        servicioCrearPaciente = mock(ServicioCrearPaciente.class);
        manejadorCrearPaciente = new ManejadorCrearPaciente(fabricaPaciente, servicioCrearPaciente);
    }

    @Test
    @DisplayName("Debería ejecutar el manejador para crear paciente")
    void deberia_CrearPaciente_Cuando_ComandoEsValido() {
        // Arrange
        ComandoPaciente comando = ComandoPacienteTestDataBuilder.unComandoPacienteValido()
                .conNumeroDocumento(123L)
                .conNombre("Ana")
                .conApellido("Ruiz")
                .conCorreoElectronico("ana@example.com")
                .conFechaNacimiento(LocalDate.of(1995, 4, 12))
                .build();
        Paciente paciente = Paciente.builder()
                .conNumeroDocumento(123L)
                .conTipoDocumento(TipoDocumento.CC)
                .conNombre(new Nombre("Ana", "El nombre es obligatorio", "El nombre contiene caracteres no permitidos"))
                .conApellido(new Nombre("Ruiz", "El apellido es obligatorio", "El apellido contiene caracteres no permitidos"))
                .conFechaNacimiento(LocalDate.of(1995, 4, 12))
                .conTelefono("3001234567")
                .conCorreoElectronico(new Email("ana@example.com"))
                .conEps("Sura")
                .conGenero(Genero.FEMENINO)
                .build();

        when(fabricaPaciente.crear(comando)).thenReturn(paciente);
        when(servicioCrearPaciente.ejecutar(paciente)).thenReturn(123L);

        // Act
        ComandoRespuesta<Long> respuesta = manejadorCrearPaciente.ejecutar(comando);

        // Assert
        assertNotNull(respuesta);
        assertEquals(123L, respuesta.getValor());
        verify(servicioCrearPaciente, times(1)).ejecutar(paciente);
    }

    @Test
    void deberia_LanzarExcepcionDuplicidad_Cuando_ServicioFallaPorDuplicado() {
        // Arrange
        ComandoPaciente comando = ComandoPacienteTestDataBuilder.unComandoPacienteValido()
                .conNumeroDocumento(123L)
                .conNombre("Ana")
                .conApellido("Ruiz")
                .conCorreoElectronico("ana@example.com")
                .conFechaNacimiento(LocalDate.of(1995, 4, 12))
                .build();
        Paciente paciente = Paciente.builder()
                .conNumeroDocumento(123L)
                .conTipoDocumento(TipoDocumento.CC)
                .conNombre(new Nombre("Ana", "El nombre es obligatorio", "El nombre contiene caracteres no permitidos"))
                .conApellido(new Nombre("Ruiz", "El apellido es obligatorio", "El apellido contiene caracteres no permitidos"))
                .conFechaNacimiento(LocalDate.of(1995, 4, 12))
                .conTelefono("3001234567")
                .conCorreoElectronico(new Email("ana@example.com"))
                .conEps("Sura")
                .conGenero(Genero.FEMENINO)
                .build();

        when(fabricaPaciente.crear(comando)).thenReturn(paciente);
        when(servicioCrearPaciente.ejecutar(paciente)).thenThrow(new ExcepcionDuplicidad("El número de documento ya existe"));

        // Act & Assert
        assertThrows(ExcepcionDuplicidad.class, () -> manejadorCrearPaciente.ejecutar(comando));
        verify(servicioCrearPaciente, times(1)).ejecutar(paciente);
    }
}
