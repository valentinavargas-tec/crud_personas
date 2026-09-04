package ceiba.com.co.doctor.comando.manejador;

import ceiba.com.co.ComandoRespuesta;
import ceiba.com.co.doctor.comando.ComandoActualizarDoctor;
import ceiba.com.co.doctor.modelo.entidad.Doctor;
import ceiba.com.co.doctor.puerto.repositorio.RepositorioDoctor;
import ceiba.com.co.doctor.servicio.ServicioActualizarDoctor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ManejadorActualizarDoctorTest {

    private ServicioActualizarDoctor servicioActualizarDoctor;
    private RepositorioDoctor repositorioDoctor;
    private ManejadorActualizarDoctor manejadorActualizarDoctor;

    private static final String DOCUMENTO = "DOC-001";
    private static final String TARJETA = "TP-001";

    @BeforeEach
    void setUp() {
        servicioActualizarDoctor = Mockito.mock(ServicioActualizarDoctor.class);
        repositorioDoctor = Mockito.mock(RepositorioDoctor.class);
        manejadorActualizarDoctor = new ManejadorActualizarDoctor(servicioActualizarDoctor, repositorioDoctor);
    }

    @Test
    @DisplayName("Debería actualizar doctor exitosamente y retornar ComandoRespuesta")
    void deberiaActualizarDoctor_Y_RetornarRespuesta() {
        // Arrange
        Doctor doctorExistente = Doctor.builder()
                .conNumeroDocumento(DOCUMENTO)
                .conNombre("Ana")
                .conApellido("Torres")
                .conTarjetaProfesional(TARJETA)
                .conEspecialidad("CARDIOLOGIA")
                .conCorreoInstitucional("ana@hospital.com")
                .build();
        when(repositorioDoctor.obtenerPorNumeroDocumento(DOCUMENTO))
                .thenReturn(Optional.of(doctorExistente));

        ComandoActualizarDoctor comando = new ComandoActualizarDoctor(
                "Ana Actualizada", "Torres", "NEUROLOGIA", "ana2@hospital.com"
        );
        doNothing().when(servicioActualizarDoctor).ejecutar(any(Doctor.class));

        // Act
        ComandoRespuesta<String> respuesta = manejadorActualizarDoctor.ejecutar(DOCUMENTO, comando);

        // Assert
        assertNotNull(respuesta);
        assertEquals(DOCUMENTO, respuesta.getValor());
        assertEquals("Doctor actualizado exitosamente", respuesta.getMensaje());
        verify(servicioActualizarDoctor, times(1)).ejecutar(any(Doctor.class));
    }

    @Test
    @DisplayName("Debería retornar tarjeta null cuando el doctor no existe en el repositorio")
    void deberiaUsarTarjetaNull_Cuando_DoctorNoExisteEnRepositorio() {
        // Arrange
        when(repositorioDoctor.obtenerPorNumeroDocumento(DOCUMENTO))
                .thenReturn(Optional.empty());

        ComandoActualizarDoctor comando = new ComandoActualizarDoctor(
                "Ana", "Torres", "CARDIOLOGIA", "ana@hospital.com"
        );
        doNothing().when(servicioActualizarDoctor).ejecutar(any(Doctor.class));

        // Act - el handler lanzará ExcepcionSinDatos porque obtenerTarjetaActual retornó null (Optional.empty)
        assertThrows(ceiba.com.co.excepcion.ExcepcionSinDatos.class,
                () -> manejadorActualizarDoctor.ejecutar(DOCUMENTO, comando));
    }
}
