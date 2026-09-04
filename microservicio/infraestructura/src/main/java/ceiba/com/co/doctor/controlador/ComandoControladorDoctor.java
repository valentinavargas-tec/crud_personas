package ceiba.com.co.doctor.controlador;

import ceiba.com.co.ComandoRespuesta;
import ceiba.com.co.doctor.comando.ComandoActualizarDoctor;
import ceiba.com.co.doctor.comando.ComandoDoctor;
import ceiba.com.co.doctor.comando.manejador.ManejadorActualizarDoctor;
import ceiba.com.co.doctor.comando.manejador.ManejadorDeshabilitarDoctor;
import ceiba.com.co.doctor.comando.manejador.ManejadorRegistrarDoctor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/doctores")
public class ComandoControladorDoctor {

    private final ManejadorRegistrarDoctor manejadorRegistrarDoctor;
    private final ManejadorActualizarDoctor manejadorActualizarDoctor;
    private final ManejadorDeshabilitarDoctor manejadorDeshabilitarDoctor;

    public ComandoControladorDoctor(ManejadorRegistrarDoctor manejadorRegistrarDoctor, ManejadorActualizarDoctor manejadorActualizarDoctor, ManejadorDeshabilitarDoctor manejadorDeshabilitarDoctor) {
        this.manejadorRegistrarDoctor = manejadorRegistrarDoctor;
        this.manejadorActualizarDoctor = manejadorActualizarDoctor;
        this.manejadorDeshabilitarDoctor = manejadorDeshabilitarDoctor;

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ComandoRespuesta<String> registrar(@RequestBody ComandoDoctor comandoDoctor) {
        return this.manejadorRegistrarDoctor.ejecutar(comandoDoctor);
    }

    @PutMapping("/{numeroDocumento}")
    @ResponseStatus(HttpStatus.OK)
    public ComandoRespuesta<String> actualizar(
            @PathVariable ("numeroDocumento") String numeroDocumento,
            @RequestBody ComandoActualizarDoctor comandoActualizarDoctor) {
        return this.manejadorActualizarDoctor.ejecutar(numeroDocumento, comandoActualizarDoctor);
    }

    @DeleteMapping("/{numeroDocumento}")
    @ResponseStatus(HttpStatus.OK)
    public ComandoRespuesta<String> deshabilitar(@PathVariable("numeroDocumento") String numeroDocumento) {
        return this.manejadorDeshabilitarDoctor.ejecutar(numeroDocumento);
    }
}

