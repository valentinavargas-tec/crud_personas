package ceiba.com.co.paciente.modelo.entidad;

import ceiba.com.co.ValidadorArgumento;

public class Nombre {

    private static final String REGEXP = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ' ]+$";

    private final String valor;

    public Nombre(String valor, String mensajeObligatorio, String mensajeFormato) {
        String nombreLimpio = valor != null ? valor.trim() : null;
        ValidadorArgumento.validarObligatorio(nombreLimpio, mensajeObligatorio);
        ValidadorArgumento.validarRegex(nombreLimpio, REGEXP, mensajeFormato);
        this.valor = nombreLimpio;
    }

    public String getValor() {
        return valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nombre nombre = (Nombre) o;
        return java.util.Objects.equals(valor, nombre.valor);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(valor);
    }

    @Override
    public String toString() {
        return valor;
    }
}
