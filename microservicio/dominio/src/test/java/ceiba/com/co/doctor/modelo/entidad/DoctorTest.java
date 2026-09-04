package ceiba.com.co.doctor.modelo.entidad;

import ceiba.com.co.excepcion.ExcepcionValorObligatorio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DoctorTest {

    @Test
    void validarCreacionExitosa() {
        // Arrange
        String numeroDocumento = "123456789";
        String nombre = "Juan";
        String apellido = "Perez";
        String tarjetaProfesional = "TP123";
        String especialidad = "CARDIOLOGIA";
        String correoInstitucional = "juan.perez@hospital.com";

        // Act
        Doctor doctor = Doctor.builder()
                .conNumeroDocumento(numeroDocumento)
                .conNombre(nombre)
                .conApellido(apellido)
                .conTarjetaProfesional(tarjetaProfesional)
                .conEspecialidad(especialidad)
                .conCorreoInstitucional(correoInstitucional)
                .build();

        // Assert
        Assertions.assertEquals(numeroDocumento, doctor.getNumeroDocumento());
        Assertions.assertEquals(nombre, doctor.getNombre());
        Assertions.assertEquals(apellido, doctor.getApellido());
        Assertions.assertEquals(tarjetaProfesional, doctor.getTarjetaProfesional());
        Assertions.assertEquals(especialidad, doctor.getEspecialidad());
        Assertions.assertEquals(correoInstitucional, doctor.getCorreoInstitucional());
        Assertions.assertTrue(doctor.isHabilitado());
    }

    @Test
    void validarFaltaNumeroDocumento() {
        Assertions.assertThrows(ExcepcionValorObligatorio.class, () ->
                Doctor.builder()
                        .conNombre("Juan")
                        .conApellido("Perez")
                        .conTarjetaProfesional("TP123")
                        .conEspecialidad("CARDIOLOGIA")
                        .conCorreoInstitucional("correo@hospital.com")
                        .build()
        );
    }
}
