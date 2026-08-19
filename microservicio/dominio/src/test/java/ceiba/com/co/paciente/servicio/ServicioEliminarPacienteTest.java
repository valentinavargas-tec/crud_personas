package ceiba.com.co.paciente.servicio;

import ceiba.com.co.excepcion.ExcepcionSinDatos;
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
public class ServicioEliminarPacienteTest {

    @Mock
    private RepositorioPaciente repositorioPaciente;
    private ServicioEliminarPaciente servicioEliminarPaciente;

    @BeforeEach
    public void setUp() {
        servicioEliminarPaciente = new ServicioEliminarPaciente(repositorioPaciente);
    }

    @Test
    void deberia_EliminarPaciente_Cuando_NumeroDocumentoExiste() {
        // Arrange
        Long numeroDocumento = 123456789L;
        when(repositorioPaciente.existeConNumeroDocumento(numeroDocumento)).thenReturn(true);

        // Act
        servicioEliminarPaciente.ejecutar(numeroDocumento);

        // Assert
        verify(repositorioPaciente).existeConNumeroDocumento(numeroDocumento);
        verify(repositorioPaciente).eliminar(numeroDocumento);
    }

    @Test
    void deberia_LanzarExcepcionSinDatos_Cuando_NumeroDocumentoNoExiste() {
        // Arrange
        Long numeroDocumento = 999999L;
        when(repositorioPaciente.existeConNumeroDocumento(numeroDocumento)).thenReturn(false);

        // Act & Assert
        ExcepcionSinDatos excepcion = assertThrows(
                ExcepcionSinDatos.class,
                () -> servicioEliminarPaciente.ejecutar(numeroDocumento)
        );

        assertEquals("No existe un paciente con el número de documento ingresado: " + numeroDocumento, excepcion.getMessage());
        verify(repositorioPaciente).existeConNumeroDocumento(numeroDocumento);
        verify(repositorioPaciente, never()).eliminar(anyLong());
    }
}
