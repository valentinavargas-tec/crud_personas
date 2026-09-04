package ceiba.com.co.paciente.controlador;

import ceiba.com.co.ComandoRespuesta;
import ceiba.com.co.paciente.comando.ComandoActualizarPaciente;
import ceiba.com.co.paciente.comando.ComandoEliminarPaciente;
import ceiba.com.co.paciente.comando.ComandoPaciente;
import ceiba.com.co.paciente.comando.manejador.ManejadorActualizarPaciente;
import ceiba.com.co.paciente.comando.manejador.ManejadorCrearPaciente;
import ceiba.com.co.paciente.comando.manejador.ManejadorEliminarPaciente;
import ceiba.com.co.paciente.controlador.doc.ComandoControladorPacienteApiDoc;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pacientes")
public class ComandoControladorPaciente implements ComandoControladorPacienteApiDoc {

    private final ManejadorCrearPaciente manejadorCrearPaciente;
    private final ManejadorActualizarPaciente manejadorActualizarPaciente;
    private final ManejadorEliminarPaciente manejadorEliminarPaciente;

    public ComandoControladorPaciente(ManejadorCrearPaciente manejadorCrearPaciente,
                                      ManejadorActualizarPaciente manejadorActualizarPaciente,
                                      ManejadorEliminarPaciente manejadorEliminarPaciente) {
        this.manejadorCrearPaciente = manejadorCrearPaciente;
        this.manejadorActualizarPaciente = manejadorActualizarPaciente;
        this.manejadorEliminarPaciente = manejadorEliminarPaciente;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Override
    public ComandoRespuesta<Long> crear(@RequestBody ComandoPaciente comandoPaciente) {
        return this.manejadorCrearPaciente.ejecutar(comandoPaciente);
    }


    @PutMapping("/{numeroDocumento}")
    @Override
    public ComandoRespuesta<Long> actualizar(@PathVariable("numeroDocumento") Long numeroDocumento, @RequestBody ComandoActualizarPaciente comando) {
        manejadorActualizarPaciente.ejecutar(numeroDocumento, comando);
        return new ComandoRespuesta<>(numeroDocumento, "Paciente actualizado exitosamente");
    }

    @DeleteMapping("/{numeroDocumento}")
    @Override
    public ComandoRespuesta<Long> eliminar(@PathVariable("numeroDocumento") Long numeroDocumento) {
        this.manejadorEliminarPaciente.ejecutar(new ComandoEliminarPaciente(numeroDocumento));
        return new ComandoRespuesta<>(numeroDocumento, "Paciente eliminado exitosamente");
    }
}
