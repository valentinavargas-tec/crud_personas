package ceiba.com.co.cita.controlador;

import ceiba.com.co.cita.consulta.DtoCita;
import ceiba.com.co.cita.consulta.DtoDisponibilidadDoctor;
import ceiba.com.co.cita.consulta.manejador.ManejadorConsultarCita;
import ceiba.com.co.cita.consulta.manejador.ManejadorConsultarCitasDoctor;
import ceiba.com.co.cita.consulta.manejador.ManejadorConsultarCitasPaciente;
import ceiba.com.co.cita.consulta.manejador.ManejadorConsultarDisponibilidadDoctor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/citas")
public class ConsultaControladorCita {

    private final ManejadorConsultarCita manejadorConsultarCita;
    private final ManejadorConsultarCitasPaciente manejadorConsultarCitasPaciente;
    private final ManejadorConsultarCitasDoctor manejadorConsultarCitasDoctor;
    private final ManejadorConsultarDisponibilidadDoctor manejadorConsultarDisponibilidadDoctor;

    public ConsultaControladorCita(ManejadorConsultarCita manejadorConsultarCita,
                                   ManejadorConsultarCitasPaciente manejadorConsultarCitasPaciente,
                                   ManejadorConsultarCitasDoctor manejadorConsultarCitasDoctor,
                                   ManejadorConsultarDisponibilidadDoctor manejadorConsultarDisponibilidadDoctor) {
        this.manejadorConsultarCita = manejadorConsultarCita;
        this.manejadorConsultarCitasPaciente = manejadorConsultarCitasPaciente;
        this.manejadorConsultarCitasDoctor = manejadorConsultarCitasDoctor;
        this.manejadorConsultarDisponibilidadDoctor = manejadorConsultarDisponibilidadDoctor;
    }

    @GetMapping("/{idCita}")
    public DtoCita buscarPorId(@PathVariable("idCita") Long idCita) {
        return this.manejadorConsultarCita.ejecutar(idCita);
    }

    @GetMapping("/paciente/{pacienteDocumento}")
    public List<DtoCita> buscarPorPaciente(@PathVariable("pacienteDocumento") String pacienteDocumento) {
        return this.manejadorConsultarCitasPaciente.ejecutar(pacienteDocumento);
    }

    @GetMapping("/doctor/{doctorDocumento}")
    public List<DtoCita> buscarPorDoctor(@PathVariable("doctorDocumento") String doctorDocumento) {
        return this.manejadorConsultarCitasDoctor.ejecutar(doctorDocumento);
    }

    @GetMapping("/doctor/{doctorDocumento}/disponibilidad")
    public DtoDisponibilidadDoctor consultarDisponibilidad(
            @PathVariable("doctorDocumento") String doctorDocumento,
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return this.manejadorConsultarDisponibilidadDoctor.ejecutar(doctorDocumento, fecha);
    }
}
