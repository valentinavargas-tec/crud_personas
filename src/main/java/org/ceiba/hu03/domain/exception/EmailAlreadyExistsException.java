package org.ceiba.hu03.domain.exception;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String mensaje) {
        super(mensaje);
    }

    public EmailAlreadyExistsException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
