package ceiba.com.co.paciente.comando;

import java.time.LocalDate;

public class ComandoActualizarPacienteTestDataBuilder {

    private String nombre = "Juan";
    private String apellido = "Perez";
    private LocalDate fechaNacimiento = LocalDate.of(1990, 1, 1);
    private String telefono = "3001234567";
    private String correoElectronico = "juan.perez@example.com";
    private String eps = "Sura";
    private String genero = "MASCULINO";

    private ComandoActualizarPacienteTestDataBuilder() {}

    public static ComandoActualizarPacienteTestDataBuilder unComandoActualizarPacienteValido() {
        return new ComandoActualizarPacienteTestDataBuilder();
    }

    public ComandoActualizarPacienteTestDataBuilder conNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public ComandoActualizarPacienteTestDataBuilder conApellido(String apellido) {
        this.apellido = apellido;
        return this;
    }

    public ComandoActualizarPacienteTestDataBuilder conCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
        return this;
    }

    public ComandoActualizarPacienteTestDataBuilder conFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
        return this;
    }

    public ComandoActualizarPaciente build() {
        return new ComandoActualizarPaciente(nombre, apellido, fechaNacimiento, telefono, correoElectronico, eps, genero);
    }
}
