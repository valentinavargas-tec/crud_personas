package ceiba.com.co.doctor.servicio;

import ceiba.com.co.doctor.modelo.entidad.Doctor;
import ceiba.com.co.doctor.puerto.repositorio.RepositorioDoctor;
import ceiba.com.co.excepcion.ExcepcionReglaNegocio;
import ceiba.com.co.excepcion.ExcepcionSinDatos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ServicioDeshabilitarDoctorTest {

    private RepositorioDoctor repositorioDoctor;
    private ServicioDeshabilitarDoctor servicioDeshabilitarDoctor;

    private static final String DOCUMENTO = "DOC-001";

    @BeforeEach
    void setUp() {
        repositorioDoctor = Mockito.mock(RepositorioDoctor.class);
        servicioDeshabilitarDoctor = new ServicioDeshabilitarDoctor(repositorioDoctor);
    }

    private Doctor construirDoctorHabilitado() {
        return Doctor.builder()
                .conNumeroDocumento(DOCUMENTO)
                .conNombre("Luis")
                .conApellido("Ramos")
                .conTarjetaProfesional("TP-002")
                .conEspecialidad("NEUROLOGIA")
                .conCorreoInstitucional("luis.ramos@hospital.com")
                .conHabilitado(true)
                .build();
    }

    private Doctor construirDoctorDeshabilitado() {
        return Doctor.builder()
                .conNumeroDocumento(DOCUMENTO)
                .conNombre("Luis")
                .conApellido("Ramos")
                .conTarjetaProfesional("TP-002")
                .conEspecialidad("NEUROLOGIA")
                .conCorreoInstitucional("luis.ramos@hospital.com")
                .conHabilitado(false)
                .build();
    }

    @Test
    @DisplayName("Debería deshabilitar doctor exitosamente cuando está habilitado (RN-13)")
    void deberiaDeshabilitar_Cuando_DoctorEstaHabilitado() {
        // Arrange
        when(repositorioDoctor.obtenerPorNumeroDocumento(DOCUMENTO))
                .thenReturn(Optional.of(construirDoctorHabilitado()));

        // Act
        servicioDeshabilitarDoctor.ejecutar(DOCUMENTO);

        // Assert
        verify(repositorioDoctor, times(1)).deshabilitar(DOCUMENTO);
    }

    @Test
    @DisplayName("Debería lanzar ExcepcionSinDatos cuando el doctor no existe")
    void deberiaLanzarExcepcionSinDatos_Cuando_DoctorNoExiste() {
        // Arrange
        when(repositorioDoctor.obtenerPorNumeroDocumento("NOEXISTE"))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ExcepcionSinDatos.class,
                () -> servicioDeshabilitarDoctor.ejecutar("NOEXISTE"));
        verify(repositorioDoctor, never()).deshabilitar(any());
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando el doctor ya está deshabilitado (RN-13)")
    void deberiaLanzarExcepcion_Cuando_DoctorYaEstaDeshabilitado() {
        // Arrange
        when(repositorioDoctor.obtenerPorNumeroDocumento(DOCUMENTO))
                .thenReturn(Optional.of(construirDoctorDeshabilitado()));

        // Act & Assert
        assertThrows(ExcepcionReglaNegocio.class,
                () -> servicioDeshabilitarDoctor.ejecutar(DOCUMENTO));
        verify(repositorioDoctor, never()).deshabilitar(any());
    }
}
