package ceiba.com.co.paciente.modelo.entidad;

import ceiba.com.co.ValidadorArgumento;
import ceiba.com.co.excepcion.ExcepcionValorInvalido;
import java.time.LocalDate;
import java.time.Period;

@SuppressWarnings("ClassCanBeRecord")
public class Paciente {

    private final Long numeroDocumento;
    private final TipoDocumento tipoDocumento;
    private final Nombre nombre;
    private final Nombre apellido;
    private final LocalDate fechaNacimiento;
    private final String telefono;
    private final Email correoElectronico;
    private final String eps;
    private final Genero genero;

    private Paciente(Builder builder) {
        this.numeroDocumento = builder.numeroDocumento;
        this.tipoDocumento = builder.tipoDocumento;
        this.nombre = builder.nombre;
        this.apellido = builder.apellido;
        this.fechaNacimiento = builder.fechaNacimiento;
        this.telefono = builder.telefono;
        this.correoElectronico = builder.correoElectronico;
        this.eps = builder.eps;
        this.genero = builder.genero;

        ValidadorArgumento.validarObligatorio(this.numeroDocumento, "El número de documento es obligatorio");
        if (this.numeroDocumento <= 0) {
            throw new ExcepcionValorInvalido("El número de documento debe ser un número válido y positivo");
        }

        ValidadorArgumento.validarObligatorio(this.tipoDocumento, "El tipo de documento es obligatorio");
        ValidadorArgumento.validarObligatorio(this.nombre, "El nombre es obligatorio");
        ValidadorArgumento.validarObligatorio(this.apellido, "El apellido es obligatorio");
        
        if (this.fechaNacimiento != null && this.fechaNacimiento.isAfter(LocalDate.now())) {
            throw new ExcepcionValorInvalido("La fecha de nacimiento no puede ser futura");
        }

        ValidadorArgumento.validarObligatorio(this.telefono, "El teléfono es obligatorio");
        ValidadorArgumento.validarObligatorio(this.correoElectronico, "El correo electrónico es obligatorio");
        ValidadorArgumento.validarObligatorio(this.eps, "La EPS es obligatoria");
        ValidadorArgumento.validarObligatorio(this.genero, "El género es obligatorio");
    }

    public static Builder builder() {
        return new Builder();
    }

    public Paciente actualizarDatos(String nombreStr, String apellidoStr, LocalDate fechaNacimiento, String telefono, String correoElectronicoStr, String eps, Genero genero) {
        return Paciente.builder()
                .conNumeroDocumento(this.numeroDocumento)
                .conTipoDocumento(this.tipoDocumento)
                .conNombre(new Nombre(nombreStr, "El nombre es obligatorio", "El nombre contiene caracteres no permitidos"))
                .conApellido(new Nombre(apellidoStr, "El apellido es obligatorio", "El apellido contiene caracteres no permitidos"))
                .conFechaNacimiento(fechaNacimiento)
                .conTelefono(telefono)
                .conCorreoElectronico(new Email(correoElectronicoStr))
                .conEps(eps)
                .conGenero(genero)
                .build();
    }

    public Integer obtenerEdadActual() {
        if (this.fechaNacimiento == null) {
            return null;
        }
        return Period.between(this.fechaNacimiento, LocalDate.now()).getYears();
    }

    public Long getNumeroDocumento() {
        return numeroDocumento;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public String getNombre() {
        return nombre.getValor();
    }

    public String getApellido() {
        return apellido.getValor();
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreoElectronico() {
        return correoElectronico.getValor();
    }

    public String getEps() {
        return eps;
    }

    public Genero getGenero() {
        return genero;
    }

    public static class Builder {
        private Long numeroDocumento;
        private TipoDocumento tipoDocumento;
        private Nombre nombre;
        private Nombre apellido;
        private LocalDate fechaNacimiento;
        private String telefono;
        private Email correoElectronico;
        private String eps;
        private Genero genero;

        public Builder conNumeroDocumento(Long numeroDocumento) {
            this.numeroDocumento = numeroDocumento;
            return this;
        }

        public Builder conTipoDocumento(TipoDocumento tipoDocumento) {
            this.tipoDocumento = tipoDocumento;
            return this;
        }

        public Builder conNombre(Nombre nombre) {
            this.nombre = nombre;
            return this;
        }

        public Builder conApellido(Nombre apellido) {
            this.apellido = apellido;
            return this;
        }

        public Builder conFechaNacimiento(LocalDate fechaNacimiento) {
            this.fechaNacimiento = fechaNacimiento;
            return this;
        }

        public Builder conTelefono(String telefono) {
            this.telefono = telefono;
            return this;
        }

        public Builder conCorreoElectronico(Email correoElectronico) {
            this.correoElectronico = correoElectronico;
            return this;
        }

        public Builder conEps(String eps) {
            this.eps = eps;
            return this;
        }

        public Builder conGenero(Genero genero) {
            this.genero = genero;
            return this;
        }

        public Paciente build() {
            return new Paciente(this);
        }
    }
}
