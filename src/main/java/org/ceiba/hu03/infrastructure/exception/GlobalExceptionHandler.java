package org.ceiba.hu03.infrastructure.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.ceiba.hu03.domain.constant.AppConstants;
import org.ceiba.hu03.domain.exception.CedulaInmutableException;
import org.ceiba.hu03.domain.exception.EmailAlreadyExistsException;
import org.ceiba.hu03.domain.exception.PersonaAlreadyExistsException;
import org.ceiba.hu03.domain.exception.PersonaNotFoundException;
import org.ceiba.hu03.domain.exception.InvalidSearchCriteriaException;
import org.ceiba.hu03.infrastructure.controller.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse manejarValidaciones(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errores.put(error.getField(), error.getDefaultMessage())
        );
        return ErrorResponse.builder()
                .timestamp(System.currentTimeMillis())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("VALIDACION_FALLIDA")
                .message("Errores en la validación de campos obligatorios")
                .path(request.getRequestURI())
                .fieldErrors(errores)
                .build();
    }

    @ExceptionHandler(PersonaNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse manejarPersonaNoEncontrada(PersonaNotFoundException ex, HttpServletRequest request) {
        return ErrorResponse.builder()
                .timestamp(System.currentTimeMillis())
                .status(HttpStatus.NOT_FOUND.value())
                .error("PERSONA_NO_ENCONTRADA")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
    }

    @ExceptionHandler(PersonaAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse manejarPersonaYaExiste(PersonaAlreadyExistsException ex, HttpServletRequest request) {
        return ErrorResponse.builder()
                .timestamp(System.currentTimeMillis())
                .status(HttpStatus.CONFLICT.value())
                .error("CEDULA_DUPLICADA")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
    }

    @ExceptionHandler(CedulaInmutableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse manejarCedulaInmutable(CedulaInmutableException ex, HttpServletRequest request) {
        return ErrorResponse.builder()
                .timestamp(System.currentTimeMillis())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("CEDULA_INMUTABLE")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse manejarEmailDuplicado(EmailAlreadyExistsException ex, HttpServletRequest request) {
        return ErrorResponse.builder()
                .timestamp(System.currentTimeMillis())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("EMAIL_DUPLICADO")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse manejarTipoInvalido(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        return ErrorResponse.builder()
                .timestamp(System.currentTimeMillis())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("TIPO_INVALIDO")
                .message(AppConstants.ID_TIPO_INVALIDO)
                .path(request.getRequestURI())
                .build();
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ErrorResponse manejarMediaTypeNoSoportado(HttpMediaTypeNotSupportedException ex, HttpServletRequest request) {
        return ErrorResponse.builder()
                .timestamp(System.currentTimeMillis())
                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
                .error("MEDIA_TYPE_NO_SOPORTADO")
                .message("El tipo de contenido (Content-Type) no es soportado. Debe ser application/json.")
                .path(request.getRequestURI())
                .build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse manejarCuerpoLegibleInvalido(HttpMessageNotReadableException ex, HttpServletRequest request) {
        return ErrorResponse.builder()
                .timestamp(System.currentTimeMillis())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("CUERPO_PETICION_INVALIDO")
                .message("El cuerpo de la solicitud no pudo ser leído o contiene un formato inválido (por ejemplo, JSON mal formado).")
                .path(request.getRequestURI())
                .build();
    }

    @ExceptionHandler(InvalidSearchCriteriaException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse manejarCriterioBusquedaInvalido(InvalidSearchCriteriaException ex, HttpServletRequest request) {
        return ErrorResponse.builder()
                .timestamp(System.currentTimeMillis())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("CRITERIO_BUSQUEDA_INVALIDO")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse manejarArgumentoInvalido(IllegalArgumentException ex, HttpServletRequest request) {
        return ErrorResponse.builder()
                .timestamp(System.currentTimeMillis())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("ARGUMENTO_INVALIDO")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse manejarExcepcionGenerica(Exception ex, HttpServletRequest request) {
        log.error("Error inesperado en el servidor: ", ex);
        return ErrorResponse.builder()
                .timestamp(System.currentTimeMillis())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("ERROR_INTERNO")
                .message("Ha ocurrido un error interno en el servidor. Por favor, intente más tarde.")
                .path(request.getRequestURI())
                .build();
    }
}
