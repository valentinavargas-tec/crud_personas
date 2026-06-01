package org.ceiba.hu03.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.ceiba.hu03.constants.AppConstants;
import org.ceiba.hu03.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones para la API REST.
 * Todas las excepciones se convierten a ErrorResponse con estructura consistente.
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse manejarValidaciones(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errores.put(error.getField(), error.getDefaultMessage())
        );
        return new ErrorResponse(
                System.currentTimeMillis(),
                HttpStatus.BAD_REQUEST.value(),
                "VALIDACION_FALLIDA",
                "Errores en la validación de campos obligatorios",
                request.getRequestURI(),
                errores
        );
    }

    @ExceptionHandler(PersonaNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse manejarPersonaNoEncontrada(PersonaNotFoundException ex, HttpServletRequest request) {
        return new ErrorResponse(
                System.currentTimeMillis(),
                HttpStatus.NOT_FOUND.value(),
                "PERSONA_NO_ENCONTRADA",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(PersonaAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse manejarPersonaYaExiste(PersonaAlreadyExistsException ex, HttpServletRequest request) {
        return new ErrorResponse(
                System.currentTimeMillis(),
                HttpStatus.CONFLICT.value(),
                "CEDULA_DUPLICADA",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(CedulaInmutableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse manejarCedulaInmutable(CedulaInmutableException ex, HttpServletRequest request) {
        return new ErrorResponse(
                System.currentTimeMillis(),
                HttpStatus.BAD_REQUEST.value(),
                "CEDULA_INMUTABLE",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse manejarEmailDuplicado(EmailAlreadyExistsException ex, HttpServletRequest request) {
        return new ErrorResponse(
                System.currentTimeMillis(),
                HttpStatus.BAD_REQUEST.value(),
                "EMAIL_DUPLICADO",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse manejarTipoInvalido(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        return new ErrorResponse(
                System.currentTimeMillis(),
                HttpStatus.BAD_REQUEST.value(),
                "TIPO_INVALIDO",
                AppConstants.ID_TIPO_INVALIDO,
                request.getRequestURI()
        );
    }

}
