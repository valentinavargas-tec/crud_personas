package org.ceiba.hu03.domain.exception;

public class PersonaAlreadyExistsException extends RuntimeException {

    public PersonaAlreadyExistsException(String mensaje) {
        super(mensaje);
    }

    public PersonaAlreadyExistsException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
