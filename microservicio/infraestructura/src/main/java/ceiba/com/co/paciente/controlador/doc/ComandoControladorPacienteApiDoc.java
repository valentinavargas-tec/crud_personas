package ceiba.com.co.paciente.controlador.doc;

import ceiba.com.co.ComandoRespuesta;
import ceiba.com.co.paciente.comando.ComandoActualizarPaciente;
import ceiba.com.co.paciente.comando.ComandoPaciente;
import ceiba.com.co.infraestructura.error.ErrorRespuesta;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Pacientes", description = "Endpoints para la administración y consulta integral de Pacientes")
public interface ComandoControladorPacienteApiDoc {

    @Operation(
            summary = "Crear paciente",
            description = "Valida las reglas de negocio e inserta una nueva paciente. La cédula y el correo deben ser únicos."
    )

    @org.springframework.web.bind.annotation.PostMapping
    @RequestBody(

            description = "Estructura requerida para registrar una nueva paciente",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ComandoPaciente.class),
                    examples = @ExampleObject(
                            name = "Crear Paciente - Éxito",
                            summary = "Payload básico de creación",
                            value = """
                            {
                              "numeroDocumento": 1017123456,
                              "tipoDocumento": "CEDULA_CIUDADANIA",
                              "nombre": "Carlos",
                              "apellido": "Pérez",
                              "fechaNacimiento": "1990-05-15",
                              "telefono": "3001234567",
                              "correoElectronico": "carlos.perez@example.com",
                              "eps": "SURA",
                              "genero": "MASCULINO"
                            }
                            """
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Paciente creada exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ComandoRespuesta.class),
                            examples = @ExampleObject(
                                    name = "Paciente Creada",
                                    value = """
                                    {
                                      "valor": 1017123456
                                    }DisplayName
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Violación de reglas de negocio (campos obligatorios o cédula/email duplicados)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorRespuesta.class),
                            examples = @ExampleObject(
                                    name = "Duplicidad de Cédula",
                                    value = """
                                    {
                                      "nombreExcepcion": "ExcepcionDuplicidad",
                                      "mensaje": "La paciente con cédula 1017123456 ya se encuentra registrada."
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
                            schema = @Schema(implementation = ErrorRespuesta.class),
                            examples = @ExampleObject(
                                    name = "Error de Sistema",
                                    value = """
                                    {
                                      "nombreExcepcion": "ExcepcionTecnica",
                                      "mensaje": "Ocurrió un error favor contactar al administrador."
                                    }
                                    """
                            )
                    )
            )
    })
    ComandoRespuesta<Long> crear(
            @org.springframework.web.bind.annotation.RequestBody ComandoPaciente comandoPaciente
    );

    @Operation(
            summary = "Actualizar paciente",
            description = "Actualiza los datos modificables (nombre, apellido, email, fecha de nacimiento) de una paciente existente por su cédula."
    )
    @org.springframework.web.bind.annotation.PutMapping("/{numeroDocumento}")
    @RequestBody(
            description = "Campos actualizables de la paciente",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ComandoActualizarPaciente.class),
                    examples = @ExampleObject(
                            name = "Actualizar Paciente",
                            value = """
                            {
                              "nombre": "Carlos Alberto",
                              "apellido": "Pérez Gómez",
                              "fechaNacimiento": "1990-05-15",
                              "telefono": "3007654321",
                              "correoElectronico": "carlos.perez.updated@example.com",
                              "eps": "SURA",
                              "genero": "MASCULINO"
                            }
                            """
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Paciente actualizada correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ComandoRespuesta.class),
                            examples = @ExampleObject(
                                    name = "Paciente Actualizada",
                                    value = """
                                    {
                                      "valor": 1017123456
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos o formato incorrecto",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorRespuesta.class),
                            examples = @ExampleObject(
                                    name = "Paciente Inexistente",
                                    value = """
                                    {
                                      "nombreExcepcion": "ExcepcionSinDatos",
                                      "mensaje": "No existe una paciente registrada con la cédula: 9999999"
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
                            schema = @Schema(implementation = ErrorRespuesta.class),
                            examples = @ExampleObject(
                                    name = "Error de Sistema",
                                    value = """
                                    {
                                      "nombreExcepcion": "ExcepcionTecnica",
                                      "mensaje": "Ocurrió un error favor contactar al administrador."
                                    }
                                    """
                            )
                    )
            )
    })
    ComandoRespuesta<Long> actualizar(
            @Parameter(description = "Número de documento único", example = "1017123456", required = true)
            @PathVariable("numeroDocumento") Long numeroDocumento,

            @org.springframework.web.bind.annotation.RequestBody ComandoActualizarPaciente comando
    );

    @Operation(
            summary = "Eliminar paciente",
            description = "Elimina de forma permanente el registro de una paciente por su número de cédula."
    )
    @org.springframework.web.bind.annotation.DeleteMapping("/{numeroDocumento}")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Paciente eliminado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ComandoRespuesta.class),
                            examples = @ExampleObject(
                                    name = "Paciente Eliminado",
                                    value = """
                                    {
                                      "valor": 1017123456,
                                      "mensaje": "Paciente eliminado exitosamente"
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Formato de cédula incorrecto",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorRespuesta.class),
                            examples = @ExampleObject(
                                    name = "Paciente Inexistente",
                                    value = """
                                    {
                                      "nombreExcepcion": "ExcepcionSinDatos",
                                      "mensaje": "No se encontró la paciente con cédula 9999999"
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
                            schema = @Schema(implementation = ErrorRespuesta.class),
                            examples = @ExampleObject(
                                    name = "Error de Sistema",
                                    value = """
                                    {
                                      "nombreExcepcion": "ExcepcionTecnica",
                                      "mensaje": "Ocurrió un error favor contactar al administrador."
                                    }
                                    """
                            )
                    )
            )
    })
    ComandoRespuesta<Long> eliminar(
            @Parameter(description = "Número de documento del paciente", example = "1017123456", required = true)
            @PathVariable("numeroDocumento") Long numeroDocumento
    );
}