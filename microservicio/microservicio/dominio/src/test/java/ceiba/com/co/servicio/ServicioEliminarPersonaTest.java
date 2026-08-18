package ceiba.com.co.servicio;

import ceiba.com.co.excepcion.ExcepcionSinDatos;
import ceiba.com.co.puerto.repositorio.RepositorioPersona;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServicioEliminarPersonaTest {

    @Mock
    private RepositorioPersona repositorioPersona;
    private ServicioEliminarPersona servicioEliminarPersona;

    @BeforeEach
    public void setUp() {
        servicioEliminarPersona = new ServicioEliminarPersona(repositorioPersona);
    }

    @Test
    void deberia_EliminarPersona_Cuando_CedulaExiste() {
        // Arrange
        Long cedula = 123456789L;
        when(repositorioPersona.existeConCedula(cedula)).thenReturn(true);

        // Act
        servicioEliminarPersona.ejecutar(cedula);

        // Assert
        verify(repositorioPersona).existeConCedula(cedula);
        verify(repositorioPersona).eliminar(cedula);
    }

    @Test
    void deberia_LanzarExcepcionSinDatos_Cuando_CedulaNoExiste() {
        // Arrange
        Long cedula = 999999L;
        when(repositorioPersona.existeConCedula(cedula)).thenReturn(false);

        // Act & Assert
        ExcepcionSinDatos excepcion = assertThrows(
                ExcepcionSinDatos.class,
                () -> servicioEliminarPersona.ejecutar(cedula)
        );

        assertEquals("No existe una persona con la cédula ingresada: " + cedula, excepcion.getMessage());
        verify(repositorioPersona).existeConCedula(cedula);
        verify(repositorioPersona, never()).eliminar(anyLong());
    }
}
