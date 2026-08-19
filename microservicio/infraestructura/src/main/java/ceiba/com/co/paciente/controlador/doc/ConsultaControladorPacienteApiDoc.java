package ceiba.com.co.paciente.controlador.doc;

import ceiba.com.co.infraestructura.error.ErrorRespuesta;
import ceiba.com.co.paciente.modelo.dto.Pagina;
import ceiba.com.co.paciente.modelo.dto.PacienteDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Pacientes", description = "Endpoints para la administración y consulta integral de Pacientes")
@SuppressWarnings("unused")
public interface ConsultaControladorPacienteApiDoc {

    @Operation(
            summary = "Listar todas las pacientes",
            description = "Retorna el listado completo de pacientes sin aplicar filtros ni paginación."
    )
    @org.springframework.web.bind.annotation.GetMapping
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Consulta exitosa",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PacienteDTO.class))
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
    List<PacienteDTO> listar();

    @Operation(
            summary = "Obtener paciente por número de documento",
            description = "Busca un paciente específico en la base de datos a partir de su número de documento."
    )
    @org.springframework.web.bind.annotation.GetMapping("/{numeroDocumento}")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Paciente encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PacienteDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Formato de cédula incorrecto",
                    content = @Content(
                            mediaType = "application/json", 
                            schema = @Schema(implementation = ErrorRespuesta.class),
                            examples = @ExampleObject(
                                    name = "Formato Incorrecto",
                                    value = """
                                    {
                                      "nombreExcepcion": "ExcepcionValorInvalido",
                                      "mensaje": "El valor ingresado para la cédula no es válido. Debe ser un número."
                                    }
                                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Paciente no encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorRespuesta.class),
                            examples = @ExampleObject(
                                    name = "Sin Resultados",
                                    value = """
                                    {
                                      "nombreExcepcion": "ExcepcionSinDatos",
                                      "mensaje": "No se encontró ninguna paciente registrada con la cédula: 1017123456"
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
    PacienteDTO buscarPorNumeroDocumento(
            @Parameter(description = "Número de documento del paciente", example = "1017123456", required = true)
            @PathVariable("numeroDocumento") Long numeroDocumento
    );

    @Operation(
            summary = "Búsqueda avanzada paginada",
            description = "Permite realizar búsquedas combinadas filtrando por coincidencias de nombre/apellido o rango de edad con soporte de ordenamiento y paginación."
    )
    @org.springframework.web.bind.annotation.GetMapping("/search")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Búsqueda paginada procesada con éxito",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pagina.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Criterios de búsqueda inválidos (ej. edad mínima mayor a la máxima)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorRespuesta.class),
                            examples = @ExampleObject(
                                    name = "Rango Invalido",
                                    value = """
                                    {
                                      "nombreExcepcion": "ExcepcionValorInvalido",
                                      "mensaje": "La edad mínima (50) no puede ser mayor que la edad máxima (18)"
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
    Pagina<PacienteDTO> buscarPorCriterios(
            @Parameter(description = "Filtro parcial por nombre", example = "Carlos")
            @RequestParam(required = false) String nombre,

            @Parameter(description = "Filtro parcial por apellido", example = "Pérez")
            @RequestParam(required = false) String apellido,

            @Parameter(description = "Edad mínima", example = "18")
            @RequestParam(required = false) Integer edadMinima,

            @Parameter(description = "Edad máxima", example = "65")
            @RequestParam(required = false) Integer edadMaxima,

            @Parameter(description = "Número de página (base 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Tamaño de la página", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Criterio de ordenación (campo,direccion)", example = "nombre,asc")
            @RequestParam(required = false) String sort
    );
}