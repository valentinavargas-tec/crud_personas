package org.ceiba.hu03.domain.exception;

public class InvalidSearchCriteriaException extends RuntimeException {

    public InvalidSearchCriteriaException(String mensaje) {
        super(mensaje);
    }

    public InvalidSearchCriteriaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
