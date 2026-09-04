package ceiba.com.co.doctor.comando;

public class ComandoDoctorTestDataBuilder {

    private String numeroDocumento = "DOC-001";
    private String nombre = "Ana";
    private String apellido = "Torres";
    private String tarjetaProfesional = "TP-001";
    private String especialidad = "CARDIOLOGIA";
    private String correoInstitucional = "ana.torres@hospital.com";

    private ComandoDoctorTestDataBuilder() {}

    public static ComandoDoctorTestDataBuilder unComandoDoctorValido() {
        return new ComandoDoctorTestDataBuilder();
    }

    public ComandoDoctorTestDataBuilder conNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
        return this;
    }

    public ComandoDoctorTestDataBuilder conNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public ComandoDoctorTestDataBuilder conApellido(String apellido) {
        this.apellido = apellido;
        return this;
    }

    public ComandoDoctorTestDataBuilder conTarjetaProfesional(String tarjetaProfesional) {
        this.tarjetaProfesional = tarjetaProfesional;
        return this;
    }

    public ComandoDoctorTestDataBuilder conEspecialidad(String especialidad) {
        this.especialidad = especialidad;
        return this;
    }

    public ComandoDoctorTestDataBuilder conCorreoInstitucional(String correoInstitucional) {
        this.correoInstitucional = correoInstitucional;
        return this;
    }

    public ComandoDoctor build() {
        return new ComandoDoctor(numeroDocumento, nombre, apellido, tarjetaProfesional, especialidad, correoInstitucional);
    }
}
