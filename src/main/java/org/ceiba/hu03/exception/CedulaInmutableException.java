package org.ceiba.hu03.exception;

public class CedulaInmutableException extends RuntimeException {

    public CedulaInmutableException(String mensaje) {
        super(mensaje);
    }

    public CedulaInmutableException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
