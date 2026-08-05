package ceiba.com.co.comando;

import java.time.LocalDate;

public class ComandoPersonaTestDataBuilder {

    private Long cedula = 123456789L;
    private String nombre = "Juan";
    private String apellido = "Perez";
    private String email = "juan.perez@example.com";
    private LocalDate fechaNacimiento = LocalDate.of(1990, 1, 1);

    private ComandoPersonaTestDataBuilder() {}

    public static ComandoPersonaTestDataBuilder unComandoPersonaValido() {
        return new ComandoPersonaTestDataBuilder();
    }

    public ComandoPersonaTestDataBuilder conCedula(Long cedula) {
        this.cedula = cedula;
        return this;
    }

    public ComandoPersonaTestDataBuilder conNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public ComandoPersonaTestDataBuilder conApellido(String apellido) {
        this.apellido = apellido;
        return this;
    }

    public ComandoPersonaTestDataBuilder conEmail(String email) {
        this.email = email;
        return this;
    }

    public ComandoPersonaTestDataBuilder conFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
        return this;
    }

    public ComandoPersona build() {
        return new ComandoPersona(cedula, nombre, apellido, email, fechaNacimiento);
    }
}
