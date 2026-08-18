package org.ceiba.hu03.exception;

public class PersonaNotFoundException extends RuntimeException {

    public PersonaNotFoundException(String mensaje) {
        super(mensaje);
    }
}
