package ceiba.com.co.servicio;

import ceiba.com.co.excepcion.ExcepcionDuplicidad;
import ceiba.com.co.excepcion.ExcepcionSinDatos;
import ceiba.com.co.excepcion.ExcepcionValorInvalido;
import ceiba.com.co.excepcion.ExcepcionValorObligatorio;
import ceiba.com.co.modelo.entidad.Persona;
import ceiba.com.co.modelo.entidad.PersonaTestDataBuilder;
import ceiba.com.co.puerto.repositorio.RepositorioPersona;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServicioActualizarPersonaTest {

    @Mock
    private RepositorioPersona repositorioPersona;
    private ServicioActualizarPersona servicioActualizarPersona;

    @BeforeEach
    public void setUp() {
        servicioActualizarPersona = new ServicioActualizarPersona(repositorioPersona);
    }

    @Test
    void deberia_ActualizarPersona_Cuando_PersonaExisteYEmailCambia() {
        // Arrange
        Persona personaExistente = PersonaTestDataBuilder.unPersonaValida().build();
        Long cedula = personaExistente.getCedula();
        String nuevoEmail = "nuevo_email@example.com";

        when(repositorioPersona.obtener(cedula)).thenReturn(personaExistente);
        when(repositorioPersona.existeConEmail(nuevoEmail)).thenReturn(false);

        // Act
        servicioActualizarPersona.ejecutar(cedula, "NuevoNombre", "NuevoApellido", nuevoEmail, LocalDate.of(1995, 1, 1));

        // Assert
        verify(repositorioPersona).obtener(cedula);
        verify(repositorioPersona).existeConEmail(nuevoEmail);
        verify(repositorioPersona).actualizar(any(Persona.class));
    }

    @Test
    void deberia_ActualizarPersona_Cuando_EmailSeMantieneIgual() {
        // Arrange
        Persona personaExistente = PersonaTestDataBuilder.unPersonaValida().build();
        Long cedula = personaExistente.getCedula();

        when(repositorioPersona.obtener(cedula)).thenReturn(personaExistente);

        // Act
        servicioActualizarPersona.ejecutar(cedula, "NuevoNombre", "NuevoApellido", personaExistente.getEmail(), LocalDate.of(1995, 1, 1));

        // Assert
        verify(repositorioPersona).obtener(cedula);
        verify(repositorioPersona, never()).existeConEmail(anyString());
        verify(repositorioPersona).actualizar(any(Persona.class));
    }

    @Test
    void deberia_LanzarExcepcionSinDatos_Cuando_PersonaNoExiste() {
        // Arrange
        Long cedula = 999L;
        when(repositorioPersona.obtener(cedula)).thenReturn(null);

        // Act & Assert
        ExcepcionSinDatos excepcion = assertThrows(
                ExcepcionSinDatos.class,
                () -> servicioActualizarPersona.ejecutar(cedula, "Pedro", "Perez", "pedro@example.com", null)
        );

        assertEquals("No existe la persona que desea actualizar", excepcion.getMessage());
        verify(repositorioPersona).obtener(cedula);
        verify(repositorioPersona, never()).actualizar(any());
    }

    @Test
    void deberia_LanzarExcepcionDuplicidad_Cuando_NuevoEmailYaExiste() {
        // Arrange
        Persona personaExistente = PersonaTestDataBuilder.unPersonaValida().build();
        Long cedula = personaExistente.getCedula();
        String emailDuplicado = "duplicado@example.com";

        when(repositorioPersona.obtener(cedula)).thenReturn(personaExistente);
        when(repositorioPersona.existeConEmail(emailDuplicado)).thenReturn(true);

        // Act & Assert
        ExcepcionDuplicidad excepcion = assertThrows(
                ExcepcionDuplicidad.class,
                () -> servicioActualizarPersona.ejecutar(cedula, "Pedro", "Perez", emailDuplicado, null)
        );

        assertEquals("El email ya está registrado: " + emailDuplicado, excepcion.getMessage());
        verify(repositorioPersona).obtener(cedula);
        verify(repositorioPersona).existeConEmail(emailDuplicado);
        verify(repositorioPersona, never()).actualizar(any());
    }

    @Test
    void deberia_LanzarExcepcionValorObligatorio_Cuando_NombreEsNuloEnActualizacion() {
        // Arrange
        Persona personaExistente = PersonaTestDataBuilder.unPersonaValida().build();
        Long cedula = personaExistente.getCedula();

        when(repositorioPersona.obtener(cedula)).thenReturn(personaExistente);

        // Act & Assert
        assertThrows(ExcepcionValorObligatorio.class,
                () -> servicioActualizarPersona.ejecutar(cedula, null, "Perez", "juan@test.com", null));

        verify(repositorioPersona, never()).actualizar(any());
    }

    @Test
    void deberia_LanzarExcepcionValorInvalido_Cuando_NombreContieneCaracteresInvalidos() {
        // Arrange
        Persona personaExistente = PersonaTestDataBuilder.unPersonaValida().build();
        Long cedula = personaExistente.getCedula();

        when(repositorioPersona.obtener(cedula)).thenReturn(personaExistente);

        // Act & Assert
        assertThrows(ExcepcionValorInvalido.class,
                () -> servicioActualizarPersona.ejecutar(cedula, "Juan123", "Perez", "juan@test.com", null));

        verify(repositorioPersona, never()).actualizar(any());
    }
}
