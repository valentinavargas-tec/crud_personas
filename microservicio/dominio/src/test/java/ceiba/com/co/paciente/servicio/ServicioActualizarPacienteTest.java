package ceiba.com.co.paciente.servicio;

import ceiba.com.co.excepcion.ExcepcionDuplicidad;
import ceiba.com.co.excepcion.ExcepcionSinDatos;
import ceiba.com.co.excepcion.ExcepcionValorInvalido;
import ceiba.com.co.excepcion.ExcepcionValorObligatorio;
import ceiba.com.co.paciente.modelo.entidad.Genero;
import ceiba.com.co.paciente.modelo.entidad.Paciente;
import ceiba.com.co.paciente.modelo.entidad.PacienteTestDataBuilder;
import ceiba.com.co.paciente.puerto.repositorio.RepositorioPaciente;
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
public class ServicioActualizarPacienteTest {

    @Mock
    private RepositorioPaciente repositorioPaciente;
    private ServicioActualizarPaciente servicioActualizarPaciente;

    @BeforeEach
    public void setUp() {
        servicioActualizarPaciente = new ServicioActualizarPaciente(repositorioPaciente);
    }

    @Test
    void deberia_ActualizarPaciente_Cuando_PacienteExisteYCorreoElectronicoCambia() {
        // Arrange
        Paciente pacienteExistente = PacienteTestDataBuilder.unPacienteValida().build();
        Long numeroDocumento = pacienteExistente.getNumeroDocumento();
        String nuevoCorreoElectronico = "nuevo_email@example.com";

        when(repositorioPaciente.obtener(numeroDocumento)).thenReturn(pacienteExistente);
        when(repositorioPaciente.existeConCorreoElectronico(nuevoCorreoElectronico)).thenReturn(false);

        // Act
        servicioActualizarPaciente.ejecutar(numeroDocumento, "NuevoNombre", "NuevoApellido", LocalDate.of(1995, 1, 1), "3001234567", nuevoCorreoElectronico, "EPS Sura", Genero.MASCULINO);

        // Assert
        verify(repositorioPaciente).obtener(numeroDocumento);
        verify(repositorioPaciente).existeConCorreoElectronico(nuevoCorreoElectronico);
        verify(repositorioPaciente).actualizar(any(Paciente.class));
    }

    @Test
    void deberia_ActualizarPaciente_Cuando_CorreoElectronicoSeMantieneIgual() {
        // Arrange
        Paciente pacienteExistente = PacienteTestDataBuilder.unPacienteValida().build();
        Long numeroDocumento = pacienteExistente.getNumeroDocumento();

        when(repositorioPaciente.obtener(numeroDocumento)).thenReturn(pacienteExistente);

        // Act
        servicioActualizarPaciente.ejecutar(numeroDocumento, "NuevoNombre", "NuevoApellido", LocalDate.of(1995, 1, 1), pacienteExistente.getTelefono(), pacienteExistente.getCorreoElectronico(), pacienteExistente.getEps(), pacienteExistente.getGenero());

        // Assert
        verify(repositorioPaciente).obtener(numeroDocumento);
        verify(repositorioPaciente, never()).existeConCorreoElectronico(anyString());
        verify(repositorioPaciente).actualizar(any(Paciente.class));
    }

    @Test
    void deberia_LanzarExcepcionSinDatos_Cuando_PacienteNoExiste() {
        // Arrange
        Long numeroDocumento = 999L;
        when(repositorioPaciente.obtener(numeroDocumento)).thenReturn(null);

        // Act & Assert
        ExcepcionSinDatos excepcion = assertThrows(
                ExcepcionSinDatos.class,
                () -> servicioActualizarPaciente.ejecutar(numeroDocumento, "Pedro", "Perez", null, "3001234567", "pedro@example.com", "EPS Sura", Genero.MASCULINO)
        );

        assertEquals("No existe el paciente que desea actualizar", excepcion.getMessage());
        verify(repositorioPaciente).obtener(numeroDocumento);
        verify(repositorioPaciente, never()).actualizar(any());
    }

    @Test
    void deberia_LanzarExcepcionDuplicidad_Cuando_NuevoCorreoElectronicoYaExiste() {
        // Arrange
        Paciente pacienteExistente = PacienteTestDataBuilder.unPacienteValida().build();
        Long numeroDocumento = pacienteExistente.getNumeroDocumento();
        String emailDuplicado = "duplicado@example.com";

        when(repositorioPaciente.obtener(numeroDocumento)).thenReturn(pacienteExistente);
        when(repositorioPaciente.existeConCorreoElectronico(emailDuplicado)).thenReturn(true);

        // Act & Assert
        ExcepcionDuplicidad excepcion = assertThrows(
                ExcepcionDuplicidad.class,
                () -> servicioActualizarPaciente.ejecutar(numeroDocumento, "Pedro", "Perez", null, "3001234567", emailDuplicado, "EPS Sura", Genero.MASCULINO)
        );

        assertEquals("El correo electrónico ya está registrado: " + emailDuplicado, excepcion.getMessage());
        verify(repositorioPaciente).obtener(numeroDocumento);
        verify(repositorioPaciente).existeConCorreoElectronico(emailDuplicado);
        verify(repositorioPaciente, never()).actualizar(any());
    }

    @Test
    void deberia_LanzarExcepcionValorObligatorio_Cuando_NombreEsNuloEnActualizacion() {
        // Arrange
        Paciente pacienteExistente = PacienteTestDataBuilder.unPacienteValida().build();
        Long numeroDocumento = pacienteExistente.getNumeroDocumento();

        when(repositorioPaciente.obtener(numeroDocumento)).thenReturn(pacienteExistente);

        // Act & Assert
        assertThrows(ExcepcionValorObligatorio.class,
                () -> servicioActualizarPaciente.ejecutar(numeroDocumento, null, "Perez", null, "3001234567", "juan@test.com", "EPS Sura", Genero.MASCULINO));

        verify(repositorioPaciente, never()).actualizar(any());
    }

    @Test
    void deberia_LanzarExcepcionValorInvalido_Cuando_NombreContieneCaracteresInvalidos() {
        // Arrange
        Paciente pacienteExistente = PacienteTestDataBuilder.unPacienteValida().build();
        Long numeroDocumento = pacienteExistente.getNumeroDocumento();

        when(repositorioPaciente.obtener(numeroDocumento)).thenReturn(pacienteExistente);

        // Act & Assert
        assertThrows(ExcepcionValorInvalido.class,
                () -> servicioActualizarPaciente.ejecutar(numeroDocumento, "Juan123", "Perez", null, "3001234567", "juan@test.com", "EPS Sura", Genero.MASCULINO));

        verify(repositorioPaciente, never()).actualizar(any());
    }
}
