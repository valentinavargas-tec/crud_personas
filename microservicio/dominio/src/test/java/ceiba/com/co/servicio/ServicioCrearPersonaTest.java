package ceiba.com.co.servicio;

import ceiba.com.co.excepcion.ExcepcionDuplicidad;
import ceiba.com.co.modelo.entidad.Persona;
import ceiba.com.co.modelo.entidad.PersonaTestDataBuilder;
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
public class ServicioCrearPersonaTest {

    @Mock
    private RepositorioPersona repositorioPersona;
    private ServicioCrearPersona servicioCrearPersona;

    @BeforeEach
    void setUp() {
        servicioCrearPersona = new ServicioCrearPersona(repositorioPersona);
    }

    @Test
    void deberia_RetornarCedula_Cuando_PersonaEsCreadaExitosamente(){
        // Arrange
        Persona persona = PersonaTestDataBuilder.unPersonaValida().build();
        when(repositorioPersona.existeConCedula(persona.getCedula())).thenReturn(false);
        when(repositorioPersona.existeConEmail(persona.getEmail())).thenReturn(false);
        when(repositorioPersona.guardar(persona)).thenReturn(persona.getCedula());

        // Act
        Long resultado = servicioCrearPersona.ejecutar(persona);

        // Assert
        assertEquals(persona.getCedula(), resultado);
        verify(repositorioPersona).existeConCedula(persona.getCedula());
        verify(repositorioPersona).existeConEmail(persona.getEmail());
        verify(repositorioPersona).guardar(persona);
    }

    @Test
    void deberia_LanzarExcepcionDuplicidad_Cuando_CedulaYaExiste(){
        // Arrange
        Persona persona = PersonaTestDataBuilder.unPersonaValida().build();
        when(repositorioPersona.existeConCedula(persona.getCedula())).thenReturn(true);

        // Act & Assert
        ExcepcionDuplicidad excepcion = assertThrows(
                ExcepcionDuplicidad.class,
                () -> servicioCrearPersona.ejecutar(persona)
        );

        assertEquals("La cédula ya está registrada: " + persona.getCedula(), excepcion.getMessage());
        verify(repositorioPersona).existeConCedula(persona.getCedula());
        verify(repositorioPersona, never()).existeConEmail(anyString());
        verify(repositorioPersona, never()).guardar(any());
    }

    @Test
    void deberia_LanzarExcepcionDuplicidad_Cuando_EmailYaExiste(){
        // Arrange
        Persona persona = PersonaTestDataBuilder.unPersonaValida().build();
        when(repositorioPersona.existeConCedula(persona.getCedula())).thenReturn(false);
        when(repositorioPersona.existeConEmail(persona.getEmail())).thenReturn(true);

        // Act & Assert
        ExcepcionDuplicidad excepcion = assertThrows(
                ExcepcionDuplicidad.class,
                () -> servicioCrearPersona.ejecutar(persona)
        );

        assertEquals("El email ya está registrado: " + persona.getEmail(), excepcion.getMessage());
        verify(repositorioPersona).existeConCedula(persona.getCedula());
        verify(repositorioPersona).existeConEmail(persona.getEmail());
        verify(repositorioPersona, never()).guardar(any());
    }

    @Test
    void deberia_RetornarCedula_Cuando_PersonaTieneFechaNacimientoNula() {
        // Arrange
        Persona persona = PersonaTestDataBuilder.unPersonaValida().fechaNacimiento(null).build();
        when(repositorioPersona.existeConCedula(persona.getCedula())).thenReturn(false);
        when(repositorioPersona.existeConEmail(persona.getEmail())).thenReturn(false);
        when(repositorioPersona.guardar(persona)).thenReturn(persona.getCedula());

        // Act
        Long resultado = servicioCrearPersona.ejecutar(persona);

        // Assert
        assertEquals(persona.getCedula(), resultado);
        verify(repositorioPersona).guardar(persona);
    }
}
