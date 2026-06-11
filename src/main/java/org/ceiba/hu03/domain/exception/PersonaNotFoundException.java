package org.ceiba.hu03.domain.exception;

public class PersonaNotFoundException extends RuntimeException {

    public PersonaNotFoundException(String mensaje) {
        super(mensaje);
    }

    public PersonaNotFoundException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
