package ceiba.com.co.controlador.doc;

import ceiba.com.co.modelo.dto.Pagina;
import ceiba.com.co.modelo.dto.PersonaDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Personas", description = "Endpoints para la administración y consulta integral de Personas")
@SuppressWarnings("unused")
public interface ConsultaControladorPersonaApiDoc {

    @Operation(
            summary = "Listar todas las personas",
            description = "Retorna el listado completo de personas sin aplicar filtros ni paginación."
    )
    @org.springframework.web.bind.annotation.GetMapping
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Consulta exitosa",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonaDTO.class))
            )
    })
    List<PersonaDTO> listar();

    @Operation(
            summary = "Obtener persona por cédula",
            description = "Busca una persona específica en la base de datos a partir de su cédula."
    )
    @org.springframework.web.bind.annotation.GetMapping("/{cedula}")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Persona encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonaDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Persona no encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    name = "Sin Resultados",
                                    value = """
                                    {
                                      "type": "about:blank",
                                      "title": "ExcepcionSinDatos",
                                      "status": 404,
                                      "detail": "No se encontró ninguna persona registrada con la cédula: 1017123456",
                                      "instance": "/api/personas/1017123456"
                                    }
                                    """
                            )
                    )
            )
    })
    PersonaDTO buscarPorCedula(
            @Parameter(description = "Cédula de la persona", example = "1017123456", required = true)
            @PathVariable("cedula") Long cedula
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
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    name = "Rango Invalido",
                                    value = """
                                    {
                                      "type": "about:blank",
                                      "title": "ExcepcionValorInvalido",
                                      "status": 400,
                                      "detail": "La edad mínima (50) no puede ser mayor que la edad máxima (18)",
                                      "instance": "/api/personas/search"
                                    }
                                    """
                            )
                    )
            )
    })
    Pagina<PersonaDTO> buscarPorCriterios(
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