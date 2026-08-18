package ceiba.com.co.controlador.doc;

import ceiba.com.co.ComandoRespuesta;
import ceiba.com.co.comando.ComandoActualizarPersona;
import ceiba.com.co.comando.ComandoPersona;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Personas", description = "Endpoints para la administración y consulta integral de Personas")
@SuppressWarnings("unused")
public interface ComandoControladorPersonaApiDoc {

    @Operation(
            summary = "Crear persona",
            description = "Valida las reglas de negocio e inserta una nueva persona. La cédula y el correo deben ser únicos."
    )

    @org.springframework.web.bind.annotation.PostMapping
    @RequestBody(

            description = "Estructura requerida para registrar una nueva persona",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ComandoPersona.class),
                    examples = @ExampleObject(
                            name = "Crear Persona - Éxito",
                            summary = "Payload básico de creación",
                            value = """
                            {
                              "cedula": 1017123456,
                              "nombre": "Carlos",
                              "apellido": "Pérez",
                              "email": "carlos.perez@example.com",
                              "fechaNacimiento": "1990-05-15"
                            }
                            """
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Persona creada exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ComandoRespuesta.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Violación de reglas de negocio (campos obligatorios o cédula/email duplicados)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    name = "Duplicidad de Cédula",
                                    value = """
                                    {
                                      "type": "about:blank",
                                      "title": "ExcepcionDuplicidad",
                                      "status": 400,
                                      "detail": "La persona con cédula 1017123456 ya se encuentra registrada.",
                                      "instance": "/api/personas"
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno no controlado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    name = "Error de Sistema",
                                    value = """
                                    {
                                      "type": "about:blank",
                                      "title": "ExcepcionTecnica",
                                      "status": 500,
                                      "detail": "Ha ocurrido un error inesperado al procesar la solicitud.",
                                      "instance": "/api/personas"
                                    }
                                    """
                            )
                    )
            )
    })
    ComandoRespuesta<Long> crear(
            @org.springframework.web.bind.annotation.RequestBody ComandoPersona comandoPersona
    );

    @Operation(
            summary = "Actualizar persona",
            description = "Actualiza los datos modificables (nombre, apellido, email, fecha de nacimiento) de una persona existente por su cédula."
    )
    @org.springframework.web.bind.annotation.PutMapping("/{cedula}")
    @RequestBody(
            description = "Campos actualizables de la persona",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ComandoActualizarPersona.class),
                    examples = @ExampleObject(
                            name = "Actualizar Persona",
                            value = """
                            {
                              "nombre": "Carlos Alberto",
                              "apellido": "Pérez Gómez",
                              "email": "carlos.perez.updated@example.com",
                              "fechaNacimiento": "1990-05-15"
                            }
                            """
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Persona actualizada correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ComandoRespuesta.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos o formato incorrecto",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Persona no encontrada para actualizar",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    name = "Persona Inexistente",
                                    value = """
                                    {
                                      "type": "about:blank",
                                      "title": "ExcepcionSinDatos",
                                      "status": 404,
                                      "detail": "No existe una persona registrada con la cédula: 9999999",
                                      "instance": "/api/personas/9999999"
                                    }
                                    """
                            )
                    )
            )
    })
    ComandoRespuesta<Long> actualizar(
            @Parameter(description = "Número de cédula único", example = "1017123456", required = true)
            @PathVariable("cedula") Long cedula,

            @org.springframework.web.bind.annotation.RequestBody ComandoActualizarPersona comando
    );

    @Operation(
            summary = "Eliminar persona",
            description = "Elimina de forma permanente el registro de una persona por su número de cédula."
    )
    @org.springframework.web.bind.annotation.DeleteMapping("/{cedula}")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Persona eliminada exitosamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cédula no encontrada para eliminar",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    void eliminar(
            @Parameter(description = "Número de cédula de la persona", example = "1017123456", required = true)
            @PathVariable("cedula") Long cedula
    );
}