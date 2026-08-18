package ceiba.com.co.comando;

import java.time.LocalDate;

public class ComandoActualizarPersonaTestDataBuilder {

    private String nombre = "Juan";
    private String apellido = "Perez";
    private String email = "juan.perez@example.com";
    private LocalDate fechaNacimiento = LocalDate.of(1990, 1, 1);

    private ComandoActualizarPersonaTestDataBuilder() {}

    public static ComandoActualizarPersonaTestDataBuilder unComandoActualizarPersonaValido() {
        return new ComandoActualizarPersonaTestDataBuilder();
    }

    public ComandoActualizarPersonaTestDataBuilder conNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public ComandoActualizarPersonaTestDataBuilder conApellido(String apellido) {
        this.apellido = apellido;
        return this;
    }

    public ComandoActualizarPersonaTestDataBuilder conEmail(String email) {
        this.email = email;
        return this;
    }

    public ComandoActualizarPersonaTestDataBuilder conFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
        return this;
    }

    public ComandoActualizarPersona build() {
        return new ComandoActualizarPersona(nombre, apellido, email, fechaNacimiento);
    }
}
