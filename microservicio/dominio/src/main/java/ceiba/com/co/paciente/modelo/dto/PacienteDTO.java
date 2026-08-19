package ceiba.com.co.paciente.modelo.dto;

import java.time.LocalDate;

public class PacienteDTO {

    private final Long numeroDocumento;
    private final String tipoDocumento;
    private final String nombre;
    private final String apellido;
    private final LocalDate fechaNacimiento;
    private final String telefono;
    private final String correoElectronico;
    private final String eps;
    private final String genero;

    public PacienteDTO(Long numeroDocumento, String tipoDocumento, String nombre, String apellido, LocalDate fechaNacimiento, String telefono, String correoElectronico, String eps, String genero) {
        this.numeroDocumento = numeroDocumento;
        this.tipoDocumento = tipoDocumento;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
        this.correoElectronico = correoElectronico;
        this.eps = eps;
        this.genero = genero;
    }

    public Long getNumeroDocumento() {
        return numeroDocumento;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public String getEps() {
        return eps;
    }

    public String getGenero() {
        return genero;
    }
}
