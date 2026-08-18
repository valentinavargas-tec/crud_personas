package ceiba.com.co;

import ceiba.com.co.excepcion.ExcepcionValorInvalido;
import ceiba.com.co.excepcion.ExcepcionValorObligatorio;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidadorArgumento {

    private ValidadorArgumento() {
    }


    public static void validarObligatorio(Object valor, String mensaje) {
        if (valor == null) {
            throw new ExcepcionValorObligatorio(mensaje);
        }
    }

    public static void validarRegex(String correoElectronico, String regex, String mensaje) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(correoElectronico);

        if (!matcher.matches()) {
            throw new ExcepcionValorInvalido(mensaje);
        }
    }

}