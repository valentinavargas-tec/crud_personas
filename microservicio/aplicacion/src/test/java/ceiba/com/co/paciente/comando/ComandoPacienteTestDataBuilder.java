package ceiba.com.co.paciente.comando;

import java.time.LocalDate;

public class ComandoPacienteTestDataBuilder {

    private Long numeroDocumento = 123456789L;
    private String tipoDocumento = "CC";
    private String nombre = "Juan";
    private String apellido = "Perez";
    private LocalDate fechaNacimiento = LocalDate.of(1990, 1, 1);
    private String telefono = "3001234567";
    private String correoElectronico = "juan.perez@example.com";
    private String eps = "Sura";
    private String genero = "MASCULINO";

    private ComandoPacienteTestDataBuilder() {}

    public static ComandoPacienteTestDataBuilder unComandoPacienteValido() {
        return new ComandoPacienteTestDataBuilder();
    }

    public ComandoPacienteTestDataBuilder conNumeroDocumento(Long numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
        return this;
    }

    public ComandoPacienteTestDataBuilder conNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public ComandoPacienteTestDataBuilder conApellido(String apellido) {
        this.apellido = apellido;
        return this;
    }

    public ComandoPacienteTestDataBuilder conCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
        return this;
    }

    public ComandoPacienteTestDataBuilder conFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
        return this;
    }

    public ComandoPaciente build() {
        return new ComandoPaciente(numeroDocumento, tipoDocumento, nombre, apellido,
                fechaNacimiento, telefono, correoElectronico, eps, genero);
    }
}
