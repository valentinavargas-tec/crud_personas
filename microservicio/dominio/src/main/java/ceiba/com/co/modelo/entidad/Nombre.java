package ceiba.com.co.modelo.entidad;

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
}
