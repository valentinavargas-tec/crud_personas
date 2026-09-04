package ceiba.com.co.doctor.servicio;

import ceiba.com.co.doctor.modelo.entidad.Doctor;
import ceiba.com.co.doctor.puerto.repositorio.RepositorioDoctor;
import ceiba.com.co.excepcion.ExcepcionDuplicidad;
import ceiba.com.co.excepcion.ExcepcionSinDatos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ServicioActualizarDoctorTest {

    private RepositorioDoctor repositorioDoctor;
    private ServicioActualizarDoctor servicioActualizarDoctor;

    private static final String DOCUMENTO = "123456789";
    private static final String CORREO = "ana.torres@hospital.com";
    private static final String OTRO_CORREO = "otro@hospital.com";

    @BeforeEach
    void setUp() {
        repositorioDoctor = Mockito.mock(RepositorioDoctor.class);
        servicioActualizarDoctor = new ServicioActualizarDoctor(repositorioDoctor);
    }

    private Doctor construirDoctor(String documento, String correo) {
        return Doctor.builder()
                .conNumeroDocumento(documento)
                .conNombre("Ana")
                .conApellido("Torres")
                .conTarjetaProfesional("TP-001")
                .conEspecialidad("CARDIOLOGIA")
                .conCorreoInstitucional(correo)
                .build();
    }

    @Test
    @DisplayName("Debería actualizar doctor exitosamente cuando existe y el correo no está en uso")
    void deberiaActualizarDoctor_Cuando_ExisteYCorreoDisponible() {
        // Arrange
        Doctor doctor = construirDoctor(DOCUMENTO, CORREO);
        when(repositorioDoctor.obtenerPorNumeroDocumento(DOCUMENTO))
                .thenReturn(Optional.of(doctor));
        when(repositorioDoctor.existeCorreoParaOtroDoctor(CORREO, DOCUMENTO))
                .thenReturn(false);

        // Act
        servicioActualizarDoctor.ejecutar(doctor);

        // Assert
        verify(repositorioDoctor, times(1)).actualizar(doctor);
    }

    @Test
    @DisplayName("Debería lanzar ExcepcionSinDatos cuando el doctor no existe (RN-1 en actualización)")
    void deberiaLanzarExcepcionSinDatos_Cuando_DoctorNoExiste() {
        // Arrange
        Doctor doctor = construirDoctor("NOEXISTE", CORREO);
        when(repositorioDoctor.obtenerPorNumeroDocumento("NOEXISTE"))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ExcepcionSinDatos.class, () -> servicioActualizarDoctor.ejecutar(doctor));
        verify(repositorioDoctor, never()).actualizar(any());
    }

    @Test
    @DisplayName("Debería lanzar ExcepcionDuplicidad cuando el correo pertenece a otro doctor (RN-11)")
    void deberiaLanzarExcepcionDuplicidad_Cuando_CorreoUsadoPorOtroDoctor() {
        // Arrange
        Doctor doctor = construirDoctor(DOCUMENTO, OTRO_CORREO);
        when(repositorioDoctor.obtenerPorNumeroDocumento(DOCUMENTO))
                .thenReturn(Optional.of(doctor));
        when(repositorioDoctor.existeCorreoParaOtroDoctor(OTRO_CORREO, DOCUMENTO))
                .thenReturn(true);

        // Act & Assert
        assertThrows(ExcepcionDuplicidad.class, () -> servicioActualizarDoctor.ejecutar(doctor));
        verify(repositorioDoctor, never()).actualizar(any());
    }
}
