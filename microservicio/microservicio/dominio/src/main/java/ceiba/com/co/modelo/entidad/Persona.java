package ceiba.com.co.modelo.entidad;

import ceiba.com.co.ValidadorArgumento;
import ceiba.com.co.excepcion.ExcepcionValorInvalido;
import java.time.LocalDate;
import java.time.Period;

@SuppressWarnings("ClassCanBeRecord")
public class Persona {

    private static final String NOMBRE_APELLIDO_REGEXP = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ' ]+$";
    private static final String EMAIL_REGEXP = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private final Long cedula;
    private final String nombre;
    private final String apellido;
    private final String email;
    private final LocalDate fechaNacimiento;

    // Constructor único público: limpia los datos y ejecuta las validaciones de dominio
    public Persona(Long cedula, String nombre, String apellido, String email, LocalDate fechaNacimiento) {
        this.cedula = cedula;
        this.nombre = limpiar(nombre);
        this.apellido = limpiar(apellido);
        this.email = limpiar(email);
        this.fechaNacimiento = fechaNacimiento;

        ValidadorArgumento.validarObligatorio(this.cedula, "La cédula es obligatoria");
        ValidadorArgumento.validarObligatorio(this.nombre, "El nombre es obligatorio");
        ValidadorArgumento.validarObligatorio(this.apellido, "El apellido es obligatorio");
        ValidadorArgumento.validarObligatorio(this.email, "El email es obligatorio");

        if (this.cedula <= 0) {
            throw new ExcepcionValorInvalido("La cédula debe ser un número válido y positivo");
        }

        ValidadorArgumento.validarRegex(this.nombre, NOMBRE_APELLIDO_REGEXP, "El nombre contiene caracteres no permitidos");
        ValidadorArgumento.validarRegex(this.apellido, NOMBRE_APELLIDO_REGEXP, "El apellido contiene caracteres no permitidos");
        ValidadorArgumento.validarRegex(this.email, EMAIL_REGEXP, "El formato de email no es válido");

        if (this.fechaNacimiento != null && this.fechaNacimiento.isAfter(LocalDate.now())) {
            throw new ExcepcionValorInvalido("La fecha de nacimiento no puede ser futura");
        }
    }

    public Persona actualizarDatos(String nombre, String apellido, String email, LocalDate fechaNacimiento) {
        return new Persona(this.cedula, nombre, apellido, email, fechaNacimiento);
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
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    private static String limpiar(String valor) {
        return valor != null ? valor.trim() : null;
    }

}
