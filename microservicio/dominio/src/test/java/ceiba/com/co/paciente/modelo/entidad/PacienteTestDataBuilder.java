package ceiba.com.co.paciente.modelo.entidad;

import java.time.LocalDate;

public class PacienteTestDataBuilder {

    private Long numeroDocumento = 123456789L;
    private TipoDocumento tipoDocumento = TipoDocumento.CC;
    private String nombre = "Juan";
    private String apellido = "Perez";
    private String telefono = "3001234567";
    private String correoElectronico = "juan.perez@example.com";
    private String eps = "EPS Sura";
    private Genero genero = Genero.MASCULINO;
    private LocalDate fechaNacimiento = LocalDate.of(1990, 1, 1);

    private PacienteTestDataBuilder() {}

    public static PacienteTestDataBuilder unPacienteValida() {
        return new PacienteTestDataBuilder();
    }

    public PacienteTestDataBuilder numeroDocumento(Long numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
        return this;
    }

    public PacienteTestDataBuilder nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public PacienteTestDataBuilder apellido(String apellido) {
        this.apellido = apellido;
        return this;
    }

    public PacienteTestDataBuilder correoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
        return this;
    }

    public PacienteTestDataBuilder fechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
        return this;
    }

    public Paciente build() {
        return Paciente.builder()
                .conNumeroDocumento(numeroDocumento)
                .conTipoDocumento(tipoDocumento)
                .conNombre(new Nombre(nombre, "El nombre es obligatorio", "El nombre contiene caracteres no permitidos"))
                .conApellido(new Nombre(apellido, "El apellido es obligatorio", "El apellido contiene caracteres no permitidos"))
                .conFechaNacimiento(fechaNacimiento)
                .conTelefono(telefono)
                .conCorreoElectronico(new Email(correoElectronico))
                .conEps(eps)
                .conGenero(genero)
                .build();
    }
}
