package ceiba.com.co.doctor.comando;

public class ComandoActualizarDoctor {

    private String nombre;
    private String apellido;
    private String especialidad;
    private String correoInstitucional;

    public ComandoActualizarDoctor() {}

    public ComandoActualizarDoctor(String nombre, String apellido,
                                   String especialidad, String correoInstitucional) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.especialidad = especialidad;
        this.correoInstitucional = correoInstitucional;
    }

    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getEspecialidad() { return especialidad; }
    public String getCorreoInstitucional() { return correoInstitucional; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
    public void setCorreoInstitucional(String correoInstitucional) {
        this.correoInstitucional = correoInstitucional;
    }
}
