package ceiba.com.co.cita.controlador;

import ceiba.com.co.ComandoRespuesta;
import ceiba.com.co.cita.comando.ComandoAgendarCita;
import ceiba.com.co.cita.comando.manejador.ManejadorAgendarCita;
import ceiba.com.co.cita.consulta.DtoCita;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/citas")
public class ComandoControladorCita {

    private final ManejadorAgendarCita manejadorAgendarCita;

    public ComandoControladorCita(ManejadorAgendarCita manejadorAgendarCita) {
        this.manejadorAgendarCita = manejadorAgendarCita;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ComandoRespuesta<DtoCita> agendar(@RequestBody ComandoAgendarCita comando) {
        return this.manejadorAgendarCita.ejecutar(comando);
    }
}
