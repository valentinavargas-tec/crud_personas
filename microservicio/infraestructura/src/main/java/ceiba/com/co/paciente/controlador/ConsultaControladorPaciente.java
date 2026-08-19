package ceiba.com.co.paciente.controlador;

import ceiba.com.co.paciente.consulta.ManejadorBuscarPacientePorNumeroDocumento;
import ceiba.com.co.paciente.consulta.ManejadorBuscarPacientesPorCriterios;
import ceiba.com.co.paciente.consulta.ManejadorListarPacientes;
import ceiba.com.co.paciente.controlador.doc.ConsultaControladorPacienteApiDoc;
import ceiba.com.co.paciente.modelo.dto.CriteriosBusquedaPaciente;
import ceiba.com.co.paciente.modelo.dto.Pagina;
import ceiba.com.co.paciente.modelo.dto.PacienteDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
public class ConsultaControladorPaciente implements ConsultaControladorPacienteApiDoc {

    private final ManejadorListarPacientes manejadorListarPacientes;
    private final ManejadorBuscarPacientePorNumeroDocumento manejadorBuscarPacientePorNumeroDocumento;
    private final ManejadorBuscarPacientesPorCriterios manejadorBuscarPacientesPorCriterios;

    public ConsultaControladorPaciente(ManejadorListarPacientes manejadorListarPacientes,
                                     ManejadorBuscarPacientePorNumeroDocumento manejadorBuscarPacientePorNumeroDocumento,
                                     ManejadorBuscarPacientesPorCriterios manejadorBuscarPacientesPorCriterios) {
        this.manejadorListarPacientes = manejadorListarPacientes;
        this.manejadorBuscarPacientePorNumeroDocumento = manejadorBuscarPacientePorNumeroDocumento;
        this.manejadorBuscarPacientesPorCriterios = manejadorBuscarPacientesPorCriterios;
    }

    @GetMapping
    @Override
    public List<PacienteDTO> listar() {
        return this.manejadorListarPacientes.ejecutar();
    }

    @GetMapping("/{numeroDocumento}")
    @Override
    public PacienteDTO buscarPorNumeroDocumento(@PathVariable("numeroDocumento") Long numeroDocumento) {
        return this.manejadorBuscarPacientePorNumeroDocumento.ejecutar(numeroDocumento);
    }

    @GetMapping("/search")
    @Override
    public Pagina<PacienteDTO> buscarPorCriterios(
            @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "apellido", required = false) String apellido,
            @RequestParam(value = "edadMinima", required = false) Integer edadMinima,
            @RequestParam(value = "edadMaxima", required = false) Integer edadMaxima,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", required = false) String sort) {

        CriteriosBusquedaPaciente criterios = new CriteriosBusquedaPaciente(
                nombre, apellido, edadMinima, edadMaxima, page, size, sort
        );
        return this.manejadorBuscarPacientesPorCriterios.ejecutar(criterios);
    }
}
