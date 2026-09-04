package ceiba.com.co.doctor.servicio;

import ceiba.com.co.doctor.modelo.entidad.Doctor;
import ceiba.com.co.doctor.puerto.repositorio.RepositorioDoctor;
import ceiba.com.co.doctor.puerto.repositorio.RepositorioEspecialidad;
import ceiba.com.co.excepcion.ExcepcionDuplicidad;
import ceiba.com.co.excepcion.ExcepcionValorInvalido;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ServicioCrearDoctorTest {

    private RepositorioDoctor repositorioDoctor;
    private RepositorioEspecialidad repositorioEspecialidad;
    private ServicioCrearDoctor servicioCrearDoctor;

    @BeforeEach
    void setUp() {
        repositorioDoctor = Mockito.mock(RepositorioDoctor.class);
        repositorioEspecialidad = Mockito.mock(RepositorioEspecialidad.class);
        servicioCrearDoctor = new ServicioCrearDoctor(repositorioDoctor, repositorioEspecialidad);
    }

    @Test
    void ejecutarCreacionExitosa() {
        // Arrange
        Doctor doctor = Doctor.builder()
                .conNumeroDocumento("123")
                .conNombre("Juan")
                .conApellido("Perez")
                .conTarjetaProfesional("TP1")
                .conEspecialidad("CARDIOLOGIA")
                .conCorreoInstitucional("juan@hospital.com")
                .build();
        Mockito.when(repositorioEspecialidad.existe("CARDIOLOGIA")).thenReturn(true);
        Mockito.when(repositorioDoctor.existePorCorreoInstitucional("juan@hospital.com")).thenReturn(false);
        Mockito.when(repositorioDoctor.existePorTarjetaProfesional("TP1")).thenReturn(false);

        // Act
        servicioCrearDoctor.ejecutar(doctor);

        // Assert
        Mockito.verify(repositorioDoctor, Mockito.times(1)).guardar(doctor);
    }

    @Test
    void ejecutarFallaEspecialidadNoExiste() {
        // Arrange
        Doctor doctor = Doctor.builder()
                .conNumeroDocumento("123")
                .conNombre("Juan")
                .conApellido("Perez")
                .conTarjetaProfesional("TP1")
                .conEspecialidad("INVENTADA")
                .conCorreoInstitucional("juan@hospital.com")
                .build();
        Mockito.when(repositorioEspecialidad.existe("INVENTADA")).thenReturn(false);

        // Act & Assert
        Assertions.assertThrows(ExcepcionValorInvalido.class, () -> servicioCrearDoctor.ejecutar(doctor));
        Mockito.verify(repositorioDoctor, Mockito.never()).guardar(Mockito.any());
    }

    @Test
    void ejecutarFallaCorreoYaExiste() {
        // Arrange
        Doctor doctor = Doctor.builder()
                .conNumeroDocumento("123")
                .conNombre("Juan")
                .conApellido("Perez")
                .conTarjetaProfesional("TP1")
                .conEspecialidad("CARDIOLOGIA")
                .conCorreoInstitucional("juan@hospital.com")
                .build();
        Mockito.when(repositorioEspecialidad.existe("CARDIOLOGIA")).thenReturn(true);
        Mockito.when(repositorioDoctor.existePorCorreoInstitucional("juan@hospital.com")).thenReturn(true);

        // Act & Assert
        Assertions.assertThrows(ExcepcionDuplicidad.class, () -> servicioCrearDoctor.ejecutar(doctor));
        Mockito.verify(repositorioDoctor, Mockito.never()).guardar(Mockito.any());
    }

    @Test
    void ejecutarFallaTarjetaYaExiste() {
        // Arrange
        Doctor doctor = Doctor.builder()
                .conNumeroDocumento("123")
                .conNombre("Juan")
                .conApellido("Perez")
                .conTarjetaProfesional("TP1")
                .conEspecialidad("CARDIOLOGIA")
                .conCorreoInstitucional("juan@hospital.com")
                .build();
        Mockito.when(repositorioEspecialidad.existe("CARDIOLOGIA")).thenReturn(true);
        Mockito.when(repositorioDoctor.existePorCorreoInstitucional("juan@hospital.com")).thenReturn(false);
        Mockito.when(repositorioDoctor.existePorTarjetaProfesional("TP1")).thenReturn(true);

        // Act & Assert
        Assertions.assertThrows(ExcepcionDuplicidad.class, () -> servicioCrearDoctor.ejecutar(doctor));
        Mockito.verify(repositorioDoctor, Mockito.never()).guardar(Mockito.any());
    }
}
