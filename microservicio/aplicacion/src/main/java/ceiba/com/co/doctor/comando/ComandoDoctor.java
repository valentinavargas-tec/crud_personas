package ceiba.com.co.doctor.comando;

public class ComandoDoctor {

    private String numeroDocumento;
    private String nombre;
    private String apellido;
    private String tarjetaProfesional;
    private String especialidad;
    private String correoInstitucional;

    public ComandoDoctor() {
    }

    public ComandoDoctor(String numeroDocumento, String nombre, String apellido, String tarjetaProfesional, String especialidad, String correoInstitucional) {
        this.numeroDocumento = numeroDocumento;
        this.nombre = nombre;
        this.apellido = apellido;
        this.tarjetaProfesional = tarjetaProfesional;
        this.especialidad = especialidad;
        this.correoInstitucional = correoInstitucional;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTarjetaProfesional() {
        return tarjetaProfesional;
    }

    public void setTarjetaProfesional(String tarjetaProfesional) {
        this.tarjetaProfesional = tarjetaProfesional;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getCorreoInstitucional() {
        return correoInstitucional;
    }

    public void setCorreoInstitucional(String correoInstitucional) {
        this.correoInstitucional = correoInstitucional;
    }
}
