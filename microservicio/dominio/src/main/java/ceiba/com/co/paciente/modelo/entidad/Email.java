package ceiba.com.co.paciente.modelo.entidad;

import ceiba.com.co.ValidadorArgumento;

public class Email {

    private static final String REGEXP = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private final String valor;

    public Email(String valor) {
        String emailLimpio = valor != null ? valor.trim() : null;
        ValidadorArgumento.validarObligatorio(emailLimpio, "El email es obligatorio");
        ValidadorArgumento.validarRegex(emailLimpio, REGEXP, "El formato de email no es válido");
        this.valor = emailLimpio;
    }

    public String getValor() {
        return valor;
    }
}
