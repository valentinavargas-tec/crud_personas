package ceiba.com.co.controlador;

import ceiba.com.co.consulta.ManejadorBuscarPersonaPorCedula;
import ceiba.com.co.consulta.ManejadorBuscarPersonasPorCriterios;
import ceiba.com.co.consulta.ManejadorListarPersonas;
import ceiba.com.co.controlador.doc.ConsultaControladorPersonaApiDoc;
import ceiba.com.co.modelo.dto.CriteriosBusquedaPersona;
import ceiba.com.co.modelo.dto.Pagina;
import ceiba.com.co.modelo.dto.PersonaDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personas")
public class ConsultaControladorPersona implements ConsultaControladorPersonaApiDoc {

    private final ManejadorListarPersonas manejadorListarPersonas;
    private final ManejadorBuscarPersonaPorCedula manejadorBuscarPersonaPorCedula;
    private final ManejadorBuscarPersonasPorCriterios manejadorBuscarPersonasPorCriterios;

    public ConsultaControladorPersona(ManejadorListarPersonas manejadorListarPersonas,
                                     ManejadorBuscarPersonaPorCedula manejadorBuscarPersonaPorCedula,
                                     ManejadorBuscarPersonasPorCriterios manejadorBuscarPersonasPorCriterios) {
        this.manejadorListarPersonas = manejadorListarPersonas;
        this.manejadorBuscarPersonaPorCedula = manejadorBuscarPersonaPorCedula;
        this.manejadorBuscarPersonasPorCriterios = manejadorBuscarPersonasPorCriterios;
    }

    @GetMapping
    @Override
    public List<PersonaDTO> listar() {
        return this.manejadorListarPersonas.ejecutar();
    }

    @GetMapping("/{cedula}")
    @Override
    public PersonaDTO buscarPorCedula(@PathVariable("cedula") Long cedula) {
        return this.manejadorBuscarPersonaPorCedula.ejecutar(cedula);
    }

    @GetMapping("/search")
    @Override
    public Pagina<PersonaDTO> buscarPorCriterios(
            @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "apellido", required = false) String apellido,
            @RequestParam(value = "edadMinima", required = false) Integer edadMinima,
            @RequestParam(value = "edadMaxima", required = false) Integer edadMaxima,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", required = false) String sort) {

        CriteriosBusquedaPersona criterios = new CriteriosBusquedaPersona(
                nombre, apellido, edadMinima, edadMaxima, page, size, sort
        );
        return this.manejadorBuscarPersonasPorCriterios.ejecutar(criterios);
    }
}
