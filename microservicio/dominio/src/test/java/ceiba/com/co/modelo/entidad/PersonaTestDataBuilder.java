package ceiba.com.co.modelo.entidad;

import java.time.LocalDate;

public class PersonaTestDataBuilder {

    private Long cedula = 123456789L;
    private String nombre = "Juan";
    private String apellido = "Perez";
    private String email = "juan.perez@example.com";
    private LocalDate fechaNacimiento = LocalDate.of(1990, 1, 1);

    private PersonaTestDataBuilder() {}

    public static PersonaTestDataBuilder unPersonaValida() {
        return new PersonaTestDataBuilder();
    }

    public PersonaTestDataBuilder cedula(Long cedula) {
        this.cedula = cedula;
        return this;
    }

    public PersonaTestDataBuilder nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public PersonaTestDataBuilder apellido(String apellido) {
        this.apellido = apellido;
        return this;
    }

    public PersonaTestDataBuilder email(String email) {
        this.email = email;
        return this;
    }

    public PersonaTestDataBuilder fechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
        return this;
    }

    public Persona build() {
        return new Persona(cedula, nombre, apellido, email, fechaNacimiento);
    }
}
