package ceiba.com.co.doctor.modelo.entidad;

import ceiba.com.co.ValidadorArgumento;

public class Doctor {

    private final String numeroDocumento;
    private final String nombre;
    private final String apellido;
    private final String tarjetaProfesional;
    private final String especialidad;
    private final String correoInstitucional;
    private boolean habilitado;

    private Doctor(Builder builder) {
        this.numeroDocumento = builder.numeroDocumento;
        this.nombre = builder.nombre;
        this.apellido = builder.apellido;
        this.tarjetaProfesional = builder.tarjetaProfesional;
        this.especialidad = builder.especialidad;
        this.correoInstitucional = builder.correoInstitucional;

        ValidadorArgumento.validarObligatorio(this.numeroDocumento, "El número de documento es obligatorio");
        ValidadorArgumento.validarObligatorio(this.nombre, "El nombre es obligatorio");
        ValidadorArgumento.validarObligatorio(this.apellido, "El apellido es obligatorio");
        ValidadorArgumento.validarObligatorio(this.tarjetaProfesional, "La tarjeta profesional es obligatoria");
        ValidadorArgumento.validarObligatorio(this.especialidad, "La especialidad es obligatoria");
        ValidadorArgumento.validarObligatorio(this.correoInstitucional, "El correo institucional es obligatorio");

        this.habilitado = builder.habilitado != null ? builder.habilitado : true;
    }

    public void deshabilitar() {
        if (!this.habilitado) {
            throw new ceiba.com.co.excepcion.ExcepcionReglaNegocio(
                    String.format("El doctor con número de documento %s ya se encuentra deshabilitado.", this.numeroDocumento));
        }
        this.habilitado = false;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getTarjetaProfesional() {
        return tarjetaProfesional;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public String getCorreoInstitucional() {
        return correoInstitucional;
    }

    public boolean isHabilitado() {
        return habilitado;
    }

    public static class Builder {
        private String numeroDocumento;
        private String nombre;
        private String apellido;
        private String tarjetaProfesional;
        private String especialidad;
        private String correoInstitucional;
        private Boolean habilitado;

        public Builder conNumeroDocumento(String numeroDocumento) {
            this.numeroDocumento = numeroDocumento;
            return this;
        }

        public Builder conNombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public Builder conApellido(String apellido) {
            this.apellido = apellido;
            return this;
        }

        public Builder conTarjetaProfesional(String tarjetaProfesional) {
            this.tarjetaProfesional = tarjetaProfesional;
            return this;
        }

        public Builder conEspecialidad(String especialidad) {
            this.especialidad = especialidad;
            return this;
        }

        public Builder conCorreoInstitucional(String correoInstitucional) {
            this.correoInstitucional = correoInstitucional;
            return this;
        }

        public Builder conHabilitado(Boolean habilitado) {
            this.habilitado = habilitado;
            return this;
        }

        public Doctor build() {
            return new Doctor(this);
        }
    }
}
