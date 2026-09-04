package ceiba.com.co.cita.controlador;

import ceiba.com.co.ComandoRespuesta;
import ceiba.com.co.cita.comando.ComandoActualizarCita;
import ceiba.com.co.cita.comando.ComandoAgendarCita;
import ceiba.com.co.cita.comando.ComandoCancelarCita;
import ceiba.com.co.cita.comando.ComandoReasignarCita;
import ceiba.com.co.cita.comando.manejador.ManejadorActualizarCita;
import ceiba.com.co.cita.comando.manejador.ManejadorAgendarCita;
import ceiba.com.co.cita.comando.manejador.ManejadorCancelarCita;
import ceiba.com.co.cita.comando.manejador.ManejadorReasignarCita;
import ceiba.com.co.cita.consulta.DtoCita;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/citas")
public class ComandoControladorCita {

    private final ManejadorAgendarCita manejadorAgendarCita;
    private final ManejadorActualizarCita manejadorActualizarCita;
    private final ManejadorCancelarCita manejadorCancelarCita;
    private final ManejadorReasignarCita manejadorReasignarCita;

    public ComandoControladorCita(ManejadorAgendarCita manejadorAgendarCita,
                                  ManejadorActualizarCita manejadorActualizarCita,
                                  ManejadorCancelarCita manejadorCancelarCita,
                                  ManejadorReasignarCita manejadorReasignarCita) {
        this.manejadorAgendarCita = manejadorAgendarCita;
        this.manejadorActualizarCita = manejadorActualizarCita;
        this.manejadorCancelarCita = manejadorCancelarCita;
        this.manejadorReasignarCita = manejadorReasignarCita;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ComandoRespuesta<DtoCita> agendar(@RequestBody ComandoAgendarCita comando) {
        return this.manejadorAgendarCita.ejecutar(comando);
    }

    @PutMapping("/{idCita}")
    @ResponseStatus(HttpStatus.OK)
    public ComandoRespuesta<DtoCita> reprogramar(
            @PathVariable("idCita") Long idCita,
            @RequestBody ComandoActualizarCita comando) {
        return this.manejadorActualizarCita.ejecutar(idCita, comando);
    }

    @PatchMapping("/{idCita}/cancelar")
    @ResponseStatus(HttpStatus.OK)
    public ComandoRespuesta<DtoCita> cancelar(
            @PathVariable("idCita") Long idCita,
            @RequestBody(required = false) ComandoCancelarCita comando) {
        return this.manejadorCancelarCita.ejecutar(idCita, comando);
    }

    @PatchMapping("/{idCita}/reasignar")
    @ResponseStatus(HttpStatus.OK)
    public ComandoRespuesta<DtoCita> reasignar(
            @PathVariable("idCita") Long idCita,
            @RequestBody ComandoReasignarCita comando) {
        return this.manejadorReasignarCita.ejecutar(idCita, comando);
    }
}
