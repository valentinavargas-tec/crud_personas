package ceiba.com.co.infraestructura.error;


import java.util.concurrent.ConcurrentHashMap;

import ceiba.com.co.infraestructura.excepcion.ExcepcionTecnica;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import ceiba.com.co.excepcion.ExcepcionConflictoHorario;
import ceiba.com.co.excepcion.ExcepcionDuplicidad;
import ceiba.com.co.excepcion.ExcepcionLongitudValor;
import ceiba.com.co.excepcion.ExcepcionReglaNegocio;
import ceiba.com.co.excepcion.ExcepcionSinDatos;
import ceiba.com.co.excepcion.ExcepcionValorInvalido;
import ceiba.com.co.excepcion.ExcepcionValorObligatorio;

@ControllerAdvice
public class ManejadorError extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER_ERROR = LoggerFactory.getLogger(ManejadorError.class);

    private static final String OCURRIO_UN_ERROR_FAVOR_CONTACTAR_AL_ADMINISTRADOR = "Ocurrió un error favor contactar al administrador.";

    private static final ConcurrentHashMap<String, Integer> CODIGOS_ESTADO = new ConcurrentHashMap<>();

    public ManejadorError() {
        CODIGOS_ESTADO.put(ExcepcionLongitudValor.class.getSimpleName(), HttpStatus.BAD_REQUEST.value());
        CODIGOS_ESTADO.put(ExcepcionValorInvalido.class.getSimpleName(), HttpStatus.BAD_REQUEST.value());
        CODIGOS_ESTADO.put(ExcepcionSinDatos.class.getSimpleName(), HttpStatus.NOT_FOUND.value());
        CODIGOS_ESTADO.put(ExcepcionValorObligatorio.class.getSimpleName(), HttpStatus.BAD_REQUEST.value());
        CODIGOS_ESTADO.put(ExcepcionDuplicidad.class.getSimpleName(), HttpStatus.CONFLICT.value());
        CODIGOS_ESTADO.put(ExcepcionConflictoHorario.class.getSimpleName(), HttpStatus.CONFLICT.value());
        CODIGOS_ESTADO.put(ExcepcionReglaNegocio.class.getSimpleName(), HttpStatus.BAD_REQUEST.value());
        CODIGOS_ESTADO.put(ExcepcionTecnica.class.getSimpleName(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        CODIGOS_ESTADO.put(IllegalArgumentException.class.getSimpleName(), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorRespuesta> handleAllExceptions(Exception exception) {
        ResponseEntity<ErrorRespuesta> resultado;

        String excepcionNombre = exception.getClass().getSimpleName();
        String mensaje = exception.getMessage();
        HttpStatus status = resolverHttpStatus(exception);

        if (status != null) {
            ErrorRespuesta error = new ErrorRespuesta(excepcionNombre, mensaje);
            resultado = new ResponseEntity<>(error, status);
        } else {
            LOGGER_ERROR.error(excepcionNombre, exception);
            ErrorRespuesta error = new ErrorRespuesta(excepcionNombre, OCURRIO_UN_ERROR_FAVOR_CONTACTAR_AL_ADMINISTRADOR);
            resultado = new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return resultado;
    }

    private HttpStatus resolverHttpStatus(Exception exception) {
        if (exception instanceof ExcepcionSinDatos) {
            return HttpStatus.NOT_FOUND;
        }
        if (exception instanceof ExcepcionDuplicidad || exception instanceof ExcepcionConflictoHorario) {
            return HttpStatus.CONFLICT;
        }
        if (exception instanceof ExcepcionValorObligatorio
                || exception instanceof ExcepcionValorInvalido
                || exception instanceof ExcepcionLongitudValor
                || exception instanceof ExcepcionReglaNegocio
                || exception instanceof IllegalArgumentException) {
            return HttpStatus.BAD_REQUEST;
        }
        if (exception instanceof ExcepcionTecnica) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        Integer codigo = CODIGOS_ESTADO.get(exception.getClass().getSimpleName());
        if (codigo != null) {
            return HttpStatus.valueOf(codigo);
        }
        return null;
    }

}