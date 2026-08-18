package ceiba.com.co.modelo.entidad;

import ceiba.com.co.ValidadorArgumento;
import ceiba.com.co.excepcion.ExcepcionValorInvalido;
import java.time.LocalDate;
import java.time.Period;

@SuppressWarnings("ClassCanBeRecord")
public class Persona {

    private final Long cedula;
    private final Nombre nombre;
    private final Nombre apellido;
    private final Email email;
    private final LocalDate fechaNacimiento;

    private Persona(Builder builder) {
        this.cedula = builder.cedula;
        this.nombre = builder.nombre;
        this.apellido = builder.apellido;
        this.email = builder.email;
        this.fechaNacimiento = builder.fechaNacimiento;

        ValidadorArgumento.validarObligatorio(this.cedula, "La cédula es obligatoria");
        if (this.cedula <= 0) {
            throw new ExcepcionValorInvalido("La cédula debe ser un número válido y positivo");
        }

        ValidadorArgumento.validarObligatorio(this.nombre, "El nombre es obligatorio");
        ValidadorArgumento.validarObligatorio(this.apellido, "El apellido es obligatorio");
        ValidadorArgumento.validarObligatorio(this.email, "El email es obligatorio");

        if (this.fechaNacimiento != null && this.fechaNacimiento.isAfter(LocalDate.now())) {
            throw new ExcepcionValorInvalido("La fecha de nacimiento no puede ser futura");
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public Persona actualizarDatos(String nombreStr, String apellidoStr, String emailStr, LocalDate fechaNacimiento) {
        return Persona.builder()
                .conCedula(this.cedula)
                .conNombre(new Nombre(nombreStr, "El nombre es obligatorio", "El nombre contiene caracteres no permitidos"))
                .conApellido(new Nombre(apellidoStr, "El apellido es obligatorio", "El apellido contiene caracteres no permitidos"))
                .conEmail(new Email(emailStr))
                .conFechaNacimiento(fechaNacimiento)
                .build();
    }

    public Integer obtenerEdadActual() {
        if (this.fechaNacimiento == null) {
            return null;
        }
        return Period.between(this.fechaNacimiento, LocalDate.now()).getYears();
    }

    public Long getCedula() {
        return cedula;
    }

    public String getNombre() {
        return nombre.getValor();
    }

    public String getApellido() {
        return apellido.getValor();
    }

    public String getEmail() {
        return email.getValor();
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public static class Builder {
        private Long cedula;
        private Nombre nombre;
        private Nombre apellido;
        private Email email;
        private LocalDate fechaNacimiento;

        public Builder conCedula(Long cedula) {
            this.cedula = cedula;
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

        public Builder conEmail(Email email) {
            this.email = email;
            return this;
        }

        public Builder conFechaNacimiento(LocalDate fechaNacimiento) {
            this.fechaNacimiento = fechaNacimiento;
            return this;
        }

        public Persona build() {
            return new Persona(this);
        }
    }
}
