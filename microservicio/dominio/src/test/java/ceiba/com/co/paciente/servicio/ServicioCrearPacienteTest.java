package ceiba.com.co.paciente.servicio;

import ceiba.com.co.excepcion.ExcepcionDuplicidad;
import ceiba.com.co.paciente.modelo.entidad.Paciente;
import ceiba.com.co.paciente.modelo.entidad.PacienteTestDataBuilder;
import ceiba.com.co.paciente.puerto.repositorio.RepositorioPaciente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServicioCrearPacienteTest {

    @Mock
    private RepositorioPaciente repositorioPaciente;
    private ServicioCrearPaciente servicioCrearPaciente;

    @BeforeEach
    void setUp() {
        servicioCrearPaciente = new ServicioCrearPaciente(repositorioPaciente);
    }

    @Test
    void deberia_RetornarNumeroDocumento_Cuando_PacienteEsCreadoExitosamente(){
        // Arrange
        Paciente paciente = PacienteTestDataBuilder.unPacienteValida().build();
        when(repositorioPaciente.existeConNumeroDocumento(paciente.getNumeroDocumento())).thenReturn(false);
        when(repositorioPaciente.existeConCorreoElectronico(paciente.getCorreoElectronico())).thenReturn(false);
        when(repositorioPaciente.guardar(paciente)).thenReturn(paciente.getNumeroDocumento());

        // Act
        Long resultado = servicioCrearPaciente.ejecutar(paciente);

        // Assert
        assertEquals(paciente.getNumeroDocumento(), resultado);
        verify(repositorioPaciente).existeConNumeroDocumento(paciente.getNumeroDocumento());
        verify(repositorioPaciente).existeConCorreoElectronico(paciente.getCorreoElectronico());
        verify(repositorioPaciente).guardar(paciente);
    }

    @Test
    void deberia_LanzarExcepcionDuplicidad_Cuando_NumeroDocumentoYaExiste(){
        // Arrange
        Paciente paciente = PacienteTestDataBuilder.unPacienteValida().build();
        when(repositorioPaciente.existeConNumeroDocumento(paciente.getNumeroDocumento())).thenReturn(true);

        // Act & Assert
        ExcepcionDuplicidad excepcion = assertThrows(
                ExcepcionDuplicidad.class,
                () -> servicioCrearPaciente.ejecutar(paciente)
        );

        assertEquals("El número de documento ya está registrado: " + paciente.getNumeroDocumento(), excepcion.getMessage());
        verify(repositorioPaciente).existeConNumeroDocumento(paciente.getNumeroDocumento());
        verify(repositorioPaciente, never()).existeConCorreoElectronico(anyString());
        verify(repositorioPaciente, never()).guardar(any());
    }

    @Test
    void deberia_LanzarExcepcionDuplicidad_Cuando_CorreoElectronicoYaExiste(){
        // Arrange
        Paciente paciente = PacienteTestDataBuilder.unPacienteValida().build();
        when(repositorioPaciente.existeConNumeroDocumento(paciente.getNumeroDocumento())).thenReturn(false);
        when(repositorioPaciente.existeConCorreoElectronico(paciente.getCorreoElectronico())).thenReturn(true);

        // Act & Assert
        ExcepcionDuplicidad excepcion = assertThrows(
                ExcepcionDuplicidad.class,
                () -> servicioCrearPaciente.ejecutar(paciente)
        );

        assertEquals("El correo electrónico ya está registrado: " + paciente.getCorreoElectronico(), excepcion.getMessage());
        verify(repositorioPaciente).existeConNumeroDocumento(paciente.getNumeroDocumento());
        verify(repositorioPaciente).existeConCorreoElectronico(paciente.getCorreoElectronico());
        verify(repositorioPaciente, never()).guardar(any());
    }

    @Test
    void deberia_RetornarNumeroDocumento_Cuando_PacienteTieneFechaNacimientoNula() {
        // Arrange
        Paciente paciente = PacienteTestDataBuilder.unPacienteValida().fechaNacimiento(null).build();
        when(repositorioPaciente.existeConNumeroDocumento(paciente.getNumeroDocumento())).thenReturn(false);
        when(repositorioPaciente.existeConCorreoElectronico(paciente.getCorreoElectronico())).thenReturn(false);
        when(repositorioPaciente.guardar(paciente)).thenReturn(paciente.getNumeroDocumento());

        // Act
        Long resultado = servicioCrearPaciente.ejecutar(paciente);

        // Assert
        assertEquals(paciente.getNumeroDocumento(), resultado);
        verify(repositorioPaciente).guardar(paciente);
    }
}
