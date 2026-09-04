package ceiba.com.co.doctor.comando.manejador;

import ceiba.com.co.doctor.comando.ComandoDoctor;
import ceiba.com.co.doctor.comando.fabrica.FabricaDoctor;
import ceiba.com.co.doctor.modelo.entidad.Doctor;
import ceiba.com.co.doctor.servicio.ServicioCrearDoctor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ManejadorRegistrarDoctorTest {

    private ServicioCrearDoctor servicioCrearDoctor;
    private FabricaDoctor fabricaDoctor;
    private ManejadorRegistrarDoctor manejadorRegistrarDoctor;

    @BeforeEach
    void setUp() {
        servicioCrearDoctor = Mockito.mock(ServicioCrearDoctor.class);
        fabricaDoctor = Mockito.mock(FabricaDoctor.class);
        manejadorRegistrarDoctor = new ManejadorRegistrarDoctor(servicioCrearDoctor, fabricaDoctor);
    }

    @Test
    void ejecutarManejador() {
        // Arrange
        ComandoDoctor comandoDoctor = new ComandoDoctor("123", "Juan", "Perez", "TP1", "CARDIOLOGIA", "correo");
        Doctor doctor = Doctor.builder()
                .conNumeroDocumento("123")
                .conNombre("Juan")
                .conApellido("Perez")
                .conTarjetaProfesional("TP1")
                .conEspecialidad("CARDIOLOGIA")
                .conCorreoInstitucional("correo")
                .build();
        Mockito.when(fabricaDoctor.crear(comandoDoctor)).thenReturn(doctor);

        // Act
        manejadorRegistrarDoctor.ejecutar(comandoDoctor);

        // Assert
        Mockito.verify(servicioCrearDoctor, Mockito.times(1)).ejecutar(doctor);
    }
}
